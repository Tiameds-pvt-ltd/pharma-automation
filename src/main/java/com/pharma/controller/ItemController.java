package com.pharma.controller;


import com.pharma.dto.DoctorDto;
import com.pharma.dto.ItemDto;

import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.ItemRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.ItemService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> createItem(
            @RequestBody ItemDto itemDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        ItemDto savedItem = itemService.createItem(itemDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Item created successfully", HttpStatus.OK, savedItem);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllItems(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<ItemDto> items = itemService.getAllItem(pharmacyId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Items retrieved successfully", HttpStatus.OK, items);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{itemId}")
    public ResponseEntity<?> getItemById(
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

        ItemDto itemDto = itemService.getItemById(pharmacyId, itemId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Item retrieved successfully",
                HttpStatus.OK,
                itemDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{itemId}")
    public ResponseEntity<?> updateDoctorById(
            @PathVariable("itemId") UUID itemId,
            @RequestParam Long pharmacyId,
            @RequestBody ItemDto itemDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        Long modifiedById = currentUser.getUser().getId();

        try {
            ItemDto updatedItem = itemService.updateItem(pharmacyId, itemId, itemDto, currentUser.getUser());
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


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteItem(
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

        try {
            itemService.deleteItem(pharmacyId, itemId, currentUser.getUser());
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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/check-duplicate")
    public ResponseEntity<?> checkDuplicateItem(
            @RequestParam Long pharmacyId,
            @RequestBody ItemDto request,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        boolean isMember = userRepository.existsUserInPharmacy(
                currentUser.getUserId(),
                pharmacyId
        );

        if (!isMember) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error", true,
                            "message", "User does not belong to this pharmacy"
                    ));
        }

        boolean exists = itemRepository
                .existsByItemNameAndManufacturerAndPharmacyId(
                        request.getItemName(),
                        request.getManufacturer(),
                        pharmacyId
                );

        return ResponseEntity.ok(Map.of("duplicate", exists));
    }

}
