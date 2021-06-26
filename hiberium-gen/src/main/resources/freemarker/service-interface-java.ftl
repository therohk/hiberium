package ${package_base}.service.${module_name};

import ${package_base}.models.${module_name}.${concept_name};

import java.util.List;

public interface ${concept_name}Service {

    void handle${concept_name}InsertOrUpdate(${concept_name} ${concept_varname});

    default void handle${concept_name}InsertOrUpdate(List<${concept_name}> ${concept_varname}List) {
        for(${concept_name} ${concept_varname} : ${concept_varname}List)
            handle${concept_name}InsertOrUpdate(${concept_varname})
    }

}