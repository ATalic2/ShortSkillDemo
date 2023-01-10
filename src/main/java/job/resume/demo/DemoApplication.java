package job.resume.demo;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(scanBasePackages = "job.resume.demo", exclude = HibernateJpaAutoConfiguration.class)
public class DemoApplication {
	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		LOGGER.info("Starting up demo application");
		SpringApplication.run(DemoApplication.class, args);
	}
}