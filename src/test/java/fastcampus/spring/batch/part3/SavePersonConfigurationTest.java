package fastcampus.spring.batch.part3;

import fastcampus.spring.batch.TestConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBatchTest
@ContextConfiguration(classes = {SavePersonConfiguration.class, TestConfiguration.class})
public class SavePersonConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PersonRepository personRepository;

    @AfterEach
    public void tearDown() throws Exception {
        personRepository.deleteAll();
    }

    @DisplayName("SavePersonStep")
    @Test
    void test_step() {
        JobExecution jobExecution = jobLauncherTestUtils.launchStep("SavePersonStep"); // SavePersonStep

        Assertions.assertThat(jobExecution.getStepExecutions().stream()
                        .mapToInt(StepExecution::getWriteCount)
                        .sum())
                .isEqualTo(personRepository.count())
                .isEqualTo(3);

    }


    @DisplayName("allow_duplicate")
    @Test
    void test_allow_duplicate() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allow_duplicate", "false")
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertThat(jobExecution.getStepExecutions().stream()
                    .mapToInt(StepExecution::getWriteCount)
                    .sum())
                .isEqualTo(personRepository.count())
                .isEqualTo(3);
    }

    @DisplayName("allow_not_duplicate")
    @Test
    void test_not_allow_duplicate() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("allow_duplicate", "true")
                .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        Assertions.assertThat(jobExecution.getStepExecutions().stream()
                    .mapToInt(StepExecution::getWriteCount)
                    .sum())
                .isEqualTo(personRepository.count())
                .isEqualTo(100);
    }


}
