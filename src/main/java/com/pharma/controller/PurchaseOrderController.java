package com.pharma.controller;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.entity.User;
import com.pharma.service.PurchaseOrderService;
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
@RequestMapping("/pharma/purchaseOrder")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/save")
    public ResponseEntity<?> savePurchaseOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody PurchaseOrderDto purchaseOrderDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        PurchaseOrderDto savedOrder = purchaseOrderService.savePurchaseOrder(purchaseOrderDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Purchase order created successfully", HttpStatus.OK, savedOrder);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPurchaseOrders(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<PurchaseOrderDto> purchaseOrders = purchaseOrderService.getAllPurchaseOrders(pharmacyId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase orders retrieved successfully", HttpStatus.OK, purchaseOrders);
    }


    @GetMapping("/getById/{orderId}")
    public ResponseEntity<?> getPurchaseOrderById(
            @RequestHeader("Authorization") String token,
            @PathVariable("orderId") UUID orderId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        PurchaseOrderDto purchaseOrderDto = purchaseOrderService.getPurchaseOrderById(pharmacyId, orderId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase order retrieved successfully",
                HttpStatus.OK,
                purchaseOrderDto
        );
    }


    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<?> deletePurchaseOrderById(
            @RequestHeader("Authorization") String token,
            @PathVariable("orderId") UUID orderId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        Long createdById = currentUserOptional.get().getId();
        try {
            purchaseOrderService.deletePurchaseOrderById(pharmacyId, orderId,currentUserOptional.get());
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

}
