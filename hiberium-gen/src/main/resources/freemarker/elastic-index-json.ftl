{
    "mappings": {
        "properties": {
<#list attributes as attribute>
<@compress single_line=true>
            "${attribute.attribute_name}" : {
                "type": "${attribute.elastic_type}"
    <#if attribute.elastic_type == "text">, "analyzer": "english"</#if>
    <#if attribute.elastic_type == "date">, "format": "basic_date_time"</#if>
    <#if attribute.attribute_role?contains("H")>, "index" : false</#if>
             } <#sep>,</#sep>
</@compress>

</#list>
        }
    }
}