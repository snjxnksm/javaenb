package jp.co.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jp.co.example.common.AppRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = AppRepositoryImpl.class)
public class engscheduleApplication {

	public static void main(String[] args) {
		SpringApplication.run(engscheduleApplication.class, args);
	}
}
