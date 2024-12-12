package ru.sfchick.Data_Sphere.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sfchick.Data_Sphere.model.Verification;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<Verification, String > {
    Optional<Verification> findByCode(String code);
    void deleteByExpirationTimeBefore(LocalDateTime time);
}