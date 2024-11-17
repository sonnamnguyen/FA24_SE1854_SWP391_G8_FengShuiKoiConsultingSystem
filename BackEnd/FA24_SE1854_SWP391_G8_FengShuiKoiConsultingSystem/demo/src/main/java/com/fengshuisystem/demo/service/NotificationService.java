package com.fengshuisystem.demo.service;

import com.fengshuisystem.demo.entity.Account;
import com.fengshuisystem.demo.entity.NotificationFCM;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    public FirebaseMessaging firebaseMessaging;

    public NotificationService (FirebaseApp firebaseApp) {
        this.firebaseMessaging = FirebaseMessaging.getInstance(firebaseApp);
    }

    public void sendNotificationToAccount(NotificationFCM notificationFCM, Account account) {
        Notification notification = Notification.builder()
                .setTitle(notificationFCM.getTitle())
                .setBody(notificationFCM.getMessage())
                .build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(account.getFcmToken())   // định danh gửi tới ai
                .build();

        try {
            firebaseMessaging.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
