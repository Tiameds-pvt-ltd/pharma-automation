package com.pharma.controller;

import com.pharma.dto.BillDto;
import com.pharma.dto.BillReturnDto;
import com.pharma.dto.BillReturnListDto;
import com.pharma.entity.User;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.BillReturnService;
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
            @RequestBody BillReturnDto billReturnDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        BillReturnDto savedBillReturn = billReturnService.createBillReturn(billReturnDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Bill Return created successfully", HttpStatus.OK, savedBillReturn);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBillReturns(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<BillReturnDto> billReturnDtos = billReturnService.getAllBillReturn(pharmacyId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill Return retrieved successfully", HttpStatus.OK, billReturnDtos);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{billReturnId}")
    public ResponseEntity<?> getBillById(
            @PathVariable("billReturnId") UUID billReturnId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        BillReturnDto billReturnDto = billReturnService.getBillReturnById(pharmacyId, billReturnId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill Return retrieved successfully",
                HttpStatus.OK,
                billReturnDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{billReturnId}")
    public ResponseEntity<?> deleteBillReturn(
            @PathVariable("billReturnId") UUID billReturnId,
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
            billReturnService.deleteBillReturn(pharmacyId, billReturnId,currentUser.getUser());
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
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<BillReturnListDto> returnList =
                billReturnService.getBillReturnListsByPharmacy(pharmacyId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Bill Return list retrieved successfully",
                HttpStatus.OK,
                returnList
        );
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{billReturnId}")
    public ResponseEntity<?> updateBillReturn(
            @PathVariable UUID billReturnId,
            @RequestParam Long pharmacyId,
            @RequestBody BillReturnDto updatedReturn,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        try {
            BillReturnDto updated =
                    billReturnService.updateBillReturn(
                            pharmacyId,
                            billReturnId,
                            updatedReturn,
                            currentUser.getUser()
                    );

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Bill return updated successfully",
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
