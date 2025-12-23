package com.pharma.controller;

import com.pharma.dto.PharmacyListDto;
import com.pharma.dto.auth.AuthResponse;
import com.pharma.dto.auth.LoginRequest;
import com.pharma.dto.auth.LoginResponse;
import com.pharma.dto.auth.RegisterRequest;
import com.pharma.dto.auth.ModuleDTO;
import com.pharma.entity.Role;
import com.pharma.entity.User;
import com.pharma.repository.auth.ModuleRepository;
import com.pharma.service.auth.UserDetailsServiceImpl;
import com.pharma.service.auth.UserService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.JwtUtil;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/public")
@Tag(name = "User Controller", description = "Operations pertaining to user management")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtils;
    private final ModuleRepository moduleRepository;

    @Autowired
    public UserController(
            UserService userService,
            AuthenticationManager authenticationManager,
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtils,
            ModuleRepository moduleRepository
    ) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.moduleRepository = moduleRepository;
    }

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Service is up and running";
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest registerRequest) {

        if (userService.existsByUsername(registerRequest.getUsername())) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Username is already taken",
                    HttpStatus.BAD_REQUEST, null);
        }

        if (userService.existsByEmail(registerRequest.getEmail())) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Email is already taken",
                    HttpStatus.BAD_REQUEST, null);
        }

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
//        user.setVerified(registerRequest.isVerified());
        user.setEnabled(true);

        userService.saveUser(user);

        return ApiResponseHelper.successResponseWithDataAndMessage("User registered successfully",
                HttpStatus.CREATED, null);
    }



    /* -------------------- LOGIN (CLEAN & SAFE) -------------------- */

//    @PostMapping("/login")
//    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
//
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            loginRequest.getUsername(),
//                            loginRequest.getPassword()
//                    )
//            );
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new AuthResponse(HttpStatus.BAD_REQUEST,
//                            "Incorrect username or password", null, null));
//        }
//
//        // Load minimal user details (NO LAZY FIELDS)
//        final UserDetails userDetails =
//                userDetailsService.loadUserByUsername(loginRequest.getUsername());
//
//        String token = jwtUtils.generateToken(userDetails.getUsername());
//
//        // Fetch user using SAFE projection (roles only)
//        Optional<User> userOptional = userService.findUserForLogin(loginRequest.getUsername());
//        if (userOptional.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .body(new AuthResponse(HttpStatus.BAD_REQUEST, "User not found", null, null));
//        }
//
//        User user = userOptional.get();
//
//        // Convert roles to list of names
//        List<String> roles = user.getRoles()
//                .stream()
//                .map(Role::getName)
//                .collect(Collectors.toList());
//
//        // Convert modules to DTOs
//        List<ModuleDTO> moduleDTOList = user.getModules()
//                .stream()
//                .map(m -> new ModuleDTO(m.getId(), m.getName()))
//                .toList();
//
//        // Build login response DTO
//        LoginResponse loginResponse = new LoginResponse();
//        loginResponse.setUsername(user.getUsername());
//        loginResponse.setEmail(user.getEmail());
//        loginResponse.setFirstName(user.getFirstName());
//        loginResponse.setLastName(user.getLastName());
//        loginResponse.setRoles(roles);
//        loginResponse.setPhone(user.getPhone());
//        loginResponse.setAddress(user.getAddress());
//        loginResponse.setCity(user.getCity());
//        loginResponse.setState(user.getState());
//        loginResponse.setZip(user.getZip());
//        loginResponse.setCountry(user.getCountry());
//        loginResponse.setVerified(user.isVerified());
//        loginResponse.setEnabled(user.isEnabled());
//        loginResponse.setModules(moduleDTOList);
//
//        return ResponseEntity.ok(
//                new AuthResponse(HttpStatus.OK, "Login successful", token, loginResponse)
//        );
//    }



    /* -------------------- GET USER PHARMACIES (DTO ONLY) -------------------- */

    @GetMapping("/{id}/pharmacies")
    public ResponseEntity<?> getUserPharmacies(@PathVariable Long id) {

        User user = userService.getUserWithPharmacies(id);

        List<PharmacyListDto> list = user.getPharmacies()
                .stream()
                .map(p -> new PharmacyListDto(
                        p.getPharmacyId(),
                        p.getName(),
                        p.getAddress(),
                        p.getCity(),
                        p.getState(),
                        p.getIsActive(),
                        p.getDescription(),
                        p.getCreatedBy() != null ? p.getCreatedBy().getFullName() : null
                ))
                .toList();

        return ResponseEntity.ok(list);
    }
}

