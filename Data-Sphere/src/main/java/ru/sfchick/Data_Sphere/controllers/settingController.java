package ru.sfchick.Data_Sphere.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.sfchick.Data_Sphere.config.SecurityConfig;
import ru.sfchick.Data_Sphere.dto.PersonDTO;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.service.PeopleService;
import ru.sfchick.Data_Sphere.util.FileUploadUtil;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/settings")
public class settingController {

    private final PeopleService peopleService;
    private final SecurityConfig securityConfig;
    private final ModelMapper modelMapper;

    @Autowired
    public settingController(PeopleService peopleService, SecurityConfig securityConfig, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.peopleService = peopleService;
        this.securityConfig = securityConfig;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/profile")
    public String settings(Model model) {
        Person loggedInPerson = peopleService.findByUsername(securityConfig.getLoggedInUsername()).orElseThrow(() -> new RuntimeException("Logged in user not found"));
        model.addAttribute("person", loggedInPerson);
        return "settings/setting";
    }

    @GetMapping("/edit-avatar")
    public String edit(Model model) {
        String usernameAuthPerson = securityConfig.getLoggedInUsername();
        Person person = peopleService.findByUsername(usernameAuthPerson).orElseThrow(() -> new RuntimeException("Person not found"));

        model.addAttribute("person", person);

        return "settings/change-avatar";
    }

    @PostMapping("/edit-avatar")
    public RedirectView saveUser(@RequestParam("image") MultipartFile multipartFile) throws IOException {

        String usernameAuthPerson = securityConfig.getLoggedInUsername();
        Person person = peopleService.findByUsername(usernameAuthPerson).orElseThrow(() -> new RuntimeException("Person not found"));

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        person.setAvatar(fileName);

        System.out.println(person.getId());

        peopleService.updateAvatar(person);

        String uploadDir = "user-avatar/" + person.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return new RedirectView("/settings", true);
    }

    @PatchMapping("/profile")
    public String updateProfile(@ModelAttribute("person") @Valid Person person,
                                BindingResult bindingResult) {

        // Вызываем метод сервиса для обновления профиля
        peopleService.updateProfile(person);
        return "redirect:/settings/profile"; // Перенаправляем на страницу профиля
    }
    private Person convertToPerson(@Valid PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

}
