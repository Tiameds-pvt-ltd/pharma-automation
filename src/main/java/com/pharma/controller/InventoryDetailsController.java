package com.pharma.controller;

import com.pharma.dto.ExpiredStockDto;
import com.pharma.dto.InventoryDetailsDto;
import com.pharma.dto.InventoryDto;
import com.pharma.entity.User;
import com.pharma.service.InventoryDetailsService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllInventory(
            @RequestHeader("Authorization") String token
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<InventoryDetailsDto> inventoryDetailsDtos= inventoryDetailsService.getAllInventoryDetails(currentUserOptional.get().getId());
        return ApiResponseHelper.successResponseWithDataAndMessage("Inventory retrieved successfully", HttpStatus.OK, inventoryDetailsDtos);
    }


    @GetMapping("/currentYearStockWithSupplier")
    public ResponseEntity<?> getCurrentYearStockWithSupplier(
            @RequestHeader("Authorization") String token
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        List<ExpiredStockDto> stockList =
                inventoryDetailsService.getCurrentYearStockWithSupplier(createdById);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Current year stock with supplier retrieved successfully",
                HttpStatus.OK,
                stockList
        );
    }
}
