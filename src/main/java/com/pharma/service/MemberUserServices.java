package com.pharma.service;

import com.pharma.dto.MemberRegisterDto;
import com.pharma.dto.UserInPharmaDto;
import com.pharma.dto.auth.MemberDetailsUpdate;
import com.pharma.entity.Pharmacy;
import com.pharma.entity.Role;
import com.pharma.entity.User;
import com.pharma.repository.auth.RoleRepository;
import com.pharma.repository.auth.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MemberUserServices {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public MemberUserServices(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public static List<UserInPharmaDto> getMembersInPharmacy(Pharmacy pharmacy) {
        return pharmacy.getMembers().stream()
                .map(user -> new UserInPharmaDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.isEnabled(),
                        user.getPhone(),
                        user.getCity(),
                        user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()
                        ))).collect(Collectors.toList());
    }

    public void createUserAndAddToPharmacy(MemberRegisterDto registerRequest, Pharmacy pharmacy, User currentUser) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhone(registerRequest.getPhone());
        user.setAddress(registerRequest.getAddress());
        user.setCity(registerRequest.getCity());
        user.setState(registerRequest.getState());
        user.setZip(registerRequest.getZip());
        user.setCountry(registerRequest.getCountry());
        user.setVerified(registerRequest.isVerified());
        user.setEnabled(registerRequest.getEnabled());
        user.setCreatedBy(currentUser);
        user.setRoles(registerRequest.getRoles()
                .stream()
                .map(roleName -> roleRepository.findByName(String.valueOf(roleName))
                        //if no role found, create a new one
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(String.valueOf(roleName));

                            return roleRepository.save(newRole);
                        }))
                .collect(Collectors.toSet()));
        user.setPharmacies(Set.of(pharmacy));
        try {
            userRepository.save(user);
            pharmacy.getMembers().add(user);
        } catch (Exception e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }


    public void updateUserInPharmacy(MemberDetailsUpdate registerRequest, User userToUpdate, Pharmacy pharmacy, User currentUser) {
        userToUpdate.setUsername(registerRequest.getUsername());
        userToUpdate.setEmail(registerRequest.getEmail());
        userToUpdate.setFirstName(registerRequest.getFirstName());
        userToUpdate.setLastName(registerRequest.getLastName());
        userToUpdate.setPhone(registerRequest.getPhone());
        userToUpdate.setAddress(registerRequest.getAddress());
        userToUpdate.setCity(registerRequest.getCity());
        userToUpdate.setState(registerRequest.getState());
        userToUpdate.setZip(registerRequest.getZip());
        userToUpdate.setCountry(registerRequest.getCountry());
        userToUpdate.setVerified(registerRequest.isVerified());
        userToUpdate.setEnabled(registerRequest.getEnabled());
        userToUpdate.setCreatedBy(currentUser);
        Set<Role> roles = registerRequest.getRoles()
                .stream()
                .map(roleName -> roleRepository.findByName(String.valueOf(roleName))
                        .orElseGet(() -> {
                            Role newRole = new Role();
                            newRole.setName(String.valueOf(roleName));
                            return roleRepository.save(newRole);
                        }))
                .collect(Collectors.toSet());
        userToUpdate.setRoles(roles);
        try {
            userRepository.save(userToUpdate);
            pharmacy.getMembers().add(userToUpdate);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }
}
