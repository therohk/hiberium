package ${package_base}.jdbc.repository.${module_name};

import ${package_base}.models.${module_name}.${concept_name};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ${concept_name}Repository extends JpaRepository<${concept_name}, Integer> {

    default ${concept_name} getOne(Integer id) {
        return findById(id).orElse(null);
    }

    @Query(value = " SELECT * FROM ${concept_schema}.${concept_table} " +
            " LIMIT ?1 OFFSET ?2 "
            , nativeQuery = true)
    List<${concept_name}> findAllByLimitAndOffset(Integer limit, Integer offset);

<#list attributes as attribute>
    <#if !attribute.attribute_role?contains("R")><#continue></#if>
    List<${concept_name}> findBy${attribute.attribute_name?cap_first}(${attribute.attribute_java} ${attribute.attribute_name});

</#list>

}