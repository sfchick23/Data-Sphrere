package ru.sfchick.Data_Sphere.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonDTO {

//    @NotEmpty(message = "Name should be not empty")
//    @Size(min = 2, max = 30, message = "name cannot be less than 2 or more than 30")
    private String username;

    @NotEmpty(message = "Email should be not empty")
    @Email(message = "email must have @email.com")
    private String email;

//    @NotEmpty(message = "Password should be not empty")
    private String password;

    private String avatar;

}
