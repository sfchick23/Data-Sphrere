package ru.sfchick.Data_Sphere.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.sfchick.Data_Sphere.model.Verification;
import ru.sfchick.Data_Sphere.repositories.VerificationCodeRepository;

import java.time.LocalDateTime;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final VerificationCodeRepository verificationCodeRepository;

    public EmailService(JavaMailSender mailSender, VerificationCodeRepository verificationCodeRepository) {
        this.mailSender = mailSender;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    public void sendEmail(String to, String subject, String confirmationCode) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8"); // Указываем кодировку

            // Формируем тело письма
            String emailContent = String.format(
                    "Здравствуйте!<br><br>Ваш код подтверждения: <strong>%s</strong><br><br>Спасибо, что выбрали наш сервис!",
                    confirmationCode
            );

            // Устанавливаем параметры письма
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(emailContent, true); // true для HTML контента

            // Отправляем письмо
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Ошибка отправки письма: " + e.getMessage(), e);
        }
    }

    public boolean isVerificationCodeValid(String code) {
        Verification verification = verificationCodeRepository.findByCode(code).orElse(null);
        if (verification != null) {
            LocalDateTime currentTime = LocalDateTime.now();
            return currentTime.isBefore(verification.getExpirationTime());
        }
        return false;
    }
}
