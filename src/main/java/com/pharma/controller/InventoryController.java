package com.pharma.controller;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.InventoryDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.User;
import com.pharma.service.InventoryService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<InventoryDto> inventoryDtos = inventoryService.getAllInventory(pharmacyId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Inventory retrieved successfully", HttpStatus.OK, inventoryDtos);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/expiredStock")
    public ResponseEntity<?> getExpiredStock(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.errorResponse("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User user = currentUserOptional.get();

        List<StockItemDto> expiredStock =
                inventoryService.getExpiredStock(pharmacyId, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Expired stock retrieved successfully",
                HttpStatus.OK,
                expiredStock
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/expiredStockWithSupplier")
    public ResponseEntity<?> getExpiredStockWithSupplier(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.errorResponse("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User user = currentUserOptional.get();

        List<ExpiredStockDto> expiredStock =
                inventoryService.getExpiredStockWithSupplier(pharmacyId, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Expired stock with supplier retrieved successfully",
                HttpStatus.OK,
                expiredStock
        );
    }

}
