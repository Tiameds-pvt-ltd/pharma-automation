package com.pharma.controller;

import com.pharma.dto.*;
import com.pharma.entity.User;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.InventoryDetailsService;
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
@RequestMapping("/pharma/inventoryDetails")
public class InventoryDetailsController {

    @Autowired
    private InventoryDetailsService inventoryDetailsService;

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
        List<InventoryDetailsDto> inventoryDetailsDtos= inventoryDetailsService.getAllInventoryDetails(pharmacyId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Inventory retrieved successfully", HttpStatus.OK, inventoryDetailsDtos);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/currentYearStockWithSupplier")
    public ResponseEntity<?> getCurrentYearStockWithSupplier(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        List<ExpiredStockDto> stockList =
                inventoryDetailsService.getCurrentYearStockWithSupplier(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Current year stock with supplier retrieved successfully",
                HttpStatus.OK,
                stockList
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/nextThreeMonthsStockWithSupplier")
    public ResponseEntity<?> getNextThreeMonthsStockWithSupplier(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        List<ExpiredStockView> stockList =
                inventoryDetailsService.getNextThreeMonthsStockWithSupplier(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Next 3 months stock with supplier retrieved successfully",
                HttpStatus.OK,
                stockList
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<?> savePurchaseOrder(
            @RequestBody InventoryDetailsDto inventoryDetailsDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        InventoryDetailsDto savedInventoryDetails = inventoryDetailsService.saveInventoryDetails(inventoryDetailsDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Inventory Details created successfully", HttpStatus.OK, savedInventoryDetails);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/stockReport")
    public ResponseEntity<?> getInventory(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        List<InventoryView> inventoryList =
                inventoryDetailsService.getInventory(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Inventory retrieved successfully",
                HttpStatus.OK,
                inventoryList
        );
    }

}
