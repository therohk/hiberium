package ${package_base}.controller.${module_name};

import ${package_base}.jdbc.repository.${module_name}.${concept_name}Repository;
import ${package_base}.models.${module_name}.${concept_name};

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class ${concept_name}Controller {

    @Autowired
    private ${concept_name}Repository repository;

    <#if render_controller_select_java??>${render_controller_select_java}</#if>

    <#if render_controller_insert_java??>${render_controller_insert_java}</#if>

    <#if render_controller_update_java??>${render_controller_update_java}</#if>

    <#if render_controller_delete_java??>${render_controller_delete_java}</#if>

}