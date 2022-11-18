package com.example.petbutler.persist.batchScheduling;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

  private final JobLauncher jobLauncher;

  private final AnimalHosptlBatchConfig animalHosptlBatchConfig;

  // 매년 1월 1일 오전 2시에 신규 동물 병원 데이터 추가
  // TODO : 문을 닫은 동물 병원은 따로 삭제
  @Scheduled(cron = "0 0 2 1 1 ?")
  public void runJob(){

    // Job Parameter
    Map<String, JobParameter> parameterMap = new HashMap();
    parameterMap.put("time", new JobParameter(System.currentTimeMillis()));
    JobParameters jobParameters = new JobParameters(parameterMap);

    // run 안에 BatchConfig에서 설정한 job, Job Parameter(반복 Job의 ID 역할) 추가
    try {
      jobLauncher.run(animalHosptlBatchConfig.createAnimalHosptlJob(), jobParameters);
    } catch (JobExecutionException e){
      log.error(e.getMessage());
    }

  }

}
