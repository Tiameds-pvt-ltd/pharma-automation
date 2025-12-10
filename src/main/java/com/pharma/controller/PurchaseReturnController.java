package com.pharma.controller;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.dto.PurchaseReturnDto;
import com.pharma.entity.User;
import com.pharma.service.PurchaseReturnService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pharma/purchaseReturn")
public class PurchaseReturnController {

    @Autowired
    private PurchaseReturnService purchaseReturnService;

    @Autowired
    private UserAuthService userAuthService;

    @PostMapping("/save")
    public ResponseEntity<?> savePurchaseReturn(
            @RequestHeader("Authorization") String token,
            @RequestBody PurchaseReturnDto purchaseReturnDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        PurchaseReturnDto savedReturn = purchaseReturnService.savePurchaseReturn(purchaseReturnDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Purchase return created successfully", HttpStatus.OK, savedReturn);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPurchaseReturn(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

         List<PurchaseReturnDto> purchaseReturns = purchaseReturnService.getAllPurchaseReturn(pharmacyId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase return retrieved successfully", HttpStatus.OK, purchaseReturns);
    }



    @GetMapping("/getById/{returnId}")
    public ResponseEntity<?> getPurchaseReturnById(
            @RequestHeader("Authorization") String token,
            @PathVariable("returnId") UUID returnId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        PurchaseReturnDto purchaseReturnDto = purchaseReturnService.getPurchaseReturnById(pharmacyId, returnId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Purchase return retrieved successfully",
                HttpStatus.OK,
                purchaseReturnDto
        );
    }


    @DeleteMapping("/delete/{returnId}")
    public ResponseEntity<?> deletePurchaseReturnById(
            @RequestHeader("Authorization") String token,
            @PathVariable("returnId") UUID returnId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

         try {
            purchaseReturnService.deletePurchaseReturnById(pharmacyId, returnId, currentUserOptional.get());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Purchase return deleted successfully",
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

    @GetMapping("/creditNote/{supplierId}")
    public ResponseEntity<?> getSumReturnAmountBySupplier(
            @RequestHeader("Authorization") String token,
            @PathVariable UUID supplierId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User user = currentUserOptional.get();

        BigDecimal sumReturnAmount =
                purchaseReturnService.getSumReturnAmountBySupplier(supplierId, pharmacyId, user);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Sum of return amount retrieved successfully",
                HttpStatus.OK,
                sumReturnAmount
        );
    }

//    @GetMapping("/creditNote/{supplierId}")
//    public ResponseEntity<?> getSumReturnAmountBySupplier(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("supplierId") UUID supplierId
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        Long createdById = currentUserOptional.get().getId();
//        BigDecimal sumReturnAmount = purchaseReturnService
//                .getSumReturnAmountBySupplierAndCreatedBy(supplierId, createdById);
//        return ApiResponseHelper.successResponseWithDataAndMessage(
//                "Sum of return amount retrieved successfully",
//                HttpStatus.OK,
//                sumReturnAmount
//        );
//    }

}
