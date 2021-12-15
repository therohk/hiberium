package ${package_base}.controller.${module_name};

import ${package_base}.jdbc.repository.${module_name}.${concept_name}Repository;
import ${package_base}.models.${module_name}.${concept_name};
import ${package_base}.service.${module_name}.${concept_name}Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class ${concept_name}Controller {

    @Autowired
    private ${concept_name}Repository repository;
    @Autowired
    private ${concept_name}Service service;

    <#if render_controller_select_java??>${render_controller_select_java}</#if>

    <#if render_controller_browse_java??>${render_controller_browse_java}</#if>

    <#if render_controller_insert_java??>${render_controller_insert_java}</#if>

    <#if render_controller_update_java??>${render_controller_update_java}</#if>

    <#if render_controller_delete_java??>${render_controller_delete_java}</#if>

}