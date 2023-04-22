package com.ims.nextbatch.step;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StepLogConfig {

    @Bean
    public Step stepStart(Tasklet registerLogTask, JobRepository repository, PlatformTransactionManager transactionaManager) {
        return new StepBuilder("stepStart", repository)
            .tasklet(registerLogTask, transactionaManager)
            .build();
    }

}
