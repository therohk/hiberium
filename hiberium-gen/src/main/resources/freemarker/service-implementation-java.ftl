package ${package_base}.service.${module_name};

import ${package_base}.jdbc.repository.${module_name}.${concept_name}Repository;
import ${package_base}.models.${module_name}.${concept_name};

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ${concept_name}Service {

    @Autowired
    private ${concept_name}Repository ${concept_varname}Repository;

    public ${concept_name} findByPrimaryKey(Integer primaryKey) {
        Optional<${concept_name}> ${concept_varname}Match = ${concept_varname}Repository.findById(primaryKey);
        if(${concept_varname}Match.isEmpty())
            throw new NoSuchElementException("missing data for ${concept_name} WHERE id="+primaryKey);
        return ${concept_varname}Match.get();
    }

    public List<${concept_name}> searchByExample(${concept_name} ${concept_varname}Sample, Integer pageNum, Integer perPage,
                                               List<String> sortFields, boolean ascending) {
        if(${concept_varname}Sample == null)
            ${concept_varname}Sample = new ${concept_name}();
        Example<${concept_name}> ${concept_varname}Example = Example.of(${concept_varname}Sample);
        Pageable pageable;
        if(sortFields != null && !sortFields.isEmpty()) {
            Sort.Direction direction = ascending ? Sort.Direction.ASC : Sort.Direction.DESC;
            String[] propArray = sortFields.toArray(new String[sortFields.size()]);
            pageable = PageRequest.of(pageNum-1, perPage, direction, propArray);
        } else {
            pageable = PageRequest.of(pageNum-1, perPage);
        }
        Page<${concept_name}> ${concept_varname}Page = ${concept_varname}Repository.findAll(${concept_varname}Example, pageable);
        log.info("SELECT ${concept_name} WHERE page={} size={} ; found={}", pageNum, perPage, ${concept_varname}Page.getNumberOfElements());
        return ${concept_varname}Page.getContent();
    }

    public Integer handle${concept_name}InsertOrUpdate(${concept_name} ${concept_varname}Source, String strategy) {
        if(${concept_varname}Source == null || "N".equals(strategy))
            return -1;

        Integer ${concept_varname}Id = ${concept_varname}Source.primaryKey();
        ${concept_name} ${concept_varname}Target = null;
        if(${concept_varname}Id != null && !"C".equals(strategy))
            ${concept_varname}Target = ${concept_varname}Repository.getOne(${concept_varname}Id);

        //insert vs update
        if(${concept_varname}Target == null) {
            ${concept_varname}Source.handleFieldsForInsert();
            ${concept_varname}Target = ${concept_varname}Repository.save(${concept_varname}Source);
            ${concept_varname}Id = ${concept_varname}Target.primaryKey();
            log.info("INSERT ${concept_name} WHERE id={}", ${concept_varname}Id);
        } else {
            ${concept_varname}Target.handleFieldsForUpdate(${concept_varname}Source, strategy);
            ${concept_varname}Repository.save(${concept_varname}Target);
            ${concept_varname}Id = ${concept_varname}Target.primaryKey();
            log.info("UPDATE ${concept_name} WHERE id={}", ${concept_varname}Id);
        }
        return ${concept_varname}Id;
    }

    public void handle${concept_name}InsertOrUpdate(List<${concept_name}> ${concept_varname}SourceList) {
        if(${concept_varname}SourceList == null || ${concept_varname}SourceList.isEmpty())
            return;

        List<Integer> ${concept_varname}IdList = ${concept_varname}SourceList.stream()
            .filter(c -> c.primaryKey() != null)
            .map(c -> c.primaryKey())
            .collect(Collectors.toList());
        List<${concept_name}> ${concept_varname}TargetList = new ArrayList<${concept_name}>();
        if(!${concept_varname}IdList.isEmpty())
            ${concept_varname}TargetList = ${concept_varname}Repository.findAllById(${concept_varname}IdList);

        //Map<Integer,String> responseMap = new HashMap<Integer,String>();
        List<${concept_name}> ${concept_varname}InsertList = new ArrayList<${concept_name}>();
        List<${concept_name}> ${concept_varname}UpdateList = new ArrayList<${concept_name}>();

        //choose insert or update
        for(${concept_name} ${concept_varname}Source : ${concept_varname}SourceList) {
             Optional<${concept_name}> ${concept_varname}Match = ${concept_varname}TargetList.stream()
                 .filter(p -> p.primaryKey().equals(${concept_varname}Source.primaryKey()))
                 .findFirst();

             if(${concept_varname}Source.primaryKey() == null || !${concept_varname}Match.isPresent()) {
                 ${concept_varname}Source.handleFieldsForInsert();
                 ${concept_varname}InsertList.add(${concept_varname}Source);
             } else {
                 ${concept_name} ${concept_varname}Target = ${concept_varname}Match.get();
                 ${concept_varname}Target.handleFieldsForUpdate(${concept_varname}Source);
                 ${concept_varname}UpdateList.add(${concept_varname}Target);
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