package com.pharma.repository.auth;


import com.pharma.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @NotNull
    Optional<User> findById(@NotNull Long id);

    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.pharmacies WHERE u.id = :id")
    Optional<User> findByIdWithPharmacies(@Param("id") Long id);


}