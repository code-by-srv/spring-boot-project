package com.srvcode.model1project.impl;

import com.srvcode.model1project.NotificationService;
import org.springframework.stereotype.Component;

@Component
public class SmsNotificationService implements NotificationService {
    @Override
    public void send(String message) {
        System.out.println("Sms sending..."+message);
    }
}
