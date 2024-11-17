package ru.sfchick.Data_Sphere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.repositories.PeopleRepository;
import ru.sfchick.Data_Sphere.security.PersonDetails;


import java.util.Optional;

@Service
public class PersonDetailService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Trying to authenticate user: " + username); // Отладочный вывод
        Optional<Person> person = peopleRepository.findByUsername(username);

        if (person.isEmpty()) {
            System.out.println("User not found: " + username); // Отладочный вывод
            throw new UsernameNotFoundException("User not found");
        }

        System.out.println("User found: " + person.get().getUsername()); // Отладочный вывод
        return new PersonDetails(person.get());

    }
}
