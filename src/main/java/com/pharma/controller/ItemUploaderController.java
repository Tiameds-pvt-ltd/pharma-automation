package com.pharma.controller;

import com.pharma.dto.ItemDto;
import com.pharma.entity.ItemEntity;
import com.pharma.entity.User;
import com.pharma.service.ItemCSVUploaderService;
import com.pharma.service.ItemService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/items")
public class ItemUploaderController {

    private static final Logger LOGGER = Logger.getLogger(ItemController.class.getName());

    @Autowired
    private ItemCSVUploaderService itemService;

    @Autowired
    private UserAuthService userAuthService;

    @Transactional
    @PostMapping("/csv/upload")
    public ResponseEntity<?> uploadItemCsv(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token) {
        try {
            Optional<User> userOptional = userAuthService.authenticateUser(token);
            if (userOptional.isEmpty()) {
                return ApiResponseHelper.errorResponse("User authentication failed", HttpStatus.UNAUTHORIZED);
            }
            User currentUser = userOptional.get();

            if (file.isEmpty() || !"text/csv".equals(file.getContentType())) {
                return ApiResponseHelper.errorResponse("Please upload a valid CSV file", HttpStatus.BAD_REQUEST);
            }

            List<ItemEntity> uploadedItems = itemService.uploadItemCsv(file, currentUser);
            List<ItemDto> itemDtos = uploadedItems.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Items uploaded successfully",
                    HttpStatus.CREATED,
                    itemDtos
            );
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (errorMessage.contains("CSV validation failed")) {
                LOGGER.warning("CSV validation error: " + errorMessage);
                return ApiResponseHelper.errorResponse(errorMessage, HttpStatus.BAD_REQUEST);
            } else {
                LOGGER.severe("Error processing CSV upload: " + e.getMessage());
                return ApiResponseHelper.errorResponse("Error processing request: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

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
