package com.pharma.controller;

import com.pharma.dto.PharmacistDto;
import com.pharma.dto.PharmacyDto;
import com.pharma.dto.PharmacyRequestDto;
import com.pharma.entity.User;
import com.pharma.service.PharmacyService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pharma/pharmacy-pharmacist")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/save")
    public ResponseEntity<?> savePharmacy(
            @RequestHeader("Authorization") String token,
            @RequestBody PharmacyRequestDto requestDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User user = currentUserOptional.get();

        PharmacyDto savedPharmacy = pharmacyService.savePharmacy(requestDto.getPharmacyDto(), requestDto.getPharmacistDto(), user);

        return ApiResponseHelper.successResponseWithDataAndMessage("Pharmacy and Pharmacist saved successfully", HttpStatus.OK, savedPharmacy);
    }


    @GetMapping("/getAllPharmacies")
    public ResponseEntity<?> getAllPharmacies(@RequestHeader("Authorization") String token) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<PharmacyDto> pharmacies = pharmacyService.getAllPharmacies(currentUserOptional.get().getId());
        return ApiResponseHelper.successResponseWithDataAndMessage("All pharmacies retrieved successfully", HttpStatus.OK, pharmacies);
    }

    @GetMapping("/getAllPharmacists")
    public ResponseEntity<?> getAllPharmacists(@RequestHeader("Authorization") String token) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<PharmacistDto> pharmacists = pharmacyService.getAllPharmacists(currentUserOptional.get().getId());
        return ApiResponseHelper.successResponseWithDataAndMessage("All pharmacists retrieved successfully", HttpStatus.OK, pharmacists);
    }


    @GetMapping("/getPharmacyById/{pharmacyId}")
    public ResponseEntity<?> getPharmacyById(@RequestHeader("Authorization") String token, @PathVariable UUID pharmacyId) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        PharmacyDto pharmacy = pharmacyService.getPharmacyById(currentUserOptional.get().getId(), pharmacyId);
        return ApiResponseHelper.successResponseWithDataAndMessage("Pharmacy retrieved successfully", HttpStatus.OK, pharmacy);
    }


    @GetMapping("/getPharmacistById/{pharmacistId}")
    public ResponseEntity<?> getPharmacistById(@RequestHeader("Authorization") String token, @PathVariable UUID pharmacistId) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        PharmacistDto pharmacist = pharmacyService.getPharmacistById(currentUserOptional.get().getId(), pharmacistId);
        return ApiResponseHelper.successResponseWithDataAndMessage("Pharmacist retrieved successfully", HttpStatus.OK, pharmacist);
    }


    @DeleteMapping("/deletePharmacyById/{pharmacyId}")
    public ResponseEntity<?> deletePharmacyById(@RequestHeader("Authorization") String token, @PathVariable UUID pharmacyId) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        pharmacyService.deletePharmacyById(currentUserOptional.get().getId(), pharmacyId);
        return ApiResponseHelper.successResponseWithDataAndMessage("Pharmacy deleted successfully", HttpStatus.OK, null);
    }

    @DeleteMapping("/deletePharmacistById/{pharmacistId}")
    public ResponseEntity<?> deletePharmacistById(@RequestHeader("Authorization") String token, @PathVariable UUID pharmacistId) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        pharmacyService.deletePharmacistById(currentUserOptional.get().getId(), pharmacistId);
        return ApiResponseHelper.successResponseWithDataAndMessage("Pharmacist deleted successfully", HttpStatus.OK, null);
    }

}
