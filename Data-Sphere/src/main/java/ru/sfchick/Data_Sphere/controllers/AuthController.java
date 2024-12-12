package ru.sfchick.Data_Sphere.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.sfchick.Data_Sphere.config.SecurityConfig;
import ru.sfchick.Data_Sphere.dto.PersonDTO;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.model.Verification;
import ru.sfchick.Data_Sphere.repositories.VerificationCodeRepository;
import ru.sfchick.Data_Sphere.security.PersonDetails;
import ru.sfchick.Data_Sphere.service.EmailService;
import ru.sfchick.Data_Sphere.service.PeopleService;
import ru.sfchick.Data_Sphere.util.PersonValidator;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final ModelMapper modelMapper;
    private final SecurityConfig securityConfig;

    @Autowired
    public AuthController(PeopleService peopleService, PersonValidator personValidator, EmailService emailService, VerificationCodeRepository verificationCodeRepository, ModelMapper modelMapper, SecurityConfig securityConfig) {
        this.peopleService = peopleService;
        this.personValidator = personValidator;
        this.emailService = emailService;
        this.verificationCodeRepository = verificationCodeRepository;
        this.modelMapper = modelMapper;
        this.securityConfig = securityConfig;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("person", new PersonDTO());
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("person") PersonDTO personDTO) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid PersonDTO personDTO, BindingResult bindingResult) {
        Person person = convertToPerson(personDTO);

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "auth/login";

        person.setVerified(false);
        peopleService.register(person);

        return "redirect:/auth/login";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam(value = "message", required = false) String message, Model model) {
        String username = securityConfig.getLoggedInUsername();
        Person person = peopleService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        Person personLogged =  peopleService.findByUsername(securityConfig.getLoggedInUsername())
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        if (message != null) {
            model.addAttribute("error", message);
        }

        model.addAttribute("person", person);
        model.addAttribute("personLogged", personLogged);
        return "auth/verify";
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        boolean isValid = emailService.isVerificationCodeValid(code);
        Optional<Verification> verification = verificationCodeRepository.findByCode(code);

        if (verification.isPresent() && isValid) {
            String email = verification.get().getEmail();
            Optional<Person> personOptional = peopleService.findByEmail(email);

            if (personOptional.isPresent()) {
                System.out.println(personOptional.get());
                Person person = personOptional.get();
                person.setVerified(true);
                peopleService.save(person);
                PersonDetails updatedPersonDetails = new PersonDetails(person);
                updateAuthentication(updatedPersonDetails);

                return "redirect:/" + person.getUsername();
            } else {
                redirectAttributes.addAttribute("message", "User associated with the code not found.");
                return "redirect:/auth/verify";
            }
        } else {
            redirectAttributes.addAttribute("message", "Invalid or expired verification code.");
            return "redirect:/auth/verify";
        }
    }


    private Person convertToPerson(@Valid PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private void updateAuthentication(PersonDetails updatedPersonDetails) {
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                updatedPersonDetails,
                currentAuth.getCredentials(),
                updatedPersonDetails.getAuthorities()
        );

        // Обновляем SecurityContext
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

}
