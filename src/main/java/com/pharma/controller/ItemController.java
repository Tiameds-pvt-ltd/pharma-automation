package com.pharma.controller;


import com.pharma.dto.DoctorDto;
import com.pharma.dto.ItemDto;

import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.ItemRepository;
import com.pharma.service.ItemService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/item")
public class ItemController {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @PostMapping("/save")
    public ResponseEntity<?> createItem(
            @RequestHeader("Authorization") String token,
            @RequestBody ItemDto itemDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        ItemDto savedItem = itemService.createItem(itemDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Item created successfully", HttpStatus.OK, savedItem);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllItems(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<ItemDto> items = itemService.getAllItem(pharmacyId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Items retrieved successfully", HttpStatus.OK, items);
    }


    @GetMapping("/getById/{itemId}")
    public ResponseEntity<?> getItemById(
            @RequestHeader("Authorization") String token,
            @PathVariable("itemId") UUID itemId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        ItemDto itemDto = itemService.getItemById(pharmacyId, itemId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Item retrieved successfully",
                HttpStatus.OK,
                itemDto
        );
    }

    @PutMapping("/update/{itemId}")
    public ResponseEntity<?> updateDoctorById(
            @RequestHeader("Authorization") String token,
            @PathVariable("itemId") UUID itemId,
            @RequestParam Long pharmacyId,
            @RequestBody ItemDto itemDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long modifiedById = currentUserOptional.get().getId();

        try {
            ItemDto updatedItem = itemService.updateItem(pharmacyId, itemId, itemDto, currentUserOptional.get());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Item updated successfully",
                    HttpStatus.OK,
                    updatedItem
            );
        } catch (ResourceNotFoundException e) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND,
                    null
            );
        }

    }


    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteItem(
            @RequestHeader("Authorization") String token,
            @PathVariable("itemId") UUID itemId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        try {
            itemService.deleteItem(pharmacyId, itemId, currentUserOptional.get());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Item deleted successfully",
                    HttpStatus.OK,
                    null
            );
        } catch (ResourceNotFoundException e) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    e.getMessage(),
                    HttpStatus.NOT_FOUND,
                    null
            );
        }
    }


    @PostMapping("/check-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicateItem(@RequestBody ItemDto request) {
        boolean exists = itemRepository.existsByItemNameAndManufacturer(
                request.getItemName(), request.getManufacturer()
        );
        return ResponseEntity.ok(Map.of("duplicate", exists));
    }

}
