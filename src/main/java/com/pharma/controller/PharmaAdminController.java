package com.pharma.controller;


import com.pharma.dto.MemberRegisterDto;
import com.pharma.dto.UserInPharmaDto;
import com.pharma.dto.auth.MemberDetailsUpdate;
import com.pharma.dto.auth.RegisterRequest;
import com.pharma.entity.Pharmacy;
import com.pharma.entity.User;
import com.pharma.repository.PharmacyRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.MemberUserServices;
import com.pharma.service.auth.UserService;
import com.pharma.service.impl.UserPharmacyService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.PhramacyAccessableFilter;
import com.pharma.utils.UserAuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/user-management")
public class PharmaAdminController {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MemberUserServices memberUserServices;

    @Autowired
    private PhramacyAccessableFilter phramacyAccessableFilter;

    @Autowired
    private UserPharmacyService userPharmacyService;

    public PharmaAdminController(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @GetMapping("/get-members/{pharmacyId}")
    public ResponseEntity<?> getPharmacyMembers(
            @PathVariable Long pharmacyId,
            @RequestHeader("Authorization") String token) {
        User currentUser = userAuthService.authenticateUser(token).orElse(null);
        if (currentUser == null) {
            return ApiResponseHelper.errorResponse("User not found or unauthorized", HttpStatus.UNAUTHORIZED);
        }
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId).orElse(null);
        if (pharmacy == null) {
            return ApiResponseHelper.errorResponse("Pharmacy not found", HttpStatus.NOT_FOUND);
        }
        boolean isAccessible = phramacyAccessableFilter.isPharmacyAccessible(pharmacyId);
        if (isAccessible == false) {
            return ApiResponseHelper.errorResponse("Pharmacy is not accessible", HttpStatus.UNAUTHORIZED);
        }
//        if (!lab.getCreatedBy().equals(currentUser)) {
//            return ApiResponseHelper.errorResponse("You are not authorized to view members of this lab", HttpStatus.UNAUTHORIZED);
//        }

        // check if the user is correct role to view members SUPERADMIN AND ADMIN

        List<UserInPharmaDto> memberDTOs = MemberUserServices.getMembersInPharmacy(pharmacy);
        return ApiResponseHelper.successResponse("Pharmacy members retrieved successfully", memberDTOs);
    }


    @Transactional
    @PostMapping("/create-user/{pharmacyId}")
    public ResponseEntity<?> createUserInLab(
            @RequestBody MemberRegisterDto registerRequest,
            @PathVariable Long pharmacyId,
            @RequestHeader("Authorization") String token) {

        User currentUser = userAuthService.authenticateUser(token).orElse(null);
        if (currentUser == null)
            return ApiResponseHelper.errorResponse("User not found or unauthorized", HttpStatus.UNAUTHORIZED);

        boolean isAccessible = phramacyAccessableFilter.isPharmacyAccessible(pharmacyId);
        if (isAccessible == false) {
            return ApiResponseHelper.errorResponse("Pharmacy is not accessible", HttpStatus.UNAUTHORIZED);
        }

       Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId).orElse(null);
        if (pharmacy == null)
            return ApiResponseHelper.errorResponse("Pharmacy not found", HttpStatus.NOT_FOUND);

        // Check creator of the lab
//        if (!lab.getCreatedBy().equals(currentUser)) {
//            return ApiResponseHelper.errorResponse("You are not authorized to create members in this lab", HttpStatus.UNAUTHORIZED);
//        }
        //check the user is already a member of the lab using username and email
        if (pharmacy.getMembers().stream().anyMatch(user -> user.getUsername().equals(registerRequest.getUsername()) || user.getEmail().equals(registerRequest.getEmail()))) {
            return ApiResponseHelper.errorResponse("User is already a member of this pharmacy", HttpStatus.CONFLICT);
        }

