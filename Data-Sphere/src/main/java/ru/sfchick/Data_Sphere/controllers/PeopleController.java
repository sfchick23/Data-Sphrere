package ru.sfchick.Data_Sphere.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.service.PeopleService;

import java.io.IOException;
import java.util.Optional;

@Controller
public class PeopleController {

    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }


    @GetMapping("/{username}")
    public String getUserProfile(@PathVariable("username") String username, Model model) {
        Person person = peopleService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        model.addAttribute("person", person);
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
