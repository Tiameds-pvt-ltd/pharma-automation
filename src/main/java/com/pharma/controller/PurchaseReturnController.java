package com.pharma.controller;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.dto.PurchaseReturnDto;
import com.pharma.entity.User;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.PurchaseReturnService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pharma/purchaseReturn")
public class PurchaseReturnController {

    @Autowired
    private PurchaseReturnService purchaseReturnService;

    @Autowired
    private UserAuthService userAuthService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> savePurchaseReturn(
            @RequestBody PurchaseReturnDto purchaseReturnDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        PurchaseReturnDto savedReturn = purchaseReturnService.savePurchaseReturn(purchaseReturnDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Purchase return created successfully", HttpStatus.OK, savedReturn);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPurchaseReturn(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

         List<PurchaseReturnDto> purchaseReturns = purchaseReturnService.getAllPurchaseReturn(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase return retrieved successfully", HttpStatus.OK, purchaseReturns);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{returnId}")
    public ResponseEntity<?> getPurchaseReturnById(
            @PathVariable("returnId") UUID returnId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        PurchaseReturnDto purchaseReturnDto = purchaseReturnService.getPurchaseReturnById(pharmacyId, returnId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase return retrieved successfully",
                HttpStatus.OK,
                purchaseReturnDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{returnId}")
    public ResponseEntity<?> deletePurchaseReturnById(
            @PathVariable("returnId") UUID returnId,
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
            purchaseReturnService.deletePurchaseReturnById(pharmacyId, returnId, currentUser.getUser());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Purchase return deleted successfully",
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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @GetMapping("/creditNote/{supplierId}")
    public ResponseEntity<?> getSumReturnAmountBySupplier(
            @PathVariable UUID supplierId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        BigDecimal sumReturnAmount =
                purchaseReturnService.getSumReturnAmountBySupplier(supplierId, pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Sum of return amount retrieved successfully",
                HttpStatus.OK,
                sumReturnAmount
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{returnId}")
    public ResponseEntity<?> updatePurchaseReturn(
            @PathVariable UUID returnId,
            @RequestParam Long pharmacyId,
            @RequestBody PurchaseReturnDto updatedReturn,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        try {
            PurchaseReturnDto updated =
                    purchaseReturnService.updatePurchaseReturn(
                            pharmacyId,
                            returnId,
                            updatedReturn,
                            currentUser.getUser()
                    );

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Purchase return updated successfully",
                    HttpStatus.OK,
                    updated
            );

        } catch (Exception e) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    e.getMessage(),
                    HttpStatus.BAD_REQUEST,
                    null
            );
        }
    }
}
