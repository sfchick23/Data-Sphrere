package ru.sfchick.Data_Sphere.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification")
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    @Column(name = "code")
    private String code;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    // Конструктор с установкой времени истечения
    public Verification(String email, String code, LocalDateTime expirationTime) {
        this.email = email;
        this.code = code;
        this.expirationTime = expirationTime;
    }
}