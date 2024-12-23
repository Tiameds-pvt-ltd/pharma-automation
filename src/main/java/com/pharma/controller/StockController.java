package com.pharma.controller;


import com.pharma.dto.StockDto;
import com.pharma.entity.User;
import com.pharma.service.StockService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/pharma/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    private final UserAuthService userAuthService;

    @Autowired
    public StockController(StockService stockService, UserAuthService userAuthService) {
        this.stockService = stockService;
        this.userAuthService = userAuthService;
    }

    //    New Updated Code
    //create stock items for a user
    @PostMapping("/save")
    public ResponseEntity<?> saveStockItems(
            @RequestHeader("Authorization") String token,
            @RequestBody StockDto StockDto
    ) {
        // Validate token format
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        //service call
        StockDto savedStock = stockService.createStockAndAssociateWithUser(StockDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Stock created successfully", HttpStatus.OK, savedStock);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStocks(
            @RequestHeader("Authorization") String token
    ) {
        // Validate token and extract the user ID from it
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User currentUser = currentUserOptional.get();

        // Call the service to fetch all stocks for the authenticated user
        List<StockDto> userStocks = stockService.getAllStocks(String.valueOf(currentUser.getId()));

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stocks retrieved successfully", HttpStatus.OK, userStocks);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getStockById(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long invId
    ) {
        // Validate token format
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        // Fetch user ID from token
        String userId = String.valueOf(currentUserOptional.get().getId());

        // Service call to fetch stock details
        StockDto stockDto = stockService.getStockById(userId, invId);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock retrieved successfully",
                HttpStatus.OK,
                stockDto
        );
    }

//
//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updateStock(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("id") Long invId,
//            @RequestBody StockDto stockDto
//    ) {
//        // Validate token format
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        // Fetch user ID from token
//        String userId = String.valueOf(currentUserOptional.get().getId());
//
//        // Service call to update the stock details
//        StockDto updatedStock = stockService.updateStock(invId, stockDto, userId);
//
//        return ApiResponseHelper.successResponseWithDataAndMessage(
//                "Stock updated successfully",
//                HttpStatus.OK,
//                updatedStock
//        );
//    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteStock(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") Long invId
    ) {
        // Validate token and fetch user
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        // Service call to delete the stock
        stockService.deleteStock(invId, currentUserOptional.get().getId().toString());

        return ApiResponseHelper.successResponseWithDataAndMessage("Stock deleted successfully", HttpStatus.OK, null);
    }


}
