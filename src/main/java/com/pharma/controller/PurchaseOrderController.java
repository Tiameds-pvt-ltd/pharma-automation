package com.pharma.controller;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.entity.User;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.PurchaseOrderService;
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
@RequestMapping("/pharma/purchaseOrder")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private UserAuthService userAuthService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> savePurchaseOrder(
            @RequestBody PurchaseOrderDto purchaseOrderDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        PurchaseOrderDto savedOrder = purchaseOrderService.savePurchaseOrder(purchaseOrderDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Purchase order created successfully", HttpStatus.OK, savedOrder);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPurchaseOrders(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<PurchaseOrderDto> purchaseOrders = purchaseOrderService.getAllPurchaseOrders(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase orders retrieved successfully", HttpStatus.OK, purchaseOrders);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{orderId}")
    public ResponseEntity<?> getPurchaseOrderById(
            @PathVariable("orderId") UUID orderId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        PurchaseOrderDto purchaseOrderDto = purchaseOrderService.getPurchaseOrderById(pharmacyId, orderId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase order retrieved successfully",
                HttpStatus.OK,
                purchaseOrderDto
        );
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deletePurchaseOrderById(
            @PathVariable("orderId") UUID orderId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        Long createdById = currentUser.getUser().getId();
        try {
            purchaseOrderService.deletePurchaseOrderById(pharmacyId, orderId,currentUser.getUser());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Purchase order deleted successfully",
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
    @PutMapping("/update/{orderId}")
    public ResponseEntity<?> updatePurchaseOrder(
            @PathVariable UUID orderId,
            @RequestParam Long pharmacyId,
            @RequestBody PurchaseOrderDto updatedPurchaseOrder,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        try {
            PurchaseOrderDto updatedOrder =
                    purchaseOrderService.updatePurchaseOrder(
                            pharmacyId,
                            orderId,
                            updatedPurchaseOrder,
                            currentUser.getUser()
                    );

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Purchase order updated successfully",
                    HttpStatus.OK,
                    updatedOrder
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
