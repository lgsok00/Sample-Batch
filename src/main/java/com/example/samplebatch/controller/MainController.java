package com.example.samplebatch.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// API 기반 배치 실행
@Controller
@ResponseBody
public class MainController {

  private final JobLauncher jobLauncher;
  private final JobRegistry jobRegistry;

  public MainController(JobLauncher jobLauncher, JobRegistry jobRegistry) {
    this.jobLauncher = jobLauncher;
    this.jobRegistry = jobRegistry;
  }

  @GetMapping("/first")
  public String firstApi(@RequestParam("value") String value) throws Exception {

    JobParameters jobParameters = new JobParametersBuilder()
            .addString("date", value)
            .toJobParameters();

    jobLauncher.run(jobRegistry.getJob("firstJob"), jobParameters);

    return "ok";
  }
}
