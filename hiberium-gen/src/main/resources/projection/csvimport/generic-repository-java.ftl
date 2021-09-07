package ${package_base}.jdbc.repository;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

@Service
public class GenericRepository {

    @Autowired
    private ListableBeanFactory listableBeanFactory;

    public JpaRepository getRepositoryInstance(Class<?> entityClass) {
        Repositories repositories = new Repositories(listableBeanFactory);
        return (JpaRepository) repositories.getRepositoryFor(entityClass).get();
    }

}