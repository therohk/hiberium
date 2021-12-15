package ${package_base}.controller;

import ${package_base}.utils.ClassUtils;
import ${package_base}.utils.EntityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
  * generic search endpoint for any jpa entity
  * field names in query are same as json labels
  * rsql compatible query string required
  * @author therohk 2021/10/30
  */
@Slf4j
@Controller
public class SearchEntityController {

    @Autowired
    private EntityManager entityManager;

    @RequestMapping(value = "/query/{entity}", method = RequestMethod.GET)
    public ResponseEntity<List<?>> searchByQuery(
            @PathVariable(value = "entity") String entityName,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "perPage", defaultValue = "1000") Integer perPage
            ) throws Exception {

        Class<?> entityClass = ClassUtils.findClass(entityName, "${package_base}.models");
        Assert.notNull(entityClass, "entity not found for " + entityName);

        CriteriaQuery<?> querySpec = EntityUtils.buildCriteriaQuery(entityManager, entityClass, query);
        TypedQuery<?> typedSpec = EntityUtils.buildTypedQuery(entityManager, querySpec, pageNum, perPage);
        List<?> entityList = typedSpec.getResultList();

        log.info("SEARCH {} WHERE query AND page={} AND size={} ; found={}",
                entityName, pageNum, perPage, entityList.size());
        return ResponseEntity.ok().body(entityList);
    }

    @RequestMapping(value = "/count/{entity}", method = RequestMethod.GET)
    public ResponseEntity<Long> countByQuery(
            @PathVariable(value = "entity") String entityName,
            @RequestParam(value = "query", required = false) String query
            ) throws Exception {

        Class<?> entityClass = ClassUtils.findClass(entityName, "${package_base}.models");
        Assert.notNull(entityClass, "entity not found for " + entityName);

        CriteriaQuery<Long> querySpec = EntityUtils.buildCountQuery(entityManager, entityClass, query);
        Long entityCount = entityManager.createQuery(querySpec).getSingleResult();

        log.info("SEARCH {} WHERE query ; count={}", entityName, entityCount);
        return ResponseEntity.ok().body(entityCount);
    }
}