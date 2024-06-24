package com.example.samplebatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cglib.core.Local;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.samplebatch.repository",  // DataDBConfig 가 동작할 패키지
        entityManagerFactoryRef = "dataEntityManager",
        transactionManagerRef = "dataTransactionManager"
)
public class DataDBConfig {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource-data")
  // 데이터 소스 DB 연결을 위한 메서드
  public DataSource dataDBSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  // 엔티티들을 관리할 매니저
  public LocalContainerEntityManagerFactoryBean dataEntityManager() {

    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

    em.setDataSource(dataDBSource());
    em.setPackagesToScan(new String[]{"com.example.samplebatch.entity"});  // 엔티티들이 모여있는 패키지 등록
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    HashMap<String, Object> properties = new HashMap<>();
    properties.put("hibernate.hbm2ddl.auto", "update");
    properties.put("hibernate.show_sql", true);
    em.setJpaPropertyMap(properties);

    return em;
  }

  @Bean
  public PlatformTransactionManager dataTransactionManager() {

    JpaTransactionManager transactionManager = new JpaTransactionManager();

    transactionManager.setEntityManagerFactory(dataEntityManager().getObject());

    return transactionManager;
  }
}
