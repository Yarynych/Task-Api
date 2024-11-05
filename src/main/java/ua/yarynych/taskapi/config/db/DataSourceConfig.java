package ua.yarynych.taskapi.config.db;

import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for setting up data sources and transaction management.
 * This class configures the primary H2 data source and the secondary PostgreSQL
 * data source, as well as a routing data source to switch between them.
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);


    /**
     * Configures the primary H2 data source.
     *
     * @return the configured H2 DataSource.
     */
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryH2DataSource() {
        return DataSourceBuilder.create().build();
    }


    /**
     * Configures the secondary PostgreSQL data source.
     *
     * @return the configured PostgreSQL DataSource.
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryPostgreDataSource() {
        return DataSourceBuilder.create().build();
    }


    /**
     * Configures the custom routing data source that determines which data source
     * to use at runtime based on availability.
     *
     * @return the configured CustomRoutingDataSource.
     */
    @Bean
    public DataSource dataSource() {
        logger.info("Configuring CustomRoutingDataSource with target data sources.");

        CustomRoutingDataSource customRoutingDataSource = new CustomRoutingDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();

        targetDataSources.put("H2", primaryH2DataSource());
        targetDataSources.put("PostgreSQL", secondaryPostgreDataSource());

        customRoutingDataSource.setTargetDataSources(targetDataSources);
        customRoutingDataSource.setDefaultTargetDataSource(primaryH2DataSource());

        logger.info("CustomRoutingDataSource configured with H2 as default.");
        return customRoutingDataSource;
    }


    /**
     * Configures the entity manager factory for the primary data source.
     *
     * @param builder the EntityManagerFactoryBuilder.
     * @return the configured LocalContainerEntityManagerFactoryBean.
     */
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        logger.info("Creating EntityManagerFactory for primary data source.");
        return builder
                .dataSource(dataSource()) // Використовувати RoutingDataSource
                .packages("ua.yarynych.taskapi.entity")
                .persistenceUnit("primary")
                .build();
    }


    /**
     * Configures the transaction manager for the primary data source.
     *
     * @param primaryEntityManagerFactory the EntityManagerFactory to use for transactions.
     * @return the configured PlatformTransactionManager.
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager primaryTransactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {
        logger.info("Creating transaction manager for primary data source.");
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }
}
