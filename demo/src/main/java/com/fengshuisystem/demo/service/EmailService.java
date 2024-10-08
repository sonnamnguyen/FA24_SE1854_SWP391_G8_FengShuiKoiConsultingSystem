package com.fengshuisystem.demo.service;

public interface EmailService {
    public void sendEmail(String from, String to, String subject, String body);
}