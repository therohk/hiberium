package ${package_base}.models.${module_name};

import ${package_base}.models.StoredObject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
<#if dynamic_insert>@DynamicInsert</#if>
<#if dynamic_update>@DynamicUpdate</#if>
@Table(name = "${concept_table}", schema = "${concept_schema}")
public class ${concept_name} implements StoredObject<${concept_name}>, Serializable {

<#list attributes as attribute>
    <#if attribute.attribute_flag?contains("K")>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    @Column(name = "${attribute.field_name}")
    private ${attribute.attribute_java} ${attribute.attribute_name};
</#list>

<#assign primary_key = attributes?filter(a -> a.attribute_flag?contains("K"))?first>
    public ${primary_key.attribute_java} primaryKey() {
        return get${primary_key.attribute_name?cap_first}();
    }

    public void primaryKey(${primary_key.attribute_java} primaryKey) {
        set${primary_key.attribute_name?cap_first}(primaryKey);
    }

    public void handleFieldsForInsert() { }

    public void handleFieldsForDelete() { }

    public void handleFieldsForUpdate(${concept_name} update) {
<#list attributes as attribute>
    <#if attribute.attribute_flag?contains("K")><#continue></#if>
    <#if attribute.attribute_flag?contains("F")><#continue></#if>
        this.set${attribute.attribute_name?cap_first}(update.get${attribute.attribute_name?cap_first}());
</#list>
    }

}