package ru.sfchick.Data_Sphere.service;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.sfchick.Data_Sphere.model.Person;
import ru.sfchick.Data_Sphere.repositories.PeopleRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, PasswordEncoder passwordEncoder) {
        this.peopleRepository = peopleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Person> findByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    public Optional<Person> findByEmail(String email) {
        return peopleRepository.findByEmail(email);
    }

    public Person findById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    @Transactional
    public void register(Person person) {
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRole("ROLE_USER");

        person.setAvatar("default");

        peopleRepository.save(person);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void delete(Person person) throws IOException {
        File dirPath = new File("user-avatar/" + person.getId());
        if (dirPath.exists()) {
            FileUtils.deleteDirectory(dirPath);
        }
        peopleRepository.delete(person);
    }

    @Transactional
    public void update(Person person) {
        System.out.println("Updating person: " + person);
        peopleRepository.save(person);
        System.out.println("Person updated: " + person);
    }

    @Transactional
    public void updateProfile(Person updatePerson) {
        System.out.println("Person to updated: " + updatePerson);
        Person person = findById(updatePerson.getId());
        person.setEmail(updatePerson.getEmail());

        // Если новый пароль не пустой, обновляем его
        if (updatePerson.getPassword() != null && !updatePerson.getPassword().isEmpty()) {
            person.setPassword(passwordEncoder.encode(updatePerson.getPassword()));
        }

        peopleRepository.save(person);
        System.out.println("Person updated: " + person);
    }


    @Transactional
    public void updateAvatar(Person person) {
        person.setAvatar(person.getAvatar());
    }

    @Transactional
    public void updateRole(int id, String role) {
        Person person = peopleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found"));

        person.setRole(role);

        peopleRepository.save(person);
    }

}

