package com.srvcode.model1project;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class PaymentService {
    public void pay(){
        System.out.println("paying.....");
    }

    @PostConstruct    //Immediately after all beans are initialized
    public void afterInitialize(){
        System.out.println("Before payment");
    }

    @PreDestroy      //Just before all the beans are destroyed
    public void beforeDestroy(){
        System.out.println("After payment is done");
    }
}
