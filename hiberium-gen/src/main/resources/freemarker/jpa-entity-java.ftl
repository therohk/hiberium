package ${package_base}.models.${module_name};

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

<#if concept_desc??>
/**
 * Table - ${concept_desc}
 */
</#if>
@Getter
@Setter
@Entity
@DynamicInsert
@Table(name = "${concept_table}", schema = "${concept_schema}")
public class ${concept_name} implements Serializable {

<#list attributes as attribute>
    <#if attribute.attribute_flag?contains("K")>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    </#if>
    @Column(name = "${attribute.field_name}")
    private ${attribute.attribute_java} ${attribute.attribute_name};
</#list>

}