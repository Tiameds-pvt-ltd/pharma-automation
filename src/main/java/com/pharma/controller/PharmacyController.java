package com.pharma.controller;

import com.pharma.dto.PharmacyDto;

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


@RestController
@RequestMapping("/pharma/pharmacy")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/save")
    public ResponseEntity<?> savePharmacy(
            @RequestHeader("Authorization") String token,
            @RequestBody PharmacyDto pharmacyDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null
            );
        }

        User user = currentUserOptional.get();

        PharmacyDto savedPharmacy = pharmacyService.savePharmacy(pharmacyDto, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Pharmacy saved successfully", HttpStatus.OK, savedPharmacy
        );
    }


    @GetMapping("/getAllPharmacies")
    public ResponseEntity<?> getPharmaciesCreatedByUser(
            @RequestHeader("Authorization") String token) {

        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User currentUser = currentUserOptional.get();
        List<PharmacyDto> pharmacies = pharmacyService.getPharmaciesCreatedByUser(currentUser);

        if (pharmacies.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("No pharmacies found", HttpStatus.OK, null);
        }

        return ApiResponseHelper.successResponseWithDataAndMessage("Pharmacies fetched successfully", HttpStatus.OK, pharmacies);
    }

}
