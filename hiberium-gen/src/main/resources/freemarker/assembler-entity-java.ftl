package ${package_base}.assembler.${module_name};

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import ${package_base}.controller.${module_name}.${concept_name}Controller;
import ${package_base}.models.${module_name}.${concept_name};

@Component
class ${concept_name}Assembler implements RepresentationModelAssembler<${concept_name}, EntityModel<${concept_name}>> {

    @Override
    public EntityModel<${concept_name}> toModel(${concept_name} ${concept_varname}) {

      return EntityModel.of(${concept_varname},
          linkTo(methodOn(${concept_name}Controller.class).get${concept_name}ById(${concept_varname}.getId())).withSelfRel(),
          linkTo(methodOn(${concept_name}Controller.class).getAll${concept_name}()).withRel("${concept_varname}List")
          );
    }
}