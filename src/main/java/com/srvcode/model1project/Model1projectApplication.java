package com.srvcode.model1project;

import com.srvcode.model1project.impl.EmailNotificationService;
import com.srvcode.model1project.impl.SmsNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Model1projectApplication implements CommandLineRunner {

	//@Autowired
	final private NotificationService notificationServiceobj;

	public Model1projectApplication(NotificationService notificationServiceobj) {
		this.notificationServiceobj = notificationServiceobj;
	}

	public static void main(String[] args) {
		SpringApplication.run(Model1projectApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		notificationServiceobj.send("hello");


	}
}
