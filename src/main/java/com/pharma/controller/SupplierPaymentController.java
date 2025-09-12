package com.pharma.controller;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.dto.SupplierPaymentDto;
import com.pharma.entity.User;
import com.pharma.service.SupplierPaymentService;
import com.pharma.service.impl.SupplierPaymentServiceImpl;
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
@RequestMapping("/pharma/supplierPayment")
public class SupplierPaymentController {

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    @Autowired
    private SupplierPaymentServiceImpl supplierPaymentServiceImpl;

    @Autowired
    private final UserAuthService userAuthService;

    @Autowired
    public SupplierPaymentController(SupplierPaymentService supplierPaymentService, UserAuthService userAuthService) {
        this.supplierPaymentService = supplierPaymentService;
        this.userAuthService = userAuthService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSupplierPayment(
            @RequestHeader("Authorization") String token,
            @RequestBody SupplierPaymentDto SupplierPaymentDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        SupplierPaymentDto savedSupplierPayment = supplierPaymentService.saveSupplierPayment(SupplierPaymentDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Supplier Payment created successfully", HttpStatus.OK, savedSupplierPayment);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSupplierPayment(
            @RequestHeader("Authorization") String token
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        User currentUser = currentUserOptional.get();
        List<SupplierPaymentDto> supplierPaymentDtos = supplierPaymentService.getAllSupplierPayment(currentUser.getId());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Supplier Payment retrieved successfully", HttpStatus.OK, supplierPaymentDtos);
    }

    @GetMapping("/getById/{paymentId}")
    public ResponseEntity<?> getPurchaseOrderById(
            @RequestHeader("Authorization") String token,
            @PathVariable("paymentId") UUID paymentId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        Long createdById = currentUserOptional.get().getId();
        SupplierPaymentDto supplierPaymentDto = supplierPaymentService.getSupplierPaymentById(createdById, paymentId);
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Supplier Payment retrieved successfully",
                HttpStatus.OK,
                supplierPaymentDto
        );
    }

}
