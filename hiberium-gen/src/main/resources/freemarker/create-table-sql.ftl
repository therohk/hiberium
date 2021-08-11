
-- INCOMPLETE

-- DROP TABLE ${concept_schema}.${concept_table}

CREATE TABLE ${concept_schema}.${concept_table} (
<#list attributes as attribute>
    ${attribute.field_name} ${attribute.field_type} <#sep>,</#sep>
</#list>
);