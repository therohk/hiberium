package ${package_base}.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ${package_base}.models.StoredObject;
import ${package_base}.utils.ClassUtils;
import ${package_base}.utils.EntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * generic browser for any jpa entity
 * generates model for browse-entity-html.ftl
 * rsql compatible or blank query string required
 * @author therohk 2021/12/10
 */
@Slf4j
@Controller
public class BrowseEntityController {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private Jackson2ObjectMapperBuilder mapperBuilder;

    @RequestMapping(value = "/browser/{entity}", method = RequestMethod.GET)
    public String browseByQuery(
            @PathVariable(value = "entity") String entityName,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "perPage", defaultValue = "50") Integer perPage,
            Model model
    ) throws Exception {

        Class<?> entityClass = ClassUtils.findClass(entityName, "${package_base}.models");
        StoredObject<?> entityObj = (StoredObject) EntityUtils.constructInstance(entityClass);
        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

        perPage = Math.max(1, perPage);
        model.addAttribute("entity", entityName);
        model.addAttribute("apiPath", entityObj.apiPath());
        model.addAttribute("headers", entityObj.fetchFieldNamesAsList());
        model.addAttribute("query", query);
        model.addAttribute("perPage", perPage);

        CriteriaQuery<Long> queryCount = EntityUtils.buildCountQuery(entityManager, entityClass, query);
        Long entityCount = entityManager.createQuery(queryCount).getSingleResult();
        Integer pageMax = (int) Math.ceil((double) entityCount / (double) perPage);

        pageMax = Math.max(1, pageMax);
        pageNum = pageNum > pageMax ? pageMax : pageNum;
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalItems", entityCount);
        model.addAttribute("pageMax", pageMax);
        model.addAttribute("results", results);

        if(entityCount == 0) {
            log.info("SEARCH {} WHERE query AND page={} AND size={} ; total=0",
                    entityName, pageNum, perPage);
            return "browse-Entity";
        }

        CriteriaQuery<?> querySpec = EntityUtils.buildCriteriaQuery(entityManager, entityClass, query);
        TypedQuery<?> typedSpec = EntityUtils.buildTypedQuery(entityManager, querySpec, pageNum, perPage);
        List<?> entityList = typedSpec.getResultList();

        ObjectMapper mapper = mapperBuilder.build();
        TypeReference<Map<String,Object>> resultType = new TypeReference<Map<String,Object>>(){};
        for (Object entity : entityList) {
            StoredObject<?> storedObj = (StoredObject<?>) entity;
            Map<String, Object> result = mapper.convertValue(storedObj, resultType);
            result.put("id", storedObj.primaryKey());
            results.add(result);
        }
        model.addAttribute("results", results);

        log.info("SEARCH {} WHERE query AND page={} AND size={} ; total={} ; paged={}",
                entityName, pageNum, perPage, entityCount, results.size());
        return "browse-Entity";
    }
}
