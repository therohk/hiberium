package ${package_base}.jdbc.repository.${module_name};

import ${package_base}.models.${module_name}.${concept_name};
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ${concept_name}Repository extends JpaRepository<${concept_name}, Integer> {

<#assign primary_key = attributes?filter(a -> a.attribute_role?contains("K"))?first>
    default ${concept_name} getOne(Integer ${primary_key.attribute_name}) {
        return findById(${primary_key.attribute_name}).orElse(null);
    }

    @Query(value = " SELECT MAX(${primary_key.field_name}) FROM ${concept_schema}.${concept_table} "
                , nativeQuery = true)
    Integer getMaxPrimaryKey();

    @Query(value = " SELECT * FROM ${concept_schema}.${concept_table} " +
            " LIMIT ?1 OFFSET ?2 "
            , nativeQuery = true)
    List<${concept_name}> findAllByLimitAndOffset(Integer limit, Integer offset);

<#list attributes as attribute>
    <#if !attribute.attribute_role?contains("R")><#continue></#if>
    <@printfindby attribute=attribute/><#nt>

</#list>
<#list attributes as attribute>
<#assign allow_types = ['Integer', 'Long', 'Double', 'Date']>
<#if !allow_types?seq_contains(attribute.attribute_java)><#continue></#if>
    <@printfindbtw attribute=attribute/><#nt>

</#list>
}

<#macro printfindby attribute>
<@compress single_line=true>
<#if attribute.attribute_role?contains("U")>${concept_name} <#else>List<${concept_name}> </#if>
findBy${attribute.attribute_name?cap_first}(
${attribute.attribute_java} ${attribute.attribute_name} );
</@compress>
</#macro>

<#macro printfindbtw attribute>
<@compress single_line=true>
List<${concept_name}> findBy${attribute.attribute_name?cap_first}Between(
${attribute.attribute_java} ${attribute.attribute_name}Lower,
${attribute.attribute_java} ${attribute.attribute_name}Upper );
</@compress>
</#macro>