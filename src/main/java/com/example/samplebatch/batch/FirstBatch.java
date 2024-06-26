package com.example.samplebatch.batch;

import com.example.samplebatch.entity.AfterEntity;
import com.example.samplebatch.entity.BeforeEntity;
import com.example.samplebatch.repository.AfterRepository;
import com.example.samplebatch.repository.BeforeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class FirstBatch {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager platformTransactionManager;

  private final BeforeRepository beforeRepository;
  private final AfterRepository afterRepository;


  public FirstBatch(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager,
                    BeforeRepository beforeRepository, AfterRepository afterRepository) {
    this.jobRepository = jobRepository;
    this.platformTransactionManager = platformTransactionManager;
    this.beforeRepository = beforeRepository;
    this.afterRepository = afterRepository;
  }

  // Job 정의
  @Bean
  public Job firstJob() {

    return new JobBuilder("firstJob", jobRepository)
            .start(firstStep())
            .build();
  }

  // Step 정의
  @Bean
  public Step firstStep() {

    return new StepBuilder("firstStep", jobRepository)
            .<BeforeEntity, AfterEntity> chunk(10, platformTransactionManager)  // chunk 단위로 데이터 처리
            .reader(beforeReader())
            .processor(middleProcessor())
            .writer(afterWriter())
            .build();
  }

  // Reader 정의 (JPA 기반)
  // BeforeEntity 테이블에서 읽어오는 Reader
  @Bean
  public RepositoryItemReader<BeforeEntity> beforeReader() {

    return new RepositoryItemReaderBuilder<BeforeEntity>()
            .name("beforeReader")
            .pageSize(10)
            .methodName("findAll")
            .repository(beforeRepository)
            .sorts(Map.of("id", Sort.Direction.ASC))
            .build();
  }

  // Processor 정의
  // 읽어온 데이터를 처리하는 Process
  @Bean
  public ItemProcessor<BeforeEntity, AfterEntity> middleProcessor() {

    return new ItemProcessor<BeforeEntity, AfterEntity>() {

      @Override
      public AfterEntity process(BeforeEntity item) throws Exception {

        AfterEntity afterEntity = new AfterEntity();
        afterEntity.setUsername(item.getUsername());

        return afterEntity;
      }
    };
  }

  // Writer 정의 (JPA 기반)
  // AfterEntity에 처리한 결과를 저장하는 Writer
  @Bean
  public RepositoryItemWriter<AfterEntity> afterWriter() {

    return new RepositoryItemWriterBuilder<AfterEntity>()
            .repository(afterRepository)
            .methodName("save")
            .build();
  }
}
