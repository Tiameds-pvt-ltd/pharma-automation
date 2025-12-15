package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.UnitDto;
import com.pharma.dto.VariantDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.service.VariantService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/variant")
public class VariantController {

    @Autowired
    private VariantService variantService;

    @Autowired
    private UserAuthService userAuthService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> createVariant(
            @RequestHeader("Authorization") String token,
            @RequestBody VariantDto variantDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        VariantDto createdVariant = variantService.createVariant(variantDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Variant created successfully", HttpStatus.OK, createdVariant);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllVariants(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId)
    {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<VariantDto> variants = variantService.getAllVariants(pharmacyId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Variants retrieved successfully", HttpStatus.OK, variants);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{variantId}")
    public ResponseEntity<?> getVariantById(
            @RequestHeader("Authorization") String token,
            @PathVariable("variantId") UUID variantId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }


        VariantDto variantDto = variantService.getVariantById(pharmacyId, variantId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Variant retrieved successfully",
                HttpStatus.OK,
                variantDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @DeleteMapping("/delete/{variantId}")
    public ResponseEntity<?> deleteVariantById(
            @RequestHeader("Authorization") String token,
            @PathVariable("variantId") UUID variantId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }


        try {
            variantService.deleteVariant(pharmacyId, variantId, currentUserOptional.get());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Variant deleted successfully",
                    HttpStatus.OK,
                    null
            );
        } catch (RuntimeException e) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PutMapping("/update/{variantId}")
    public ResponseEntity<?> updateVariant(
            @RequestHeader("Authorization") String token,
            @PathVariable("variantId") UUID variantId,
            @RequestParam Long pharmacyId,
            @RequestBody VariantDto updateVariantDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null
            );
        }

        VariantDto updatedVariant = variantService.updateVariant(pharmacyId, variantId, updateVariantDto, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Variant updated successfully",
                HttpStatus.OK,
                updatedVariant
        );
    }

}
