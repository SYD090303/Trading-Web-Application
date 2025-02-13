package com.project.TradingWebApp.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender mailSender;

    public void sendVerificationEmail(String emailAddress, String verificationCode) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
        String subject = "🎉 Welcome to Trading Web App! Your Verification Code is Here! 🚀";
        String content = "🌟 Dear User! 🌟\n\n"
                + "🎯 Your exclusive verification code: *" + verificationCode + "* 🔥\n\n"
                + "🛡 Secure your account and gain full access by entering this code.\n\n"
                + "🚀 Start your trading journey today and make the most of our powerful platform!\n\n"
                + "💬 Need assistance? Our support team is just a message away! 💡\n\n"
                + "✨ Stay Awesome! ✨\n\n"
                + "Best Regards,\n💼 The Trading Web App Team";
        mimeMessageHelper.setTo(emailAddress);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content);

        try{
            mailSender.send(mimeMessage);
        }
        catch (Exception me){
            throw new MessagingException("Failed to send verification email", me);
        }
    }
}
