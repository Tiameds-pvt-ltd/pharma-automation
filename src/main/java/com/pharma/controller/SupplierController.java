package com.pharma.controller;

import com.pharma.dto.SupplierDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.SupplierRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.SupplierService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> createSupplier(
            @RequestBody SupplierDto supplierDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        SupplierDto savedSupplier = supplierService.createSupplier(supplierDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Supplier created successfully", HttpStatus.OK, savedSupplier);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSupplier(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<SupplierDto> suppliers = supplierService.getAllSupplier(pharmacyId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Supplier retrieved successfully", HttpStatus.OK, suppliers);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{supplierId}")
    public ResponseEntity<?> getSupplierById(
            @PathVariable("supplierId") UUID supplierId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        SupplierDto supplierDto = supplierService.getSupplierById(pharmacyId, supplierId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Supplier retrieved successfully",
                HttpStatus.OK,
                supplierDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{supplierId}")
    public ResponseEntity<?> updateSupplier(
            @PathVariable("supplierId") UUID supplierId,
            @RequestParam Long pharmacyId,
            @RequestBody SupplierDto updatedSupplier,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }


        try {
            SupplierDto updatedSuppliers = supplierService.updateSupplier(
                    pharmacyId, supplierId, updatedSupplier, currentUser.getUser());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Supplier updated successfully",
                    HttpStatus.OK,
                    updatedSuppliers
            );
        } catch (ResourceNotFoundException e) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{supplierId}")
    public ResponseEntity<?> deleteSupplier(
            @PathVariable("supplierId") UUID supplierId,
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
            supplierService.deleteSupplier(
                    pharmacyId,
                    supplierId,
                    currentUser.getUser()
            );
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Supplier deleted successfully",
                    HttpStatus.OK,
                    null
            );
        } catch (ResourceNotFoundException e) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/check-duplicate")
    public ResponseEntity<?> checkDuplicateSupplier(
            @RequestParam Long pharmacyId,
            @RequestBody SupplierDto request,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        boolean isMember = userRepository.existsUserInPharmacy(
                currentUser.getUserId(),
                pharmacyId
        );

        if (!isMember) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", true,
                            "message", "User does not belong to this pharmacy"
                    ));
        }

        Map<String, Boolean> result = new HashMap<>();

        result.put(
                "supplierName",
                supplierRepository.existsBySupplierNameAndPharmacyId(
                        request.getSupplierName(), pharmacyId
                )
        );

        result.put(
                "supplierMobile",
                supplierRepository.existsBySupplierMobileAndPharmacyId(
                        request.getSupplierMobile(), pharmacyId
                )
        );

        result.put(
                "supplierGstinNo",
                supplierRepository.existsBySupplierGstinNoAndPharmacyId(
                        request.getSupplierGstinNo(), pharmacyId
                )
        );

        return ResponseEntity.ok(result);
    }

}

