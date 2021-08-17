package ${package_base}.service.impl.${module_name};

import ${package_base}.jdbc.repository.${module_name}.${concept_name}Repository;
import ${package_base}.models.${module_name}.${concept_name};
import ${package_base}.service.${module_name}.${concept_name}Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ${concept_name}ServiceImpl implements ${concept_name}Service {

    @Autowired
    private ${concept_name}Repository ${concept_varname}Repository;

    @Override
    public ${concept_name} findByPrimaryKey(Integer primaryKey) {
        Optional<${concept_name}> ${concept_varname}Match = ${concept_varname}Repository.findById(primaryKey);
        if(${concept_varname}Match.isEmpty())
            throw new NoSuchElementException("missing data for ${concept_name} WHERE id="+primaryKey);
        return ${concept_varname}Match.get();
    }

    @Override
    public Integer handle${concept_name}InsertOrUpdate(${concept_name} ${concept_varname}) {
        if(${concept_varname} == null)
            return -1;
        Integer ${concept_varname}Id = ${concept_varname}.primaryKey();
        ${concept_name} ${concept_varname}Existing = null;
        if(${concept_varname}Id != null)
            ${concept_varname}Existing = ${concept_varname}Repository.getOne(${concept_varname}Id);

        //insert vs update
        if(${concept_varname}Existing == null) {
            ${concept_varname}.handleFieldsForInsert();
            ${concept_varname}Existing = ${concept_varname}Repository.save(${concept_varname});
            log.info("INSERT ${concept_name} WHERE id={}", ${concept_varname}Existing.primaryKey());
        } else {
            ${concept_varname}Existing.handleFieldsForUpdate(${concept_varname});
            ${concept_varname}Repository.save(${concept_varname}Existing);
            log.info("UPDATE ${concept_name} WHERE id={}", ${concept_varname}Existing.primaryKey());
        }
        return ${concept_varname}Existing.primaryKey();
    }

    @Override
    public void handle${concept_name}InsertOrUpdate(List<${concept_name}> ${concept_varname}List) {
        if(${concept_varname}List == null || ${concept_varname}List.isEmpty())
            return;

        List<Integer> ${concept_varname}IdList = ${concept_varname}List.stream()
            .map(c -> c.primaryKey())
            .collect(Collectors.toList());
        List<${concept_name}> ${concept_varname}ExistingList = new ArrayList<>();
        if(!${concept_varname}IdList.isEmpty())
            ${concept_varname}ExistingList = ${concept_varname}Repository.findAllById(${concept_varname}IdList);

        //Map<Integer,String> responseMap = new HashMap<Integer,String>();
        List<${concept_name}> ${concept_varname}InsertList = new ArrayList<>();
        List<${concept_name}> ${concept_varname}UpdateList = new ArrayList<>();

        //choose insert or update
        for(${concept_name} ${concept_varname} : ${concept_varname}List) {
             Optional<${concept_name}> ${concept_varname}Match = ${concept_varname}ExistingList.stream()
                 .filter(p -> p.primaryKey().equals(${concept_varname}.primaryKey()))
                 .findFirst();

             if(${concept_varname}.primaryKey() == null || !${concept_varname}Match.isPresent()) {
                 ${concept_varname}.handleFieldsForInsert();
                 ${concept_varname}InsertList.add(${concept_varname});
             } else {
                 ${concept_name} ${concept_varname}Existing = ${concept_varname}Match.get();
                 ${concept_varname}Existing.handleFieldsForUpdate(${concept_varname});
                 ${concept_varname}UpdateList.add(${concept_varname}Existing);
             }
        }

        if(!${concept_varname}InsertList.isEmpty()) {
            ${concept_varname}Repository.saveAll(${concept_varname}InsertList);
            log.info("INSERT ${concept_name} for total={}", ${concept_varname}InsertList.size());
        }

        if(!${concept_varname}UpdateList.isEmpty()) {
            ${concept_varname}Repository.saveAll(${concept_varname}UpdateList);
            log.info("UPDATE ${concept_name} for total {}", ${concept_varname}UpdateList.size());
        }
    }

}