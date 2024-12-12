package ru.sfchick.Data_Sphere.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sfchick.Data_Sphere.config.SecurityConfig;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.security.PersonDetails;
import ru.sfchick.Data_Sphere.service.EmailService;
import ru.sfchick.Data_Sphere.service.PeopleService;
import ru.sfchick.Data_Sphere.service.PostService;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

@Controller
public class PeopleController {

    private final PeopleService peopleService;
    private final PostService postService;
    private final SecurityConfig securityConfig;
    private final EmailService emailService;

    @Autowired
    public PeopleController(PeopleService peopleService, PostService postService, SecurityConfig securityConfig, EmailService emailService) {
        this.peopleService = peopleService;
        this.postService = postService;
        this.securityConfig = securityConfig;
        this.emailService = emailService;
    }


    @GetMapping("/{username}")
    public String getUserProfile(@PathVariable("username") String username, Model model) {
        Person person = peopleService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        Person personLogged =  peopleService.findByUsername(securityConfig.getLoggedInUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        model.addAttribute("person", person);
        model.addAttribute("personLogged", personLogged);
        model.addAttribute("posts", postService.getPublicPostsByAuthor(username));
        return "people/profile";
    }

    @PostMapping("/{username}/send-verification")
    public String sendVerificationCode(@PathVariable("username") String username) {
        Person person = peopleService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (!person.isVerified()) {
            // Генерация кода подтверждения
            String verificationCode = generateVerificationCode();

            peopleService.savePendingRegistration(person, verificationCode);

            // Отправка email с кодом подтверждения
            emailService.sendEmail(
                    person.getEmail(),
                    "Подтверждение аккаунта",
                    "Ваш код подтверждения: " + verificationCode
            );
        }

        return "redirect:/auth/verify";
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000; // Генерирует число от 100000 до 999999
        return String.valueOf(code);
    }

    @DeleteMapping("/{username}")
    public String deleteUser(@PathVariable("username") String username, HttpSession httpSession) throws IOException {
        Person person = peopleService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        peopleService.delete(person);

        httpSession.invalidate();

        return "redirect:/";
    }
}
