package com.pharma.controller;

import com.pharma.dto.PharmacyDto;

import com.pharma.security.CustomUserDetails;
import com.pharma.service.PharmacyService;
import com.pharma.service.impl.UserPharmaService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pharma/pharmacy")
public class PharmacyController {

    @Autowired
    private PharmacyService pharmacyService;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private UserPharmaService userPharmaService;

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping("/save")
    public ResponseEntity<?> savePharmacy(
            @RequestBody PharmacyDto pharmacyDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (userPharmaService.existsPharmaByName(pharmacyDto.getName())) {
            return ApiResponseHelper.errorResponse(
                    "Pharmacy already exists",
                    HttpStatus.BAD_REQUEST
            );
        }

        PharmacyDto savedPharmacy = pharmacyService.savePharmacy(pharmacyDto, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Pharmacy saved successfully",
                HttpStatus.OK,
                savedPharmacy
        );
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAllPharmacies")
    public ResponseEntity<?> getPharmaciesCreatedByUser(
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<PharmacyDto> pharmacies = pharmacyService.getPharmaciesCreatedByUser(currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Pharmacies fetched successfully",
                HttpStatus.OK,
                pharmacies.isEmpty() ? null : pharmacies
        );
    }
}
