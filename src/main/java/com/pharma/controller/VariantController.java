package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.UnitDto;
import com.pharma.dto.VariantDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.VariantService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            @RequestBody VariantDto variantDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        VariantDto createdVariant = variantService.createVariant(variantDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Variant created successfully", HttpStatus.OK, createdVariant);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllVariants(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        List<VariantDto> variants = variantService.getAllVariants(pharmacyId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Variants retrieved successfully", HttpStatus.OK, variants);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{variantId}")
    public ResponseEntity<?> getVariantById(
            @PathVariable("variantId") UUID variantId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        VariantDto variantDto = variantService.getVariantById(pharmacyId, variantId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Variant retrieved successfully",
                HttpStatus.OK,
                variantDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @DeleteMapping("/delete/{variantId}")
    public ResponseEntity<?> deleteVariantById(
            @PathVariable("variantId") UUID variantId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        try {
            variantService.deleteVariant(pharmacyId, variantId, currentUser.getUser());
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
            @PathVariable("variantId") UUID variantId,
            @RequestParam Long pharmacyId,
            @RequestBody VariantDto updateVariantDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        VariantDto updatedVariant = variantService.updateVariant(pharmacyId, variantId, updateVariantDto, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Variant updated successfully",
                HttpStatus.OK,
                updatedVariant
        );
    }

}
