package ${package_base}.models.elastic.${module_name};

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ${concept_name}Document {

    @Id
    @Field(name = "id", type = FieldType.Keyword)
    private String id;

<#list attributes as attribute>
    @Field(name = "${attribute.field_name}", type = FieldType.${attribute.elastic_type?cap_first!Auto})
    private ${attribute.attribute_java} ${attribute.attribute_name};
</#list>

    //convert between document and entity
}