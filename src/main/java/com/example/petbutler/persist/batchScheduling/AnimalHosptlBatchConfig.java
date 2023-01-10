package com.example.petbutler.persist.batchScheduling;

import com.example.petbutler.model.AnimalHosptlApiForm;
import com.example.petbutler.persist.entity.AnimalHosptl;
import com.example.petbutler.utils.HospitalApiProvider;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AnimalHosptlBatchConfig {

  private final JobBuilderFactory jobBuilderFactory;

  private final StepBuilderFactory stepBuilderFactory;

  private final EntityManagerFactory entityManagerFactory;

  private final HospitalApiProvider hospitalApiProvider;

  private static final int CHUNK_SIZE = 1000;

  @Bean
  public Job createAnimalHosptlJob() {
    return jobBuilderFactory.get("createAnimalHosptlJob")
        .preventRestart()
        .start(createAnimalHosptlStep())
        .build();
  }

  @Bean
  @JobScope
  public Step createAnimalHosptlStep() {
    return stepBuilderFactory.get("createAnimalHosptlStep")
        .<AnimalHosptlApiForm, AnimalHosptl>chunk(CHUNK_SIZE)
        .reader(animalHosptlReader())
        .processor(animalHosptlProcessor())
        .writer(animalHosptlWriter())
        .build();
  }

  @Bean
  @StepScope
  public ListItemReader<AnimalHosptlApiForm> animalHosptlReader(){

    long totalCount = hospitalApiProvider.getTotalCount();
    int  pTotalCnt  = (int) (totalCount / CHUNK_SIZE) + 1;

    List<AnimalHosptlApiForm> list = new ArrayList();

    for (int i = 1; i <= pTotalCnt; i++) {

      list.addAll(hospitalApiProvider.getResultList(i, CHUNK_SIZE));

    }

    return new ListItemReader(list);
  }

  @Bean
  @StepScope
  public ItemProcessor<AnimalHosptlApiForm, AnimalHosptl> animalHosptlProcessor(){
    return item -> item.toEntity();
  }

  @Bean
  @StepScope
  public JpaItemWriter<AnimalHosptl> animalHosptlWriter(){
    JpaItemWriter<AnimalHosptl> writer = new JpaItemWriter();
    writer.setEntityManagerFactory(entityManagerFactory);
    return writer;
  }

}
