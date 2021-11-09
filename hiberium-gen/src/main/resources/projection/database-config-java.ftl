package ${package_base};

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DatabaseConfig {

    @Value("${r"$"}{spring.datasource.driverClassName}")
    private String dbDriver;
    @Value("${r"$"}{spring.datasource.url}")
    private String dbUrl;
    @Value("${r"$"}{spring.datasource.username}")
    private String dbUsername;
    @Value("${r"$"}{spring.datasource.password}")
    private String dbPassword;

    @Value("${r"$"}{spring.jpa.properties.hibernate.dialect}")
    private String hibernateDialect;
    @Value("${r"$"}{spring.jpa.show-sql}")
    private String hibernateShowSql;
    @Value("${r"$"}{spring.jpa.hibernate.hbm2ddl.auto:create-drop}")
    private String hibernateDdlAuto;

    @Value("${r"$"}{entity.scan.packages:${package_base}.models}")
    private String entityScanPath;

    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(dbDriver);
        dataSource.setJdbcUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

//    @Bean
//    @ConfigurationProperties("spring.datasource")
//    public DataSource dataSource() {
//        return DataSourceBuilder.create().build();
//    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource());
        entityManager.setPackagesToScan(entityScanPath);

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManager.setJpaVendorAdapter(vendorAdapter);

        Properties hibernateProps = new Properties();
        hibernateProps.setProperty("hibernate.dialect", hibernateDialect);
        hibernateProps.setProperty("hibernate.show_sql", hibernateShowSql);
        hibernateProps.setProperty("hibernate.ddl-auto", hibernateDdlAuto);
        entityManager.setJpaProperties(hibernateProps);
        return entityManager;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }

}