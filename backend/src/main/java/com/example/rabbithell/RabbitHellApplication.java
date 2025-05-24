package com.example.rabbithell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class RabbitHellApplication {
	public static void main(String[] args) {

		// dotenv 환경변수 설정
		Dotenv dotenv = Dotenv.configure().directory("backend").ignoreIfMissing().load();

		// 모든 항목 시스템 속성으로 설정
		dotenv.entries().forEach(entry ->
			System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(RabbitHellApplication.class, args);
	}

}
