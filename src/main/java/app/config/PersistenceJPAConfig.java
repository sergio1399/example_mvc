package app.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.SystemException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



@Configuration
@ComponentScan(basePackages = { "app.components" })
@EnableTransactionManagement
public class PersistenceJPAConfig {

    /*@Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("app.components");
        factory.setDataSource(dataSource());
        factory.setJpaProperties(additionalProperties());
        factory.afterPropertiesSet();

        return factory.getObject();
    }*/


    @Bean
    public EntityManagerFactory entityManagerFactory() throws SQLException {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("app.components");
        factory.setDataSource(xaDataSource());
        //factory.setJtaDataSource(xaDataSource());
        factory.setJpaPropertyMap(jpaMapProperties());
       // factory.setJpaProperties(additionalProperties());
        factory.afterPropertiesSet();

        return factory.getObject();
    }

    @Bean
    public AtomikosDataSourceBean xaDataSource(){
        AtomikosDataSourceBean ds = new AtomikosDataSourceBean();
        ds.setUniqueResourceName("xads");
        ds.setXaDataSourceClassName("org.postgresql.xa.PGXADataSource");
        Properties p = new Properties();
        p.setProperty ( "serverName" , "localhost" );
        p.setProperty ( "portNumber" , "5432" );
        p.setProperty("databaseName", "weather");
        p.setProperty("user", "sa");
        p.setProperty("password", "sa");
        ds.setXaProperties(p);
        ds.setPoolSize(15);
        return ds;
    }


    public UserTransactionManager userTransactionManager() throws SystemException {
       UserTransactionManager userTransactionManager = new UserTransactionManager();
       userTransactionManager.setForceShutdown(false);
       userTransactionManager.init();
       return userTransactionManager;
    }


    public UserTransactionImp userTransactionImp() throws SystemException {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(300);
        return userTransactionImp;
    }

    @Bean
    public JtaTransactionManager transactionManager() throws SystemException {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        jtaTransactionManager.setUserTransaction(userTransactionImp());
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }


/*

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/weather");
        dataSource.setUsername( "sa" );
        dataSource.setPassword( "sa" );
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(
            EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);

        return transactionManager;
    }
*/
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }




  /*  Properties additionalProperties() {
        Properties properties = new Properties();
        //properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        return properties;
    }
*/

    Map<String, Object> jpaMapProperties(){
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.transactionType", "JTA");
        properties.put("hibernate.current_session_context_class", "jta");
        properties.put("hibernate.transaction.manager_lookup_class", "com.atomikos.icatch.jta.hibernate4.TransactionManagerLookup");
        properties.put("hibernate.connection.autocommit", "false");
        return properties;
    }



    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }
}
