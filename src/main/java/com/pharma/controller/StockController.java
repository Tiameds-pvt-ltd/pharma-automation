package com.pharma.controller;

import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.dto.StockSummaryDto;
import com.pharma.entity.StockItemEntity;
import com.pharma.entity.User;
import com.pharma.service.StockService;
import com.pharma.service.impl.StockSerivceImpl;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;


@RestController
@RequestMapping("/pharma/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private StockSerivceImpl stockSerivceImpl;

    @Autowired
    private final UserAuthService userAuthService;

    @Autowired
    public StockController(StockService stockService, UserAuthService userAuthService) {
        this.stockService = stockService;
        this.userAuthService = userAuthService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveStockItems(
            @RequestHeader("Authorization") String token,
            @RequestBody StockDto StockDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        StockDto savedStock = stockService.saveStock(StockDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Stock created successfully", HttpStatus.OK, savedStock);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStocks(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<StockDto> stocks = stockService.getAllStocks(pharmacyId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stocks retrieved successfully", HttpStatus.OK, stocks);
    }


    @GetMapping("/getById/{invId}")
    public ResponseEntity<?> getStockById(
            @RequestHeader("Authorization") String token,
            @PathVariable("invId") UUID invId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        StockDto stockDto = stockService.getStockById(pharmacyId, invId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock retrieved successfully",
                HttpStatus.OK,
                stockDto
        );
    }


    @DeleteMapping("/delete/{invId}")
    public ResponseEntity<?> deleteStockById(
            @RequestHeader("Authorization") String token,
            @PathVariable("invId") UUID invId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

         try {
            stockService.deleteStock(pharmacyId, invId, currentUserOptional.get());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Stock deleted successfully",
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


    @GetMapping("/getByItemId/{itemId}")
    public ResponseEntity<?> getStockByItemId(
            @RequestHeader("Authorization") String token,
            @PathVariable("itemId") UUID itemId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        Long createdById = currentUserOptional.get().getId();
        List<StockItemDto> stockItems = stockService.getStockByItemId(createdById, itemId);
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock items retrieved successfully",
                HttpStatus.OK,
                stockItems
        );
    }


    @GetMapping("/summary")
    public ResponseEntity<?> getStockSummaryByItem(@RequestHeader("Authorization") String token, @RequestParam Long pharmacyId) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

//        User currentUser = currentUserOptional.get();
//        List<StockDto> stocks = stockService.getAllStocks(currentUser.getId());
        List<StockDto> stocks = stockService.getAllStocks(pharmacyId, currentUserOptional.get());

        Map<UUID, BigDecimal> amountPerItem = new HashMap<>();

        for (StockDto stock : stocks) {
            for (StockItemDto item : stock.getStockItemDtos()) {
                UUID itemId = item.getItemId();
                BigDecimal amount = item.getAmount() != null ? item.getAmount() : BigDecimal.ZERO;
                amountPerItem.merge(itemId, amount, BigDecimal::add);
            }
        }


        List<Map<String, Object>> response = amountPerItem.entrySet().stream().map(entry -> {
            Map<String, Object> map = new HashMap<>();
            map.put("itemId", entry.getKey());
            map.put("totalAmount", entry.getValue());
            return map;
        }).toList();

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock summary fetched successfully", HttpStatus.OK, response);
    }

    @GetMapping("/{supplierId}/items")
    public ResponseEntity<List<StockItemEntity>> getItemsBySupplier(@PathVariable UUID supplierId) {
        List<StockItemEntity> items = stockSerivceImpl.getItemsBySupplierId(supplierId);
        return ResponseEntity.ok(items);
    }


    @PutMapping("/confirmPayment/{invId}")
    public ResponseEntity<?> confirmPayment(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID invId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User currentUser = currentUserOptional.get();

        try {
            stockService.confirmPayment(currentUser.getId(), invId);

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Payment confirmed successfully", HttpStatus.OK, null);
        } catch (RuntimeException e) {
            return ApiResponseHelper.errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/checkBillNo")
    public ResponseEntity<Map<String, Boolean>> checkBillNoExists(
            @RequestParam("supplierId") UUID supplierId,
            @RequestParam("year") int year,
            @RequestParam("purchaseBillNo") String purchaseBillNo) {
        try {
            boolean exists = stockService.isBillNoExists(supplierId, year, purchaseBillNo);
            return ResponseEntity.ok(Map.of("exists", exists));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", true));
        }
    }


    @GetMapping("/paymentStatusAndSupplierFilter")
    public ResponseEntity<?> getStocksByPaymentStatusAndSupplier(
            @RequestHeader("Authorization") String token,
            @RequestParam String paymentStatus,
            @RequestParam UUID supplierId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();

        List<StockSummaryDto> stocks = stockService.getStocksByPaymentStatusAndSupplierAndCreatedBy(
                paymentStatus, supplierId, createdById
        );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stocks retrieved successfully",
                HttpStatus.OK,
                stocks
        );
    }

    @PutMapping("/updateStockItem/{invId}/{itemId}/{batchNo}")
    public ResponseEntity<?> updateStockItem(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID invId,
            @PathVariable UUID itemId,
            @PathVariable String batchNo,
            @RequestBody StockItemDto updateDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User currentUser = currentUserOptional.get();

        try {
            StockItemDto updated = stockService.updateStockItem(
                    currentUser.getId(), invId, itemId, batchNo, updateDto);

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Stock item updated successfully", HttpStatus.OK, updated);
        } catch (RuntimeException e) {
            return ApiResponseHelper.errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
