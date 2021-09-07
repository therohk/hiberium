package ${package_base}.models.${module_name};

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "${concept_table}", schema = "${concept_schema}")
public class ${concept_name}Composite implements Serializable {

<#assign primary_key = attributes?filter(a -> a.attribute_role?contains("K"))?first>
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "${primary_key.field_name}")
    private ${primary_key.attribute_java} ${primary_key.attribute_name};

    @ManyToOne
    @JoinColumn(name="${primary_key.field_name}", referencedColumnName="${primary_key.field_name}", insertable=false, updatable=false)
    private ${concept_name} ${concept_varname};

<#list relations as attribute>
<#if attribute.attribute_role?contains("U")>
    @ManyToOne
    @JoinColumn(name="${primary_key.field_name}", referencedColumnName="${attribute.field_name}", insertable=false, updatable=false)
    private ${attribute.concept_name} ${attribute.concept_name?uncap_first};
<#else>
    @OneToMany
    @JoinColumn(name="${attribute.field_name}", referencedColumnName="${primary_key.field_name}", insertable=false, updatable=false)
    private List<${attribute.concept_name}> ${attribute.concept_name?uncap_first}List;
</#if>

</#list>

    public ${primary_key.attribute_java} compositeKey() {
        return get${primary_key.attribute_name?cap_first}();
    }

    public void compositeKey(${primary_key.attribute_java} ${primary_key.attribute_name}) {
        set${primary_key.attribute_name?cap_first}(${primary_key.attribute_name});
    }

}