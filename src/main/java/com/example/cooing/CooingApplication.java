package com.example.cooing;

import com.example.cooing.domain.auth.kakao.OAuthService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CooingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CooingApplication.class, args);
	}

}
