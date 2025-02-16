package com.example.samplebatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class MetaDBConfig {

  @Primary  // 메타 데이터 DB 설정
  @Bean
  @ConfigurationProperties(prefix = "spring.datasource-meta")
  // 메타 데이터 DB 연결을 위한 메서드
  public DataSource metaDBSource() {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean
  public PlatformTransactionManager metaTransactionManager() {
    return new DataSourceTransactionManager(metaDBSource());
  }

}
