package com.srvcode.model1project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Model1projectApplication implements CommandLineRunner {

	@Autowired
	PaymentService paymentServiceObj;

	public static void main(String[] args) {
		SpringApplication.run(Model1projectApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		paymentServiceObj.pay();

	}
}
