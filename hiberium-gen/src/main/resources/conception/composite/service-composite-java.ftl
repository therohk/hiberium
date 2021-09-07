package ${package_base}.service.${module_name};

import ${package_base}.jdbc.repository.${module_name}.${concept_name}CompositeRepository;
import ${package_base}.models.${module_name}.${concept_name}Composite;
import ${package_base}.models.${module_name}.${concept_name};
<#list relations as attribute>
import ${package_base}.models.${module_name}.${attribute.concept_name};
</#list>
import ${package_base}.service.${module_name}.${concept_name}Service;
<#list relations as attribute>
import ${package_base}.service.${module_name}.${attribute.concept_name}Service;
</#list>

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * transform between flat/relational and nested/composite models
 * bulk operations not allowed due to n+1 query issue
 * unique key on the attribute determines cardinality
 */
@Slf4j
@Service
public class ${concept_name}CompositeService {

    @Autowired
    private ${concept_name}CompositeRepository ${concept_varname}CompositeRepository;

    @Autowired
    private ${concept_name}Service ${concept_name?uncap_first}Service;
    <#list relations as attribute>
    @Autowired
    private ${attribute.concept_name}Service ${attribute.concept_name?uncap_first}Service;
    </#list>

    /**
     * relational to nested structure using multi select
     * requires 2+r queries where r is the number of relations
     */
    public ${concept_name}Composite findByCompositeKey(Integer primaryKey) {
        Optional<${concept_name}Composite> ${concept_varname}Match = ${concept_varname}CompositeRepository.findById(primaryKey);
        if(${concept_varname}Match.isEmpty())
            throw new NoSuchElementException("missing data for ${concept_name} composite WHERE id="+primaryKey);
        log.info("SELECT ${concept_name}Composite WHERE id={}", primaryKey);
        return ${concept_varname}Match.get();
    }

    /**
     * nested to relational structure
     * primary key is unset if secondary key does not match
     * requires at most 1+3r queries
     */
    @Transactional
    public Integer handleCompositeInsertOrUpdate(${concept_name}Composite compositeRequest, String strategy) {
        if(compositeRequest == null || "N".equals(strategy))
            return -1;

        ${concept_name} ${concept_varname} = compositeRequest.get${concept_name}();
        Integer ${concept_varname}Id = ${concept_varname}Service.handle${concept_name}InsertOrUpdate(${concept_varname}, strategy);
        if(${concept_varname}Id == -1)
            throw new IllegalArgumentException("composite base entity cannot be null");

<#list relations as attribute>
<#assign attribute_varname = attribute.concept_name?uncap_first>
<#if attribute.attribute_role?contains("U")>
        ${attribute.concept_name} ${attribute_varname} = compositeRequest.get${attribute.concept_name}();
        if(${attribute_varname} != null) {
            if(!${concept_varname}Id.equals(${attribute_varname}.get${attribute.attribute_name?cap_first}()))
                ${attribute_varname}.primaryKey(null);
            ${attribute_varname}.set${attribute.attribute_name?cap_first}(${concept_varname}Id);
            ${attribute_varname}Service.handle${attribute.concept_name}InsertOrUpdate(${attribute_varname}, strategy);
        }
<#else>
        List<${attribute.concept_name}> ${attribute_varname}List = compositeRequest.get${attribute.concept_name}List();
        if(!CollectionUtils.isEmpty(${attribute_varname}List)) {
            ${attribute_varname}List.stream()
                    .filter(t -> !${concept_varname}Id.equals(t.get${attribute.attribute_name?cap_first}()))
                    .forEach(t -> t.primaryKey(null));
            ${attribute_varname}List.forEach(t -> t.set${attribute.attribute_name?cap_first}(${concept_varname}Id));
            ${attribute_varname}Service.handle${attribute.concept_name}InsertOrUpdate(${attribute_varname}List, strategy);
        }
</#if>

</#list>
        return ${concept_varname}Id;
    }

}