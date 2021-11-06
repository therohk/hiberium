
-- Postgres Dialect
-- ${concept_schema}.${concept_table} -> ${module_name}.${concept_name}
-- Generated at ${.now}

-- DROP TABLE IF EXISTS ${concept_schema}.${concept_table};

CREATE SCHEMA IF NOT EXISTS ${concept_schema};

CREATE TABLE ${concept_schema}.${concept_table} (
<#list attributes as attribute>
    ${attribute.field_name} <@printtype attribute=attribute/> <@printnull attribute=attribute/> <@printdefault attribute=attribute/> <#sep>,</#sep>
</#list>
<#list attributes?filter(a -> a.attribute_role?contains("K")) as attribute>
    , CONSTRAINT ${attribute.field_name}_pk PRIMARY KEY (${attribute.field_name})
</#list>
<#list attributes?filter(a -> a.attribute_role?contains("U")) as attribute>
    , CONSTRAINT ${attribute.field_name}_uk UNIQUE (${attribute.field_name})
</#list>
<#list attributes?filter(a -> a.attribute_role?contains("F")) as attribute>
    , CONSTRAINT ${attribute.field_name}_fk FOREIGN KEY (${attribute.field_name}) REFERENCES ${concept_schema}.${attribute.foreign_key_table}(${attribute.foreign_key_field})
</#list>
);

<#macro printtype attribute>
<@compress single_line=true>
<#if attribute.field_type == "varchar">${attribute.field_type}(${attribute.field_scale!128})
<#elseif attribute.field_type == "numeric">${attribute.field_type}(${attribute.field_precision!22},${attribute.field_scale!4})
<#else>${attribute.field_type}
</#if>
</@compress>
</#macro>

<#macro printnull attribute>
<@compress single_line=true>
<#assign notnull_roles = ['N', 'K']>
<#if notnull_roles?seq_contains(attribute.attribute_role)>NOT</#if> NULL
</@compress>
</#macro>

<#macro printdefault attribute>
<@compress single_line=true>
<#if attribute.default_value??>
DEFAULT <#if attribute.attribute_java == "String">'${attribute.default_value}'<#else>${attribute.default_value}</#if>
</#if>
</@compress>
</#macro>