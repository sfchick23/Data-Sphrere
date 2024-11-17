package ru.sfchick.Data_Sphere.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin")
public class AdminController {

    @GetMapping("/panel")
    public String adminPanel() {
        return "admin/panel";
    }
}
