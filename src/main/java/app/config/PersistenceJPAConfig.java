package app.config;

import app.components.service.SimpleMessageListener;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
//import com.atomikos.icatch.jta.
import com.atomikos.jms.AtomikosConnectionFactoryBean;
import org.apache.activemq.spring.ActiveMQXAConnectionFactory;
import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;
import org.hibernate.engine.transaction.jta.platform.spi.JtaPlatform;
import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;

import javax.jms.ConnectionFactory;
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

    @Bean
    @DependsOn("springJtaPlatformAdapter")
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQL95Dialect");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("app.components");
        factory.setJpaDialect(new HibernateJpaDialect());
        factory.setDataSource(xaDataSource());
        factory.setJpaPropertyMap(jpaMapProperties());

        Properties p = new Properties();
        p.setProperty("hibernate.hbm2ddl.auto", "update");
        p.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL95Dialect");
        factory.setJpaProperties(p);


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

    @Bean
    public UserTransactionManager userTransactionManager() throws SystemException {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        userTransactionManager.init();
        return userTransactionManager;
    }

    @Bean
    public UserTransactionImp userTransactionImp() throws SystemException {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(1000);
        return userTransactionImp;
    }

    @Bean
    public JtaTransactionManager transactionManager() throws SystemException {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        jtaTransactionManager.setUserTransaction(userTransactionImp());
        //jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Map<String, Object> jpaMapProperties(){
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.transactionType", "JTA");
        properties.put("hibernate.current_session_context_class", "jta");
        properties.put("hibernate.transaction.jta.platform", "app.config.SpringJtaPlatformAdapter");
        properties.put("hibernate.connection.autocommit", "true");
        return properties;
    }

    @Bean
    SpringJtaPlatformAdapter springJtaPlatformAdapter() throws SystemException {
        SpringJtaPlatformAdapter springJtaPlatformAdapter = new SpringJtaPlatformAdapter();
        springJtaPlatformAdapter.setJtaTransactionManager(transactionManager());
        return springJtaPlatformAdapter;
    }


    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }


}
