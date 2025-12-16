package com.pharma.controller;

import com.pharma.dto.SupplierDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.SupplierRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.SupplierService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @RequestHeader("Authorization") String token,
            @RequestBody SupplierDto supplierDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        SupplierDto savedSupplier = supplierService.createSupplier(supplierDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Supplier created successfully", HttpStatus.OK, savedSupplier);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSupplier(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<SupplierDto> suppliers = supplierService.getAllSupplier(pharmacyId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Supplier retrieved successfully", HttpStatus.OK, suppliers);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{supplierId}")
    public ResponseEntity<?> getSupplierById(
            @RequestHeader("Authorization") String token,
            @PathVariable("supplierId") UUID supplierId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }


        SupplierDto supplierDto = supplierService.getSupplierById(pharmacyId, supplierId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Supplier retrieved successfully",
                HttpStatus.OK,
                supplierDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{supplierId}")
    public ResponseEntity<?> updateSupplier(
            @RequestHeader("Authorization") String token,
            @PathVariable("supplierId") UUID supplierId,
            @RequestParam Long pharmacyId,
            @RequestBody SupplierDto updatedSupplier
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }


        try {
            SupplierDto updatedSuppliers = supplierService.updateSupplier(
                    pharmacyId, supplierId, updatedSupplier, currentUserOptional.get()
            );            return ApiResponseHelper.successResponseWithDataAndMessage(
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
            @RequestHeader("Authorization") String token,
            @PathVariable("supplierId") UUID supplierId,
            @RequestParam Long pharmacyId

    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        try {
            supplierService.deleteSupplier(
                    pharmacyId,
                    supplierId,
                    currentUserOptional.get()
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
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId,
            @RequestBody SupplierDto request
    ) {
        User user = userAuthService.authenticateUser(token)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Unauthorized"
                        )
                );

        boolean isMember = userRepository.existsUserInPharmacy(
                user.getId(),
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

