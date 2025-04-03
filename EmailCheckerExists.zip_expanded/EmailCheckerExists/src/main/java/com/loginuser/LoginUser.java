package com.loginuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class LoginUser {

	public static void main(String[] args) {
		SpringApplication.run(LoginUser.class, args);
	}

}
