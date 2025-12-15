package com.pharma.controller;

import com.pharma.dto.BillDto;
import com.pharma.dto.BillReturnDto;
import com.pharma.dto.BillReturnListDto;
import com.pharma.entity.User;
import com.pharma.service.BillReturnService;
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
import java.util.UUID;


@AllArgsConstructor
@RestController
@RequestMapping("/pharma/billReturn")
public class BillReturnController {

    @Autowired
    private BillReturnService billReturnService;

    @Autowired
    private UserAuthService userAuthService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> saveBill(
            @RequestHeader("Authorization") String token,
            @RequestBody BillReturnDto billReturnDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        BillReturnDto savedBillReturn = billReturnService.createBillReturn(billReturnDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Bill Return created successfully", HttpStatus.OK, savedBillReturn);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBillReturns(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<BillReturnDto> billReturnDtos = billReturnService.getAllBillReturn(pharmacyId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill Return retrieved successfully", HttpStatus.OK, billReturnDtos);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{billReturnId}")
    public ResponseEntity<?> getBillById(
            @RequestHeader("Authorization") String token,
            @PathVariable("billReturnId") UUID billReturnId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        BillReturnDto billReturnDto = billReturnService.getBillReturnById(pharmacyId, billReturnId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill Return retrieved successfully",
                HttpStatus.OK,
                billReturnDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{billReturnId}")
    public ResponseEntity<?> deleteBillReturn(
            @RequestHeader("Authorization") String token,
            @PathVariable("billReturnId") UUID billReturnId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        try {
            billReturnService.deleteBillReturn(pharmacyId, billReturnId,currentUserOptional.get());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Bill Return deleted successfully",
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
    @GetMapping("/list")
    public ResponseEntity<?> getBillReturnLists(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User currentUser = currentUserOptional.get();
        List<BillReturnListDto> returnList =
                billReturnService.getBillReturnListsByPharmacy(pharmacyId, currentUser);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill Return list retrieved successfully",
                HttpStatus.OK,
                returnList
        );
    }


}
