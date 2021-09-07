package ${package_base}.controller.${module_name};

import ${package_base}.jdbc.repository.${module_name}.${concept_name}CompositeRepository;
import ${package_base}.models.${module_name}.${concept_name}Composite;
import ${package_base}.service.${module_name}.${concept_name}CompositeService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.NoSuchElementException;

@Slf4j
@RestController
public class ${concept_name}CompositeController {

    @Autowired
    private ${concept_name}CompositeRepository repository;
    @Autowired
    private ${concept_name}CompositeService service;

    @RequestMapping(value = "/${concept_apipath}-composite/{id}", method = RequestMethod.GET)
    public ${concept_name}Composite get${concept_name}CompositeById(
            @PathVariable(value = "id") Integer ${concept_varname}Id
            ) throws Exception {

        ${concept_name}Composite ${concept_varname}Composite = repository.getOne(${concept_varname}Id);
        if (${concept_varname}Composite == null)
            throw new NoSuchElementException("${concept_name} Id " + ${concept_varname}Id + " not found");
        log.info("SELECT ${concept_name} where id={}", ${concept_varname}Id);

        //${concept_name}Composite ${concept_varname}Composite = service.findByCompositeKey(${concept_varname}Id);
        return ${concept_varname}Composite;
    }

    @RequestMapping(value = "/${concept_apipath}-composite", method = RequestMethod.PUT)
    public ${concept_name}Composite insertOrUpdate${concept_name}Composite(
            @Valid @RequestBody ${concept_name}Composite compositeRequest,
            @RequestParam(value = "strategy", defaultValue = "B") String strategy
            ) throws Exception {

        Integer ${concept_varname}Id = service.handleCompositeInsertOrUpdate(compositeRequest, strategy);
        return service.findByCompositeKey(${concept_varname}Id);
    }

}