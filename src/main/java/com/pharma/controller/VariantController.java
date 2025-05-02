package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.UnitDto;
import com.pharma.dto.VariantDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.service.VariantService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/variant")
public class VariantController {

    @Autowired
    private VariantService variantService;

    @Autowired
    private UserAuthService userAuthService;

//    @PostMapping("/save")
//    public ResponseEntity<?> createVariant(
//            @RequestHeader("Authorization") String token,
//            @RequestBody VariantDto variantDto
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        VariantDto savedVariant = variantService.createVariant(variantDto, currentUserOptional.get());
//        return ApiResponseHelper.successResponseWithDataAndMessage("Variant created successfully", HttpStatus.OK, savedVariant);
//    }
//
//
//    @GetMapping("/getAll")
//    public ResponseEntity<?> getAllVariant(
//            @RequestHeader("Authorization") String token
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        List<VariantDto> variants = variantService.getAllVariant(currentUserOptional.get().getId());
//        return ApiResponseHelper.successResponseWithDataAndMessage("Variants retrieved successfully", HttpStatus.OK, variants);
//    }
//
//    @GetMapping("/getById/{variantId}")
//    public ResponseEntity<?> getVariantById(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("variantId") UUID variantId
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        Long createdById = currentUserOptional.get().getId();
//
//        VariantDto variantDto = variantService.getVariantById(createdById, variantId);
//
//        return ApiResponseHelper.successResponseWithDataAndMessage(
//                "Variant retrieved successfully",
//                HttpStatus.OK,
//                variantDto
//        );
//    }
//
//    @PutMapping("/update/{variantId}")
//    public ResponseEntity<?> updateDoctorById(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("variantId") UUID variantId,
//            @RequestBody VariantDto variantDto
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        Long modifiedById = currentUserOptional.get().getId();
//
//        try {
//            VariantDto updatedVariant = variantService.updateVariant(modifiedById, variantId, variantDto);
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Variant updated successfully",
//                    HttpStatus.OK,
//                    updatedVariant
//            );
//        } catch (ResourceNotFoundException e) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    e.getMessage(),
//                    HttpStatus.NOT_FOUND,
//                    null
//            );
//        }
//    }
//
//    @DeleteMapping("/delete/{variantId}")
//    public ResponseEntity<?> deleteDoctorById(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("variantId") UUID variantId
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        Long createdById = currentUserOptional.get().getId();
//        try {
//            variantService.deleteVariant(createdById, variantId);
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Variant deleted successfully",
//                    HttpStatus.OK,
//                    null
//            );
//        } catch (ResourceNotFoundException e) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    e.getMessage(),
//                    HttpStatus.NOT_FOUND,
//                    null
//            );
//        }
//    }

    @PostMapping("/save")
    public ResponseEntity<?> createVariant(
            @RequestHeader("Authorization") String token,
            @RequestBody VariantDto variantDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }
        VariantDto createdVariant = variantService.createVariant(variantDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Variant created successfully", HttpStatus.OK, createdVariant);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllVariants(@RequestHeader("Authorization") String token) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        User currentUser = currentUserOptional.get();
        List<VariantDto> variants = variantService.getAllVariant(currentUser.getId());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Variants retrieved successfully", HttpStatus.OK, variants);
    }


    @GetMapping("/getById/{variantId}")
    public ResponseEntity<?> getVariantById(
            @RequestHeader("Authorization") String token,
            @PathVariable("variantId") UUID variantId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        VariantDto variantDto = variantService.getVariantById(createdById, variantId);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Variant retrieved successfully",
                HttpStatus.OK,
                variantDto
        );
    }


    @DeleteMapping("/delete/{variantId}")
    public ResponseEntity<?> deleteVariantById(
            @RequestHeader("Authorization") String token,
            @PathVariable("variantId") UUID variantId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        try {
            variantService.deleteVariant(createdById, variantId);
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Variant deleted successfully",
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
}
