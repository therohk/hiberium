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
//@Service
public class ${concept_name}ServiceImpl implements ${concept_name}Service {

    @Override
    public Integer createOrUpdate${concept_name}(${concept_name} ${concept_varname}) {
        if(${concept_varname} == null)
            return -1;
        Integer ${concept_varname}Id = ${concept_varname}.getProductSplitId();
        ProductSplit ${concept_varname}Existing = null;
        if(${concept_varname}Id != null)
            ${concept_varname}Existing = ${concept_varname}Repository.getOne(${concept_varname}Id);

        //create vs update
        if(${concept_varname}Existing == null) {
            ${concept_varname}.handleFieldsForCreate(authenticationFacade.getUserId());
            productSplitExisting = productSplitRepository.save(productSplit);
        } else {
            ${concept_varname}Existing.handleFieldsForUpdate(${concept_varname});
            ${concept_varname}Repository.save(${concept_varname}Existing);
        }
        return ${concept_varname}Existing.getProductSplitId();
    }

}