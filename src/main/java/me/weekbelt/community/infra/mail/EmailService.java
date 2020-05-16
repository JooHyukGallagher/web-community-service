package me.weekbelt.community.infra.mail;

public interface EmailService {
    void sendEmail(EmailMessage emailMessage);
}
