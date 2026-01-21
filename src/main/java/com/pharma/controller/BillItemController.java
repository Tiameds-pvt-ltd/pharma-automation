package com.pharma.controller;

import com.pharma.dto.*;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.BillItemService;
import com.pharma.utils.ApiResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pharma/billItem")
public class BillItemController {

    @Autowired
    private BillItemService billItemService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/itemWiseReport")
    public ResponseEntity<?> getItemProfitByMonth(
            @RequestParam UUID itemId,
            @RequestParam Long pharmacyId,
            @RequestParam String monthYear, // MM-yyyy
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<ItemProfitRowDto> profitDetails =
                billItemService.getItemProfitByMonth(
                        itemId,
                        pharmacyId,
                        monthYear,
                        currentUser.getUser()
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Item profit details retrieved successfully",
                HttpStatus.OK,
                profitDetails
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @GetMapping("/netGST")
    public ResponseEntity<?> getNetGstSlabWise(
            @RequestParam Long pharmacyId,
            @RequestParam String monthYear,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<GstSlabNetPayableDto> gstDetails =
                billItemService.getNetGstSlabWise(
                        pharmacyId,
                        monthYear,
                        currentUser.getUser()
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Net GST slab-wise report retrieved successfully",
                HttpStatus.OK,
                gstDetails
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/doctorWiseProfit")
    public ResponseEntity<?> getDoctorWiseItemProfit(
            @RequestParam UUID doctorId,
            @RequestParam Long pharmacyId,
            @RequestParam String monthYear,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<ItemProfitByDoctorDto> result =
                billItemService.getDoctorWiseItemProfit(
                        doctorId,
                        pharmacyId,
                        monthYear,
                        currentUser.getUser()
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Doctor-wise item profit retrieved successfully",
                HttpStatus.OK,
                result
        );
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/dailyProfit")
    public ResponseEntity<?> getDailySalesCostProfit(
            @RequestParam Long pharmacyId,
            @RequestParam String monthYear, // MM-yyyy
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<DailySalesCostProfitDto> result =
                billItemService.getDailySalesCostProfit(
                        pharmacyId,
                        monthYear,
                        currentUser.getUser()
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Daily sales, cost and profit retrieved successfully",
                HttpStatus.OK,
                result
        );
    }


    @GetMapping("/itemWiseProfitSummary")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    public ResponseEntity<?> getItemWiseProfitSummary(
            @RequestParam Long pharmacyId,
            @RequestParam(required = false) String monthYear,
            @RequestParam(required = false) String dateRange,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<ItemProfitSummaryDto> result =
                billItemService.getItemWiseProfitSummary(
                        pharmacyId,
                        monthYear,
                        dateRange,
                        currentUser.getUser()
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Item-wise profit summary retrieved successfully",
                HttpStatus.OK,
                result
        );
    }


    @GetMapping("/productFlowAnalysis")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    public ResponseEntity<?> getItemPatientBillDetails(
            @RequestParam UUID itemId,
            @RequestParam Long pharmacyId,
            @RequestParam(required = false) String monthYear,
            @RequestParam(required = false) String dateRange,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<ItemPatientBillDto> result =
                billItemService.getItemPatientBillDetails(
                        itemId,
                        pharmacyId,
                        monthYear,
                        dateRange,
                        currentUser.getUser()
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Item-wise patient bill details retrieved successfully",
                HttpStatus.OK,
                result
        );
    }


    @GetMapping("/stockFlowAnalysis")
    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    public ResponseEntity<?> getItemDayWiseSales(
            @RequestParam Long pharmacyId,
            @RequestParam(required = false) String monthYear,
            @RequestParam(required = false) String dateRange,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<ItemDayWiseSaleDto> result =
                billItemService.getItemDayWiseSales(
                        pharmacyId,
                        monthYear,
                        dateRange,
                        currentUser.getUser()
                );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Item-wise day-wise sales retrieved successfully",
                HttpStatus.OK,
                result
        );
    }

}
