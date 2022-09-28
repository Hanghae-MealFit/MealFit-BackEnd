package com.mealfit.config.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Profile("stresstest")
@Configuration
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(MasterSlaveDataSourceProperties.class)
public class MasterSlaveDataSourceConfiguration {

    private final MasterSlaveDataSourceProperties dbProperties;
    private final JpaProperties jpaProperties;

    public MasterSlaveDataSourceConfiguration(MasterSlaveDataSourceProperties dbProperties,
          JpaProperties jpaProperties) {
        this.dbProperties = dbProperties;
        this.jpaProperties = jpaProperties;
    }

    // Setting DataSource (Master, Slave1, Slave2)
    @Bean
    public DataSource routingDataSource() {
        DataSource master = createDataSource(
              dbProperties.getUrl(),
              dbProperties.getUsername(),
              dbProperties.getPassword()
        );

        Map<Object, Object> dataSources = new HashMap<>();
        dataSources.put("master", master);
        dbProperties.getSlave().forEach((key, value) ->
              dataSources.put(value.getName(), createDataSource(
                    value.getUrl(), value.getUsername(), value.getPassword()
              ))
        );

        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();
        replicationRoutingDataSource.setDefaultTargetDataSource(dataSources.get("master"));
        replicationRoutingDataSource.setTargetDataSources(dataSources);

        log.info("datasources: {}", dataSources.toString());

        return replicationRoutingDataSource;
    }

    private DataSource createDataSource(String url, String username, String password) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setMaximumPoolSize(20);

        return new HikariDataSource(hikariConfig);
    }

    // JPA Setting
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        EntityManagerFactoryBuilder emBuilder =
              createEntityManagerFactoryBuilder(jpaProperties);

        return emBuilder.dataSource(dataSource())
              .packages("com.mealfit")
              .build();
    }

    // DB Vender Adapter
    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(vendorAdapter, jpaProperties.getProperties(), null);
    }

    // Datasource가 매번 변경이 되기때문에 Proxy Datasource 연결.
    private DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    // TxManager 설정
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }
}
