package ru.sfchick.Data_Sphere.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import ru.sfchick.Data_Sphere.config.SecurityConfig;
import ru.sfchick.Data_Sphere.dto.PersonDTO;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.repositories.PeopleRepository;
import ru.sfchick.Data_Sphere.service.PeopleService;
import ru.sfchick.Data_Sphere.util.FileUploadUtil;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/settings")
public class settingController {

    private final PeopleService peopleService;
    private final SecurityConfig securityConfig;
    private final ModelMapper modelMapper;

    @Autowired
    public settingController(PeopleService peopleService, SecurityConfig securityConfig, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.securityConfig = securityConfig;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public String settings(Model model) {
        String usernameAuthPerson = securityConfig.getLoggedInUsername();
        Person person = peopleService.findByUsername(usernameAuthPerson).orElse(null);
        model.addAttribute("person", person);
        return "settings/setting";
    }

    @GetMapping("/edit")
    public String edit(Model model) {
        String usernameAuthPerson = securityConfig.getLoggedInUsername();
        Person person = peopleService.findByUsername(usernameAuthPerson).orElse(null);
        model.addAttribute("person", person);
        return "settings/edit";
    }

    @PostMapping("/edit")
    public RedirectView saveUser(@RequestParam("image") MultipartFile multipartFile) throws IOException {

        String usernameAuthPerson = securityConfig.getLoggedInUsername();
        Person person = peopleService.findByUsername(usernameAuthPerson).orElse(null);

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        person.setAvatar(fileName);

        System.out.println(person.getId());

        peopleService.updateAvatar(person);

        String uploadDir = "user-avatar/" + person.getId();

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        return new RedirectView("/settings", true);
    }


    private Person convertToPerson(@Valid PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

}
