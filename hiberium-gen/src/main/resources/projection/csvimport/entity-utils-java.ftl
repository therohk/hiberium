package ${package_base}.utils;

import com.github.tennaito.rsql.jpa.JpaCriteriaCountQueryVisitor;
import com.github.tennaito.rsql.jpa.JpaCriteriaQueryVisitor;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.lang.reflect.InvocationTargetException;

public final class EntityUtils {

    private EntityUtils() { }

    /**
     * create empty criteria query
     */
    public static <T> CriteriaQuery<T> buildCriteriaQuery(EntityManager em, Class<T> entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> querySpec = cb.createQuery(entityClass);
        Root<T> root = querySpec.from(entityClass);
        querySpec.select(root);
        return querySpec;
    }

    /**
     * create criteria query with where clause from rsql query
     */
    public static <T> CriteriaQuery<T> buildCriteriaQuery(EntityManager em, Class<T> entityClass, String query) {
        T entityObj = constructInstance(entityClass);
        RSQLVisitor<CriteriaQuery<T>, EntityManager> visitor = new JpaCriteriaQueryVisitor<T>(entityObj);
        try {
            Node rootNode = new RSQLParser().parse(query);
            CriteriaQuery<T> querySpec = rootNode.accept(visitor, em);
            return querySpec;
        } catch (Exception e) {
            throw new RuntimeException("failed to parse rsql query", e);
        }
    }

    public static <T> CriteriaQuery<Long> buildCountQuery(EntityManager em, Class<T> entityClass) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> countSpec = cb.createQuery(Long.class);
        Root<T> root = countSpec.from(entityClass);
        countSpec.select(cb.countDistinct(root));
        return countSpec;
    }

    public static <T> CriteriaQuery<Long> buildCountQuery(EntityManager em, Class<T> entityClass, String query) {
        T entityObj = constructInstance(entityClass);
        RSQLVisitor<CriteriaQuery<Long>, EntityManager> visitor = new JpaCriteriaCountQueryVisitor<T>(entityObj);
        try {
            Node rootNode = new RSQLParser().parse(query);
            CriteriaQuery<Long> countSpec = rootNode.accept(visitor, em);
            return countSpec;
        } catch (Exception e) {
            throw new RuntimeException("failed to parse rsql query", e);
        }
    }

    /**
     * convert criteria query to paginated query
     */
    public static <T> TypedQuery<T> buildTypedQuery(EntityManager em, CriteriaQuery<T> querySpec, Integer pageNum, Integer perPage) {
        TypedQuery<T> typedSpec = em.createQuery(querySpec);
        typedSpec.setFirstResult((pageNum - 1) * perPage);
        typedSpec.setMaxResults(perPage);
        return typedSpec;
    }

    public static <T> T constructInstance(Class<T> entityClass) {
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