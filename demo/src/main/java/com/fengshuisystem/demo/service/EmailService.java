
package com.fengshuisystem.demo.service;

public interface EmailService {
    void sendEmail(String from, String to, String subject, String body);
}