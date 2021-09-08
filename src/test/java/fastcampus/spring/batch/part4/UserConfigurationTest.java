package fastcampus.spring.batch.part4;

import fastcampus.spring.batch.TestConfiguration;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@SpringBatchTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = { UserConfiguration.class, TestConfiguration.class }) // 테스트 대상
public class UserConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("test")
    @Test
    void test() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        System.out.println("userRepository.findById(1L) : " + userRepository.findById(1L));
        System.out.println("userRepository.findAllByUpdatedDate(LocalDate.now()).size() : " + userRepository.findAllByUpdatedDate(LocalDate.now()).size());

        int size = userRepository.findAllByUpdatedDate(LocalDate.now()).size();

        Assertions.assertThat(jobExecution.getStepExecutions().stream()
                .filter(x -> x.getStepName().equals("userLevelUpStep")) // userLevelUpStep
                .mapToInt(StepExecution::getWriteCount)
                .sum())
                .isEqualTo(size)
                .isEqualTo(300);

        Assertions.assertThat(userRepository.count())
                .isEqualTo(400);
    }

}
