package ${package_base}.models.${module_name};

import ${package_base}.models.StoredObject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

<#if concept_desc??>
/**
 * does not follow stored object model
 * desc ${concept_symbol} : ${concept_desc}
 */
</#if>
@Getter
@Setter
@Entity
<#if dynamic_insert>@DynamicInsert</#if><#if dynamic_update>@DynamicUpdate</#if>
@Table(name = "${concept_table}", schema = "${concept_schema}")
public class ${concept_name} implements StoredObject<${concept_name}>, Serializable {

<#list attributes as attribute>
    <#if attribute.attribute_role?contains("K")>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    @Column(name = "${attribute.field_name}" <@printcoldef attribute=attribute/>)
    private ${attribute.attribute_java} ${attribute.attribute_name};
</#list>

    public ${concept_name}() { }

    public ${concept_name} fetchEntity() {
        return this;
    }

<#assign primary_key = attributes?filter(a -> a.attribute_role?contains("K"))?first>
    public ${primary_key.attribute_java} primaryKey() {
        return get${primary_key.attribute_name?cap_first}();
    }

    public void primaryKey(${primary_key.attribute_java} ${primary_key.attribute_name}) {
        set${primary_key.attribute_name?cap_first}(${primary_key.attribute_name});
    }

    public void handleFieldsForInsert() {
        //set${primary_key.attribute_name?cap_first}(null);
        //activeInd + createTs + updateTs
    }

    public void handleFieldsForDelete() {
        //activeInd + updateTs
     }

    public void handleFieldsForUpdate(${concept_name} source) {
        if(source == null)
            return;
<#list attributes as attribute>
    <#if attribute.attribute_role?contains("K")><#continue></#if>
    <#if attribute.attribute_role?contains("I")><#continue></#if>
        this.set${attribute.attribute_name?cap_first}(<@printsetval attribute=attribute/>);
</#list>
        //updateTs
    }

    public void handleFieldsForMerge(${concept_name} source, String strategy) {
        if(source == null || "N".equals(strategy))
            return;
<#list attributes as attribute>
        handleFieldForMerge(this, source, "${attribute.attribute_name}", <@printstrtgy attribute=attribute/>);
</#list>
        //updateTs
    }

    public String toString() {
        return invokeToString();
    }

}

<#macro printcoldef attribute>
<@compress single_line=true>
<#if attribute.field_type == "numeric">
, columnDefinition = "${attribute.field_type}(${attribute.field_precision!22},${attribute.field_scale!4})"
</#if>
</@compress>
</#macro>

<#macro printsetval attribute>
<@compress single_line=true>
<#if attribute.setter_value??>${attribute.setter_value}
<#else>source.get${attribute.attribute_name?cap_first}()
</#if>
</@compress>
</#macro>

<#macro printstrtgy attribute>
<@compress single_line=true>
<#if attribute.attribute_role?contains("K")>"N"
<#elseif attribute.attribute_role?contains("I")>"I"
<#elseif attribute.update_code??>"${attribute.update_code}"
<#elseif attribute.attribute_role?contains("N")>"B"
<#elseif update_code??>"${update_code}"
<#else>strategy
</#if>
</@compress>
</#macro>