package com.example.encurtadordeurl;

import org.springframework.boot.SpringApplication;

public class TestEncurtadorDeUrlApplication {

	public static void main(String[] args) {
		SpringApplication.from(EncurtadorDeUrlApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
