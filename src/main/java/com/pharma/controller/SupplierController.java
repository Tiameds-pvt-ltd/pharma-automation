package com.pharma.controller;

import com.pharma.dto.SupplierDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.SupplierRepository;
import com.pharma.service.SupplierService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSupplier(
            @RequestHeader("Authorization") String token
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<SupplierDto> suppliers = supplierService.getAllSupplier(currentUserOptional.get().getId());
        return ApiResponseHelper.successResponseWithDataAndMessage("Supplier retrieved successfully", HttpStatus.OK, suppliers);
    }

    @GetMapping("/getById/{supplierId}")
    public ResponseEntity<?> getSupplierById(
            @RequestHeader("Authorization") String token,
            @PathVariable("supplierId") UUID supplierId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        SupplierDto supplierDto = supplierService.getSupplierById(createdById, supplierId);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Supplier retrieved successfully",
                HttpStatus.OK,
                supplierDto
        );
    }


    @PutMapping("/update/{supplierId}")
    public ResponseEntity<?> updateSupplier(
            @RequestHeader("Authorization") String token,
            @PathVariable("supplierId") UUID supplierId,
            @RequestBody SupplierDto updatedSupplier
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long modifiedById = currentUserOptional.get().getId();

        try {
            SupplierDto updatedSuppliers = supplierService.updateSupplier(modifiedById, supplierId, updatedSupplier);
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

    @DeleteMapping("/delete/{supplierId}")
    public ResponseEntity<?> deleteSupplier(
            @RequestHeader("Authorization") String token,
            @PathVariable("supplierId") UUID supplierId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        try {
            supplierService.deleteSupplier(createdById, supplierId);
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


    @PostMapping("/check-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateSupplier(@RequestBody SupplierDto request) {
        Map<String, Boolean> result = new HashMap<>();
        result.put("supplierName", supplierRepository.existsBySupplierName(request.getSupplierName()));
        result.put("supplierMobile", supplierRepository.existsBySupplierMobile(request.getSupplierMobile()));
        result.put("supplierGstinNo", supplierRepository.existsBySupplierGstinNo(request.getSupplierGstinNo()));

        return ResponseEntity.ok(result);
    }

}

