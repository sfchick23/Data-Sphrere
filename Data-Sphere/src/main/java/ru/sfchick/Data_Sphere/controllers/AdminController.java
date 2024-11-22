package ru.sfchick.Data_Sphere.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sfchick.Data_Sphere.dto.PersonDTO;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.repositories.PeopleRepository;
import ru.sfchick.Data_Sphere.service.PeopleService;
import ru.sfchick.Data_Sphere.service.PostService;

import java.awt.*;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController {

    private final PeopleService peopleService;
    private final PostService postService;
    private final PeopleRepository peopleRepository;

    public AdminController(PeopleService peopleService, PostService postService, PeopleRepository peopleRepository) {
        this.peopleService = peopleService;
        this.postService = postService;
        this.peopleRepository = peopleRepository;
    }


    @GetMapping("/panel")
    public String adminPanel(Model model) {
        model.addAttribute("posts", postService.getPosts());
        model.addAttribute("people", peopleService.findAll());
        return "admin/panel";
    }

    @GetMapping("/roles/{id}")
    public String manageRoles(@PathVariable("id") int id, Model model) {
        Person person = peopleService.findById(id);
        model.addAttribute("person", person); // Передаем объект person
        model.addAttribute("roles", new String[]{"ROLE_USER", "ROLE_ADMIN"}); // Список ролей
        return "admin/roles";
    }


    @PatchMapping("/{id}/assignRole")
    public String assignRole(@PathVariable("id") int id,
                             @RequestParam("role") String role) {
        peopleService.updateRole(id, role);
        return "redirect:/admin/panel";
    }


    @DeleteMapping("/deletePosts/{id}")
    public String deletePost(@PathVariable("id") int id) {
        postService.deletePost(id);
        return "redirect:/admin/panel";
    }

    @DeleteMapping("/deletePerson/{id}")
    public String deletePerson(@PathVariable("id") int id) throws IOException {
        Person person = peopleService.findById(id);
        peopleService.delete(person);
        return "redirect:/admin/panel";
    }
}
