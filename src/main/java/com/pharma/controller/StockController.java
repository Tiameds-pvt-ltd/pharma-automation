package com.pharma.controller;

import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.dto.StockSummaryDto;
import com.pharma.entity.StockItemEntity;
import com.pharma.entity.User;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.StockService;
import com.pharma.service.impl.StockSerivceImpl;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> saveStockItems(
            @RequestBody StockDto StockDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        StockDto savedStock = stockService.saveStock(StockDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Stock created successfully", HttpStatus.OK, savedStock);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllStocks(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<StockDto> stocks = stockService.getAllStocks(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stocks retrieved successfully", HttpStatus.OK, stocks);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{invId}")
    public ResponseEntity<?> getStockById(
            @PathVariable("invId") UUID invId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        StockDto stockDto = stockService.getStockById(pharmacyId, invId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock retrieved successfully",
                HttpStatus.OK,
                stockDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{invId}")
    public ResponseEntity<?> deleteStockById(
            @PathVariable("invId") UUID invId,
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
            stockService.deleteStock(pharmacyId, invId, currentUser.getUser());
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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getByItemId/{itemId}")
    public ResponseEntity<?> getStockByItemId(
            @PathVariable("itemId") UUID itemId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }


        List<StockItemDto> stockItems = stockService.getStockByItemId(pharmacyId, itemId,currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock items retrieved successfully",
                HttpStatus.OK,
                stockItems
        );
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/summary")
    public ResponseEntity<?> getStockSummaryByItem(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }


        List<StockDto> stocks = stockService.getAllStocks(pharmacyId, currentUser.getUser());

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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/{supplierId}/items")
    public ResponseEntity<?> getItemsBySupplier(
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

        List<StockItemDto> items =
                stockService.getItemsBySupplierId(pharmacyId, supplierId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stock items retrieved successfully",
                HttpStatus.OK,
                items
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/confirmPayment/{invId}")
    public ResponseEntity<?> confirmPayment(
            @PathVariable UUID invId,
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
            stockService.confirmPayment(pharmacyId, invId, currentUser.getUser());

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Payment confirmed successfully", HttpStatus.OK, null);
        } catch (RuntimeException e) {
            return ApiResponseHelper.errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/checkBillNo")
    public ResponseEntity<?> checkBillNoExists(
            @RequestParam UUID supplierId,
            @RequestParam Long pharmacyId,
            @RequestParam int year,
            @RequestParam String purchaseBillNo,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }


        boolean exists = stockService.isBillNoExists(
                supplierId,
                pharmacyId,
                year,
                purchaseBillNo,
                currentUser.getUser()
        );

        return ResponseEntity.ok(Map.of("exists", exists));
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/paymentStatusAndSupplierFilter")
    public ResponseEntity<?> getStocksByPaymentStatusAndSupplier(
            @RequestParam String paymentStatus,
            @RequestParam UUID supplierId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<StockSummaryDto> stocks =
                stockService.getStocksByPaymentStatusAndSupplierAndPharmacy(
                        paymentStatus, supplierId, pharmacyId, currentUser.getUser()
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Stocks retrieved successfully",
                HttpStatus.OK,
                stocks
        );
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/updateStockItem/{invId}/{itemId}/{batchNo}")
    public ResponseEntity<?> updateStockItem(
            @PathVariable UUID invId,
            @PathVariable UUID itemId,
            @PathVariable String batchNo,
            @RequestParam Long pharmacyId,
            @RequestBody StockItemDto updateDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        try {
            StockItemDto updated = stockService.updateStockItem(
                    currentUser.getUser(),
                    currentUser.getUserId(),
                    pharmacyId,
                    invId,
                    itemId,
                    batchNo,
                    updateDto
            );

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Stock item updated successfully", HttpStatus.OK, updated);

        } catch (RuntimeException e) {
            return ApiResponseHelper.errorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{invId}")
    public ResponseEntity<?> updateStock(
            @PathVariable UUID invId,
            @RequestParam Long pharmacyId,
            @RequestBody StockDto updatedStock,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        try {
            StockDto updated =
                    stockService.updateStock(
                            pharmacyId,
                            invId,
                            updatedStock,
                            currentUser.getUser()
                    );

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Stock updated successfully",
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
