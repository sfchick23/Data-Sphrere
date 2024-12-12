package ru.sfchick.Data_Sphere.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "Name should be not empty")
    @Size(min = 2, max = 30, message = "name cannot be less than 2 or more than 30")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Email should be not empty")
    @Email(message = "email must have @email.com")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Password should be not empty")
    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "avatar", nullable = true)
    private String avatar;

    @Column(name = "verified")
    private boolean verified;

    @Transient
    public String getPhotosImagePath() {
        if (avatar == null || avatar.isEmpty() || avatar.equals("default")) {
            return "/user-avatar/default/default-avatar.jpg";
        }
        return "/user-avatar/" + id + "/" + avatar;
    }

}
