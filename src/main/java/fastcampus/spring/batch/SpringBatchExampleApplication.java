package fastcampus.spring.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchExampleApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(SpringBatchExampleApplication.class, args)));
	}

	@Bean
	@Primary // 스프링부트에서 TaskExecutor 빈이 기본생성됨. 우리는 아래 작성한 빈을 기본으로 사용하겠다는 의미
	TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(10);
		taskExecutor.setMaxPoolSize(20);
		taskExecutor.setThreadNamePrefix("batch-thread-");
		taskExecutor.initialize();
		return taskExecutor;
	}

}
