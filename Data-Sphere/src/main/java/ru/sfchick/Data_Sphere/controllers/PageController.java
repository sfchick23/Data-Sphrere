package ru.sfchick.Data_Sphere.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.sfchick.Data_Sphere.config.SecurityConfig;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.service.PeopleService;

@Controller
public class PageController {

    private final SecurityConfig securityConfig;
    private final PeopleService peopleService;

    @Autowired
    public PageController(SecurityConfig securityConfig, PeopleService peopleService) {
        this.securityConfig = securityConfig;
        this.peopleService = peopleService;
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    public void handleFavicon() {
    }

    @GetMapping()
    public String index(Model model) {
        Person person = peopleService.findByUsername(securityConfig.getLoggedInUsername()).orElse(null);
        model.addAttribute("username", securityConfig.getLoggedInUsername());
        model.addAttribute("person", person);
        return "index";
    }

}
