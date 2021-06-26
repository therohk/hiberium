package ${base_package}.service.impl.${module_name};

import ${base_package}.jdbc.repository.${module_name}.${concept_name}Repository;
import ${base_package}.models.catalog.${module_name}.${concept_name};
import ${base_package}.service.${module_name}.${concept_name}Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ${concept_name}ServiceImpl implements ${concept_name}Service {

    @Autowired
    private ${concept_name}Repository ${concept_varname}Repository;

    @Override
    public Integer handle${concept_name}InsertOrUpdate(${concept_name} ${concept_varname}) {
        if(${concept_varname} == null)
            return -1;
        Integer ${concept_varname}Id = ${concept_varname}.getPrimaryKey();
        ${concept_name} ${concept_varname}Existing = null;
        if(${concept_varname}Id != null)
            ${concept_varname}Existing = ${concept_varname}Repository.getOne(${concept_varname}Id);

        //create vs update
        if(${concept_varname}Existing == null) {
            ${concept_varname}.handleFieldsForInsert();
            ${concept_varname}Existing = ${concept_varname}Repository.save(${concept_varname});
        } else {
            ${concept_varname}Existing.handleFieldsForUpdate(${concept_varname});
            ${concept_varname}Repository.save(${concept_varname}Existing);
        }
        return ${concept_varname}Existing.getPrimaryKey();
    }

}