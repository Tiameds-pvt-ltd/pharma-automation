package com.pharma.service.auth;

import com.pharma.entity.Role;
import com.pharma.entity.User;
import com.pharma.repository.auth.ModuleRepository;
import com.pharma.repository.auth.RoleRepository;
import com.pharma.repository.auth.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public void saveUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        Role userRole = roleRepository.findByName("SUPERADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("SUPERADMIN");
                    try {
                        return roleRepository.save(newRole);
                    } catch (Exception e) {
                        log.error("Error saving role: {}", newRole.getName(), e);
                        throw new RuntimeException("Role could not be saved", e);
                    }
                });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        log.info("Assigning roles to user: {} with roles: {}", user.getUsername(), roles);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("Error saving user: {}", user.getUsername(), e);
            throw new RuntimeException("User could not be saved", e);
        }
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    public User assignRole(Long userId, Integer roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
        } else {
            throw new RuntimeException("User already has this role assigned");
        }

        return userRepository.save(user);
    }

    @Transactional
    public User removeRole(Long userId, Integer roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
        } else {
            throw new RuntimeException("User does not have this role assigned");
        }

        return userRepository.save(user);
    }

    @Transactional
    public User deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        userRepository.delete(user);
        return user;
    }

    public Optional<User> getUser(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public User updateUser(Long userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPhone(user.getPhone());
        existingUser.setAddress(user.getAddress());
        existingUser.setCity(user.getCity());
        existingUser.setState(user.getState());
        existingUser.setZip(user.getZip());
        existingUser.setCountry(user.getCountry());

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existingUser.setRoles(user.getRoles());
        }

        if (user.getModules() != null && !user.getModules().isEmpty()) {
            existingUser.setModules(user.getModules());
        }

        return userRepository.save(existingUser);
    }

    @Transactional(readOnly = true)
    public User getUserWithPharmacies(Long id) {
        return userRepository.findByIdWithPharmacies(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional(readOnly = true)
    public Optional<User> findUserForLogin(String username) {
        return userRepository.findUserForLogin(username);
    }


}
