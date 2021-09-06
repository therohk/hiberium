package ${package_base}.repository.elastic.${module_name};

import ${package_base}.models.elastic.${module_name}.${concept_name}Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ${concept_name}DocumentRepository extends ElasticsearchRepository<${concept_name}Document, String> { }