package ${package_base}.service.${module_name};

import ${package_base}.models.${module_name}.${concept_name};

import java.util.List;

public interface ${concept_name}Service {

    void handle${concept_name}CreateOrUpdate(${concept_name} ${concept_varname});

    void handle${concept_name}CreateOrUpdate(List<${concept_name}> ${concept_varname}List);

}