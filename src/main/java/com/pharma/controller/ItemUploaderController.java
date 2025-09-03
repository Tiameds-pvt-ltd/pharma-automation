package com.pharma.controller;

import com.pharma.dto.ItemDto;
import com.pharma.entity.ItemEntity;
import com.pharma.entity.User;
import com.pharma.service.ItemCSVUploaderService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemUploaderController {

    private static final Logger LOGGER = Logger.getLogger(ItemUploaderController.class.getName());

    @Autowired
    private ItemCSVUploaderService itemService;

    @Autowired
    private UserAuthService userAuthService;


    /**
     * Endpoint for uploading CSV file containing item data
     * Authenticates user, validates file type, and processes CSV upload
     * Returns appropriate HTTP response based on processing result
     */
    @Transactional
    @PostMapping("/csv/upload")
    public ResponseEntity<?> uploadItemCsv(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token) {

        // Authenticate user using the provided token
        Optional<User> userOptional = userAuthService.authenticateUser(token);
        if (userOptional.isEmpty()) {
            return ApiResponseHelper.errorResponse("User authentication failed", HttpStatus.UNAUTHORIZED);
        }

        // Check if file is empty
        if (file.isEmpty()) {
            return ApiResponseHelper.errorResponse("Please upload a CSV file", HttpStatus.BAD_REQUEST);
        }

        // Validate file type is CSV
        if (!"text/csv".equals(file.getContentType()) && !file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
            return ApiResponseHelper.errorResponse("Please upload a valid CSV file", HttpStatus.BAD_REQUEST);
        }

        // Process the CSV file using the service
        Map<String, Object> result = itemService.uploadItemCsv(file, userOptional.get());

        // Return error response if processing failed
        if (!(boolean) result.get("success")) {
            return ResponseEntity.badRequest().body(result);
        }

        // Return success response with created status
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    /**
     * Converts ItemEntity to ItemDto for data transfer
     * Maps all relevant fields from entity to DTO
     */
    private ItemDto convertToDto(ItemEntity entity) {
        ItemDto dto = new ItemDto();
        dto.setItemId(entity.getItemId());
        dto.setItemName(entity.getItemName());
        dto.setPurchaseUnit(entity.getPurchaseUnit());
        dto.setVariantName(entity.getVariantName());
        dto.setUnitName(entity.getUnitName());
        dto.setManufacturer(entity.getManufacturer());
        dto.setPurchasePrice(entity.getPurchasePrice());
        dto.setMrpSalePrice(entity.getMrpSalePrice());
        dto.setPurchasePricePerUnit(entity.getPurchasePricePerUnit());
        dto.setMrpSalePricePerUnit(entity.getMrpSalePricePerUnit());
        dto.setGstPercentage(entity.getGstPercentage());
        dto.setGenericName(entity.getGenericName());
        dto.setHsnNo(entity.getHsnNo());
        dto.setPharmacyId(entity.getPharmacyId());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setModifiedBy(entity.getModifiedBy());
        dto.setModifiedDate(entity.getModifiedDate());
        return dto;
    }
}









