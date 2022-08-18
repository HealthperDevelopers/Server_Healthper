package umc.healthper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import umc.healthper.repository.CompleteExerciseRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;

@SpringBootApplication
@EnableJpaAuditing
public class HealthperApplication {


	public static void main(String[] args) {
		SpringApplication.run(HealthperApplication.class, args);

	}

}
