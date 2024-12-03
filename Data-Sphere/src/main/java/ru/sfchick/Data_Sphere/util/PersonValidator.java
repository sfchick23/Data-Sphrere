package ru.sfchick.Data_Sphere.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.service.PeopleService;

@Component
public class PersonValidator implements Validator{
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        // Проверка на существование имени пользователя
        if (peopleService.findByUsername(person.getUsername()).isPresent()) {
            errors.rejectValue("username", null, "Username already exists");
        }

        // Проверка на существование email
        if (peopleService.findByEmail(person.getEmail()).isPresent()) {
            errors.rejectValue("email", null, "Email already exists");
        }
    }
}
