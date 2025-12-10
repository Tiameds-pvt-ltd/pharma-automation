package com.pharma.controller;

import com.pharma.dto.*;
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
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<InventoryDetailsDto> inventoryDetailsDtos= inventoryDetailsService.getAllInventoryDetails(pharmacyId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Inventory retrieved successfully", HttpStatus.OK, inventoryDetailsDtos);
    }

    @GetMapping("/currentYearStockWithSupplier")
    public ResponseEntity<?> getCurrentYearStockWithSupplier(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.errorResponse("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User user = currentUserOptional.get();

        List<ExpiredStockDto> stockList =
                inventoryDetailsService.getCurrentYearStockWithSupplier(pharmacyId, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Current year stock with supplier retrieved successfully",
                HttpStatus.OK,
                stockList
        );
    }


//    @GetMapping("/currentYearStockWithSupplier")
//    public ResponseEntity<?> getCurrentYearStockWithSupplier(
//            @RequestHeader("Authorization") String token
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        Long createdById = currentUserOptional.get().getId();
//        List<ExpiredStockDto> stockList =
//                inventoryDetailsService.getCurrentYearStockWithSupplier(createdById);
//
//        return ApiResponseHelper.successResponseWithDataAndMessage(
//                "Current year stock with supplier retrieved successfully",
//                HttpStatus.OK,
//                stockList
//        );
//    }


//    @GetMapping("/nextThreeMonthsStockWithSupplier")
//    public ResponseEntity<?> getNextThreeMonthsStockWithSupplier(
//            @RequestHeader("Authorization") String token
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Invalid token", HttpStatus.UNAUTHORIZED, null
//            );
//        }
//
//        Long createdById = currentUserOptional.get().getId();
//        List<ExpiredStockView> stockList =
//                inventoryDetailsService.getNextThreeMonthsStockWithSupplier(createdById);
//
//        return ApiResponseHelper.successResponseWithDataAndMessage(
//                "Next 3 months stock with supplier retrieved successfully",
//                HttpStatus.OK,
//                stockList
//        );
//    }

    @GetMapping("/nextThreeMonthsStockWithSupplier")
    public ResponseEntity<?> getNextThreeMonthsStockWithSupplier(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.errorResponse("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User user = currentUserOptional.get();

        List<ExpiredStockView> stockList =
                inventoryDetailsService.getNextThreeMonthsStockWithSupplier(pharmacyId, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Next 3 months stock with supplier retrieved successfully",
                HttpStatus.OK,
                stockList
        );
    }


    @PostMapping("/save")
    public ResponseEntity<?> savePurchaseOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody InventoryDetailsDto inventoryDetailsDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        InventoryDetailsDto savedInventoryDetails = inventoryDetailsService.saveInventoryDetails(inventoryDetailsDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Inventory Details created successfully", HttpStatus.OK, savedInventoryDetails);
    }


    @GetMapping("/stockReport")
    public ResponseEntity<?> getInventory(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.errorResponse("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        User user = currentUserOptional.get();

        List<InventoryView> inventoryList =
                inventoryDetailsService.getInventory(pharmacyId, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Inventory retrieved successfully",
                HttpStatus.OK,
                inventoryList
        );
    }

}