        // deligate to memberUserServices to create user and add to lab send currentuser and registerRequest
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ApiResponseHelper.errorResponse("Username already exists", HttpStatus.CONFLICT);
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ApiResponseHelper.errorResponse("Email already exists", HttpStatus.CONFLICT);
        }

        // Create a new user and add to the lab
        memberUserServices.createUserAndAddToPharmacy(registerRequest, pharmacy, currentUser);

        return ApiResponseHelper.successResponse("User created and added to pharmacy successfully", HttpStatus.OK);
    }


    @Transactional
    @PutMapping("/update-user/{pharmacyId}/{userId}")
    public ResponseEntity<?> updateUserInLab(
            @PathVariable Long pharmacyId,
            @PathVariable Long userId,
            @RequestBody MemberDetailsUpdate registerRequest,
            @RequestHeader("Authorization") String token) {

        User currentUser = userAuthService.authenticateUser(token).orElse(null);
        if (currentUser == null) {
            return ApiResponseHelper.errorResponse("User not found or unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId).orElse(null);
        if (pharmacy == null) {
            return ApiResponseHelper.errorResponse("Pharmacy not found", HttpStatus.NOT_FOUND);
        }

        boolean isAccessible = phramacyAccessableFilter.isPharmacyAccessible(pharmacyId);
        if (!isAccessible) {
            return ApiResponseHelper.errorResponse("Pharmacy is not accessible", HttpStatus.UNAUTHORIZED);
        }

        // Check creator of the lab
//        if (!lab.getCreatedBy().equals(currentUser)) {
//            return ApiResponseHelper.errorResponse("You are not authorized to update members in this lab", HttpStatus.UNAUTHORIZED);
//        }

        User userToUpdate = userRepository.findById(userId).orElse(null);
        if (userToUpdate == null) {
            return ApiResponseHelper.errorResponse("User not found", HttpStatus.NOT_FOUND);
        }

        // delegate to memberUserServices to update user and add to lab
        memberUserServices.updateUserInPharmacy(registerRequest, userToUpdate, pharmacy, currentUser);

        return ApiResponseHelper.successResponse("User updated successfully", HttpStatus.OK);

    }

    @Transactional
    @PutMapping("/reset-password/{pharmacyId}/{userId}")
    public ResponseEntity<?> resetUserPassword(
            @PathVariable Long pharmacyId,
            @PathVariable Long userId,
            @RequestBody Map<String, String> passwordRequest, // Change to accept JSON object
            @RequestHeader("Authorization") String token) {
        User currentUser = userAuthService.authenticateUser(token).orElse(null);
        if (currentUser == null) {
            return ApiResponseHelper.errorResponse("User not found or unauthorized", HttpStatus.UNAUTHORIZED);
        }

        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId).orElse(null);
        if (pharmacy == null) {
            return ApiResponseHelper.errorResponse("Pharmacy not found", HttpStatus.NOT_FOUND);
        }

        boolean isAccessible = phramacyAccessableFilter.isPharmacyAccessible(pharmacyId);
        if (!isAccessible) {
            return ApiResponseHelper.errorResponse("Pharmacy is not accessible", HttpStatus.UNAUTHORIZED);
        }

        // Check creator of the lab
        if (!pharmacy.getCreatedBy().equals(currentUser)) {
            return ApiResponseHelper.errorResponse("You are not authorized to reset password for members in this Pharmacy", HttpStatus.UNAUTHORIZED);
        }

        User userToUpdate = userRepository.findById(userId).orElse(null);
        if (userToUpdate == null) {
            return ApiResponseHelper.errorResponse("User not found", HttpStatus.NOT_FOUND);
        }

        String newPassword = passwordRequest.get("newPassword");
        String confirmPassword = passwordRequest.get("confirmPassword");

        // Validate passwords match
        if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword)) {
            return ApiResponseHelper.errorResponse("Passwords don't match", HttpStatus.BAD_REQUEST);
        }

        // Validate password length
        if (newPassword.length() < 8) {
            return ApiResponseHelper.errorResponse("Password must be at least 8 characters", HttpStatus.BAD_REQUEST);
        }

        // Reset password
        userToUpdate.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(userToUpdate);
        return ApiResponseHelper.successResponse("Password reset successfully", HttpStatus.OK);
    }
}