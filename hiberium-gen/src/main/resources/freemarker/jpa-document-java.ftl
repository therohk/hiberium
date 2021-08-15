package ${package_base}.models.elastic.${module_name};

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(indexName = "${concept_index}")
public class ${concept_name} {

    @Id
    private String id;
<#list attributes as attribute>
    @Field(name = "${attribute.field_name}", type = ${attribute.elastic_doctype})
    private ${attribute.attribute_java} ${attribute.attribute_name};
</#list>

}