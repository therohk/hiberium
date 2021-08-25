package ${package_base}.service.spec.${module_name};

import ${package_base}.models.${module_name}.${concept_name};

import java.util.List;

@Deprecated //use implementation only
public interface ${concept_name}ServiceSpec {

    ${concept_name} findByPrimaryKey(Integer primaryKey);

    List<${concept_name}> searchByExample(${concept_name} ${concept_varname}Sample,
                                          Integer pageNum, Integer perPage,
                                          List<String> sortFields, boolean ascending)

    /**
     * handle any operation like insert, update or soft delete
     * reads record from database if present and merges source data into it
     */
    Integer handle${concept_name}InsertOrUpdate(${concept_name} ${concept_varname}Source, String strategy);

    /**
     * naive approach to multiple updates
     * implementation should support bulk calls with no more than three queries
     */
    default void handle${concept_name}InsertOrUpdate(List<${concept_name}> ${concept_varname}SourceList, String strategy) {
        for(${concept_name} ${concept_varname} : ${concept_varname}List)
            handle${concept_name}InsertOrUpdate(${concept_varname}, strategy);
    }

}