package ${package_base}.controller;

import ${package_base}.utils.ClassUtils;
import com.github.tennaito.rsql.jpa.JpaCriteriaCountQueryVisitor;
import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
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
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.lang.reflect.InvocationTargetException;
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
            @RequestParam(value = "query") String query,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "perPage", defaultValue = "1000") Integer perPage
            ) throws Exception {

        Class<?> entityClass = ClassUtils.findClass(entityName, "${package_base}.models");
        Assert.notNull(entityClass, "entity not found for " + entityName);

        CriteriaQuery<?> querySpec = buildCriteriaQuery(query, entityClass);
        //List<?> entityList = entityManager.createQuery(querySpec).getResultList();

        TypedQuery<?> typedSpec = entityManager.createQuery(querySpec);
        typedSpec.setFirstResult((pageNum - 1) * perPage);
        typedSpec.setMaxResults(perPage);
        List<?> entityList = typedSpec.getResultList();

        log.info("SEARCH {} WHERE query AND page={} AND size={} ; found={}",
                entityName, pageNum, perPage, entityList.size());
        return ResponseEntity.ok().body(entityList);
    }

    @RequestMapping(value = "/count/{entity}", method = RequestMethod.GET)
    public ResponseEntity<Long> countByQuery(
            @PathVariable(value = "entity") String entityName,
            @RequestParam(value = "query") String query
            ) throws Exception {

        Class<?> entityClass = ClassUtils.findClass(entityName, "${package_base}.models");
        Assert.notNull(entityClass, "entity not found for " + entityName);

        CriteriaQuery<Long> querySpec = buildCountQuery(query, entityClass);
        Long entityCount = entityManager.createQuery(querySpec).getSingleResult();

        log.info("SEARCH {} WHERE query ; count={}", entityName, entityCount);
        return ResponseEntity.ok().body(entityCount);
    }

    private <T> CriteriaQuery<T> buildCriteriaQuery(String query, Class<T> entityClass) {
        T entityObj = constructInstance(entityClass);
        RSQLVisitor<CriteriaQuery<T>, EntityManager> visitor = new JpaCriteriaQueryVisitor<T>(entityObj);
        try {
            Node rootNode = new RSQLParser().parse(query);
            CriteriaQuery<T> querySpec = rootNode.accept(visitor, entityManager);
            return querySpec;
        } catch (Exception e) {
            throw new RuntimeException("failed to parse rsql query", e);
        }
    }

    private <T> CriteriaQuery<Long> buildCountQuery(String query, Class<T> entityClass) {
        T entityObj = constructInstance(entityClass);
        RSQLVisitor<CriteriaQuery<Long>, EntityManager> visitor = new JpaCriteriaCountQueryVisitor<T>(entityObj);
        try {
            Node rootNode = new RSQLParser().parse(query);
            CriteriaQuery<Long> countSpec = rootNode.accept(visitor, entityManager);
            return countSpec;
        } catch (Exception e) {
            throw new RuntimeException("failed to parse rsql query", e);
        }
    }

    private static <T> T constructInstance(Class<T> entityClass) {
        if(entityClass == null)
            return null;
        String className = entityClass.getSimpleName();
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if(tableAnnotation == null)
            throw new RuntimeException("class "+className+" is not an entity");
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException ie) {
            throw new RuntimeException(ie);
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException(ite);
        } catch (NoSuchMethodException nsme) {
            throw new RuntimeException(nsme);
        }
    }
}