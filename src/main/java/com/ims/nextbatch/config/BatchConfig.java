package com.ims.nextbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

	@Bean
	public Job jobStart(JobRepository jobRepository, Step stepStart) {
		return new JobBuilder("jobStart", jobRepository).start(stepStart).build();

	}

	@Bean
	public Step stepStart(JobRepository jobRepository, PlatformTransactionManager transactionaManager) {
		return new StepBuilder("stepStart", jobRepository)
				.tasklet((StepContribution contribution, ChunkContext chunkContext) -> {
					System.out.println("===== Job initialized =====");
					return RepeatStatus.FINISHED;

				}, transactionaManager).build();
	}

}
