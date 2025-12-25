package com.pharma.repository;

import com.pharma.entity.TempUserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TempUserRegistrationRepository extends JpaRepository <TempUserRegistration, UUID> {

    Optional<TempUserRegistration> findByEmail(String email);

    void deleteByEmail(String email);

    @Modifying
    @Query("DELETE FROM TempUserRegistration t WHERE t.expiresAt < :now")
    void deleteExpiredRegistrations(@Param("now") LocalDateTime now);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
    DELETE FROM TempUserRegistration t
    WHERE t.email = :email
""")
    void hardDeleteByEmail(@Param("email") String email);



}
