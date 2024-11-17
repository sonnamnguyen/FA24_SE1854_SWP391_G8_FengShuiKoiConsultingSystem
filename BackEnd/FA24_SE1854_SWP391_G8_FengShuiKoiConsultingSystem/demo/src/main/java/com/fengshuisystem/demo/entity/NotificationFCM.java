package com.fengshuisystem.demo.entity;

import lombok.Data;

@Data
public class NotificationFCM {
    String title;
    String message;
    String fcmToken;

}
