package com.pharma.controller;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.InventoryDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.User;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.InventoryService;
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

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private UserAuthService userAuthService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllInventory(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        List<InventoryDto> inventoryDtos = inventoryService.getAllInventory(pharmacyId, currentUser.getUser()
        );
        return ApiResponseHelper.successResponseWithDataAndMessage("Inventory retrieved successfully", HttpStatus.OK, inventoryDtos);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/expiredStock")
    public ResponseEntity<?> getExpiredStock(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<StockItemDto> expiredStock =
                inventoryService.getExpiredStock(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Expired stock retrieved successfully",
                HttpStatus.OK,
                expiredStock
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/expiredStockWithSupplier")
    public ResponseEntity<?> getExpiredStockWithSupplier(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<ExpiredStockDto> expiredStock =
                inventoryService.getExpiredStockWithSupplier(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Expired stock with supplier retrieved successfully",
                HttpStatus.OK,
                expiredStock
        );
    }

}
