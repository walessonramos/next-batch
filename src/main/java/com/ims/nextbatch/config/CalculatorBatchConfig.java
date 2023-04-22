package com.ims.nextbatch.config;

import java.util.Arrays;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class CalculatorBatchConfig {

    @Bean
    public Job basicCalculatorJob(JobRepository repository, Step stepSum) {
        return new JobBuilder("jobSum", repository)
            .start(stepSum)
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean
    public Step stepSum(JobRepository jobRepository, PlatformTransactionManager transactionaManager) {
        return new StepBuilder("stepSum", jobRepository)
            .<Integer, String>chunk(1, transactionaManager) //Leitura(Integer)-Escrita(String) e tamanho chunck(1)
            .reader(inputReader(null))
            .processor(sumProcessor())
            .writer(printResultSumWriter())
            .build();
    }

    private IteratorItemReader<Integer> inputReader(@Value("#{jobParameters['value1Sum']}") Integer value1) {
        return new IteratorItemReader<Integer>(Arrays.asList(value1).iterator());
    }

    private FunctionItemProcessor<Integer, String> sumProcessor() {
        return new FunctionItemProcessor<Integer, String>(value -> {
            System.out.println(String.format("O resultado da soma de %s + %s é: %s", value, value, value + value));
            return String.valueOf(value+value);
        });
    }

    private ItemWriter<String> printResultSumWriter() {
        System.out.printf("=== Resultado ===");
        return result -> result.forEach(System.out::println);
    }

}
