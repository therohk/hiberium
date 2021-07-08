package com.konivax.obsolete;

import com.konivax.utils.FileUtils;
import com.konivax.utils.format.FtlUtils;
import com.konivax.utils.Validate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderHibernateProject {

    public void renderJpaEntity(Map<String,Object> root, String projectPath) {
        String packagePath = FtlUtils.parseLocalTemplate(root, "src.main.java.${package_base}.models.${module_name}");
        String fileName = FtlUtils.parseLocalTemplate(root, "${concept_name}.java");

        flushNamedTemplate(root, "jpa-entity-java.ftl", projectPath, packagePath, fileName);
    }

    public void renderJpaRepository(Map<String,Object> root, String projectPath) {

        Validate.notNull(root.get("concept_name"), "concept not defined");

        String packagePath = FtlUtils.parseLocalTemplate(root, "src.main.java.${package_base}.jdbc.repository.${module_name}");
        String fileName = FtlUtils.parseLocalTemplate(root, "${concept_name}Repository.java");

        flushNamedTemplate(root, "spring-repository-java.ftl", projectPath, packagePath, fileName);
    }

    public void renderController(Map<String,Object> root, String projectPath) {

        Validate.notNull(root.get("concept_name"), "concept not defined");

        List<String> availFuncs = Arrays.asList("select", "insert", "update", "delete");

        Map<String,Object> render = new HashMap<String,Object>();
        render.put("render_controller_select_java", FtlUtils.parseNamedTemplate(root, "controller-select-java.ftl"));
        render.put("render_controller_insert_java", FtlUtils.parseNamedTemplate(root, "controller-insert-java.ftl"));
        render.put("render_controller_update_java", FtlUtils.parseNamedTemplate(root, "controller-update-java.ftl"));
        render.put("render_controller_delete_java", FtlUtils.parseNamedTemplate(root, "controller-delete-java.ftl"));

        render.putAll(root);

        String packagePath = FtlUtils.parseLocalTemplate(render, "src.main.java.${package_base}.controller.${module_name}");
        String fileName = FtlUtils.parseLocalTemplate(render, "${concept_name}Controller.java");

        flushNamedTemplate(render, "controller-shell-java.ftl", projectPath, packagePath, fileName);
    }

    public void renderServiceInterface(Map<String,Object> root, String projectPath) {
        String packagePath = FtlUtils.parseLocalTemplate(root, "src.main.java.${package_base}.service.${module_name}");
        String fileName = FtlUtils.parseLocalTemplate(root, "${concept_name}Service.java");

        flushNamedTemplate(root, "service-interface-java.ftl", projectPath, packagePath, fileName);
    }

    public void renderServiceImplementation(Map<String,Object> root, String projectPath) {
        String packagePath = FtlUtils.parseLocalTemplate(root, "src.main.java.${package_base}.service.impl.${module_name}");
        String fileName = FtlUtils.parseLocalTemplate(root, "${concept_name}RepositoryImpl.java");

        flushNamedTemplate(root, "service-implementation-java.ftl", projectPath, packagePath, fileName);
    }

    public void renderTableSql(Map<String,Object> root, String projectPath) {
        String packagePath = FtlUtils.parseLocalTemplate(root, "src.main.java.resources.database");
        String fileName = FtlUtils.parseLocalTemplate(root, "create-${concept_table}.sql");

        flushNamedTemplate(root, "create-table-sql.ftl", projectPath, packagePath, fileName);
    }

    public void renderElasticIndex(Map<String,Object> root, String projectPath) {
        String packagePath = FtlUtils.parseLocalTemplate(root, "src.main.java.resources.elastic");
        String fileName = FtlUtils.parseLocalTemplate(root, "index-${concept_index}.json");

        flushNamedTemplate(root, "elastic-index-json.ftl", projectPath, packagePath, fileName);
    }

    public static String flushNamedTemplate(final Map<String,Object> dataModel, String templateName,
                                          String basePath, String packagePath, String fileName) {

        String folderPath = FileUtils.getFilePath(basePath, packagePath);
        String classPath = FileUtils.getFilePath(basePath, packagePath, fileName);

        FileUtils.createFolder(folderPath, true);

        FtlUtils.flushNamedTemplate(dataModel, templateName, classPath);

        return classPath;
    }

}
