package ${package_base}.jdbc.repository.${module_name};

import org.springframework.data.jpa.repository.JpaRepository;
import ${package_base}.models.${module_name}.${concept_name};

public interface ${concept_name}Repository extends JpaRepository<${concept_name}, Integer> {

    default ${concept_name} getOne(Integer id) {
        return findById(id).orElse(null);
    }

}