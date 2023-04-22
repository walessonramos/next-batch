package com.ims.nextbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    public Job jobStart(JobRepository jobRepository, Step stepStart) {
        return new JobBuilder("jobStart", jobRepository)
            .start(stepStart)
            .incrementer(new RunIdIncrementer())
            .build();

    }

    @Bean
    public Step stepStart(JobRepository jobRepository, PlatformTransactionManager transactionaManager) {
        return new StepBuilder("stepStart", jobRepository)
            .tasklet(printLogTasklet(transactionaManager, null), transactionaManager)
            .build();
    }

    //Além de outras razões, esta anotação @StepScope permite que o méttodo esteja no escopo de step para capturar parametros de execução.
    @Bean
    @StepScope
    public Tasklet printLogTasklet(PlatformTransactionManager transactionaManager, @Value("#{jobParameters['nome']}") String nome) {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("===== Job initialized =====");
                System.out.println(String.format("Executed by %s: ", nome));
                return RepeatStatus.FINISHED;
            }
        };
    }

}
