package ${package_base}.models.${module_name};

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

<#if concept_desc??>
/**
 * does not follow stored object model
 * desc : ${concept_desc}
 */
</#if>
@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "${concept_table}", schema = "${concept_schema}")
public class ${concept_name} implements Serializable {

<#list attributes as attribute>
    <#if attribute.attribute_role?contains("K")>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    @Column(name = "${attribute.field_name}" <@printcoldef attribute=attribute/>)
    private ${attribute.attribute_java} ${attribute.attribute_name};
</#list>

}

<#macro printcoldef attribute>
<@compress single_line=true>
<#if attribute.field_type == "numeric">
, columnDefinition = "${attribute.field_type}(${attribute.field_scale!15},${attribute.field_precision!4})"
</#if>
</@compress>
</#macro>