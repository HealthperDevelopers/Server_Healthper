package umc.healthper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HealthperApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthperApplication.class, args);

	}

}
