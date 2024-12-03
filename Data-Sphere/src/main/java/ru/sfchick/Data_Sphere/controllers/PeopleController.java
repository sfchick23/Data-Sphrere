package ru.sfchick.Data_Sphere.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sfchick.Data_Sphere.config.SecurityConfig;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.service.PeopleService;
import ru.sfchick.Data_Sphere.service.PostService;

import java.io.IOException;

@Controller
public class PeopleController {

    private final PeopleService peopleService;
    private final PostService postService;
    private final SecurityConfig securityConfig;

    @Autowired
    public PeopleController(PeopleService peopleService, PostService postService, SecurityConfig securityConfig) {
        this.peopleService = peopleService;
        this.postService = postService;
        this.securityConfig = securityConfig;
    }


    @GetMapping("/{username}")
    public String getUserProfile(@PathVariable("username") String username, Model model) {
        Person person = peopleService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        Person personLogged =  peopleService.findByUsername(securityConfig.getLoggedInUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        model.addAttribute("person", person);
        model.addAttribute("personLogged", personLogged);
        model.addAttribute("posts", postService.getPostsByAuthor(username));
        return "people/profile";
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
