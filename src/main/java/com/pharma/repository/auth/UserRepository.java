package com.pharma.repository.auth;

import com.pharma.entity.User;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @NotNull
    Optional<User> findById(@NotNull Long id);

    @Query("""
        SELECT DISTINCT u
        FROM User u
        LEFT JOIN FETCH u.pharmacies p
        WHERE u.id = :id
    """)
    Optional<User> findByIdWithPharmacies(@Param("id") Long id);

    @Query("""
    SELECT DISTINCT u 
    FROM User u 
    LEFT JOIN FETCH u.roles 
    LEFT JOIN FETCH u.modules
    WHERE u.username = :username
""")
    Optional<User> findUserForLogin(@Param("username") String username);

    @Query("""
    SELECT u FROM User u
    LEFT JOIN FETCH u.pharmacies
    WHERE u.id = :id
""")
    Optional<User> findByIdFetchPharmacies(@Param("id") Long id);

    @Query("""
SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END
FROM User u
JOIN u.pharmacies p
WHERE u.id = :userId
AND p.pharmacyId = :pharmacyId
""")
    boolean existsUserInPharmacy(
            @Param("userId") Long userId,
            @Param("pharmacyId") Long pharmacyId
    );

}
