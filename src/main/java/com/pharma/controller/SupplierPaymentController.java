package com.pharma.controller;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.dto.SupplierPaymentDto;
import com.pharma.entity.User;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.SupplierPaymentService;
import com.pharma.service.impl.SupplierPaymentServiceImpl;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<?> saveSupplierPayment(
            @RequestBody SupplierPaymentDto SupplierPaymentDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        SupplierPaymentDto savedSupplierPayment = supplierPaymentService.saveSupplierPayment(SupplierPaymentDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Supplier Payment created successfully", HttpStatus.OK, savedSupplierPayment);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllSupplierPayment(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<SupplierPaymentDto> supplierPaymentDtos = supplierPaymentService.getAllSupplierPayment(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Supplier Payment retrieved successfully", HttpStatus.OK, supplierPaymentDtos);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @GetMapping("/getById/{paymentId}")
    public ResponseEntity<?> getPurchaseOrderById(
            @PathVariable("paymentId") UUID paymentId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        SupplierPaymentDto supplierPaymentDto = supplierPaymentService.getSupplierPaymentById(pharmacyId, paymentId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Supplier Payment retrieved successfully",
                HttpStatus.OK,
                supplierPaymentDto
        );
    }

}
