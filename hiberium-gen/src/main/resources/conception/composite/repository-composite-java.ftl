package ${package_base}.jdbc.repository.${module_name};

import ${package_base}.models.${module_name}.${concept_name}Composite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * read only repository for composite entities
 * can query linked concepts using multi select
 */
public interface ${concept_name}CompositeRepository extends JpaRepository<${concept_name}Composite, Integer> {

    default ${concept_name}Composite getOne(Integer primaryKey) {
        return findById(primaryKey).orElse(null);
    }

    default <S extends ${concept_name}Composite> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    default ${concept_name}Composite save(${concept_name}Composite entity) {
        return null;
    }

    default void delete(${concept_name}Composite entity) { }
}