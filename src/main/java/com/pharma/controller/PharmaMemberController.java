package com.pharma.controller;

import com.pharma.dto.PharmacyDto;
import com.pharma.dto.PharmacyListDto;
import com.pharma.entity.Pharmacy;
import com.pharma.entity.User;
import com.pharma.repository.PharmacyRepository;
import com.pharma.service.impl.UserPharmaService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.PhramacyAccessableFilter;
import com.pharma.utils.UserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/pharma/admin")
@RequiredArgsConstructor
public class PharmaMemberController {

    private final UserAuthService userAuthService;
    private final PharmacyRepository pharmacyRepository;
    private final PhramacyAccessableFilter phramacyAccessableFilter;
    private final UserPharmaService userPharmaService;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/get-user-pharma")
    public ResponseEntity<?> getUserPharma(
            @RequestHeader("Authorization") String token
    ) {
        User currentUser = userAuthService.authenticateUser(token).orElse(null);
        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "User not found or unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        Set<Pharmacy> pharmacies = pharmacyRepository.findPharmaciesByMemberId(currentUser.getId());

        if (pharmacies.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "No Pharmacy found",
                    HttpStatus.OK,
                    List.of()
            );
        }

        List<PharmacyListDto> dtoList = pharmacies.stream()
                .map(p -> new PharmacyListDto(
                        p.getPharmacyId(),
                        p.getName(),
                        p.getAddress(),
                        p.getCity(),
                        p.getState(),
                        p.getIsActive(),
                        p.getDescription(),
                        p.getCreatedBy() != null ? p.getCreatedBy().getFullName() : null
                ))
                .toList();

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Pharmacy fetched successfully",
                HttpStatus.OK,
                dtoList
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/get-user-pharma-by-id")
    public ResponseEntity<?> getUserPharmaById(
            @RequestHeader("Authorization") String token,
            @RequestParam("pharmacyId") Long pharmacyId
    ) {
        User currentUser = userAuthService.authenticateUser(token).orElse(null);
        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "User not found or unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        Pharmacy pharmacy = pharmacyRepository.findPharmacyForUser(pharmacyId, currentUser.getId())
                .orElse(null);

        if (pharmacy == null) {
            return ApiResponseHelper.errorResponse(
                    "Pharmacy not found or not accessible",
                    HttpStatus.NOT_FOUND
            );
        }

        PharmacyDto dto = new PharmacyDto(
                pharmacy.getPharmacyId(),
                pharmacy.getName(),
                pharmacy.getAddress(),
                pharmacy.getCity(),
                pharmacy.getState(),
                pharmacy.getDescription(),
                pharmacy.getIsActive(),
                pharmacy.getGstNo(),
                pharmacy.getLicenseNo(),
                pharmacy.getPharmaLogo(),
                pharmacy.getPharmaZip(),
                pharmacy.getPharmaCountry(),
                pharmacy.getPharmaPhone(),
                pharmacy.getPharmaEmail(),
                pharmacy.getCreatedBy() != null ? pharmacy.getCreatedBy().getId() : null,
                pharmacy.getCreatedDate(),
                pharmacy.getModifiedBy(),
                pharmacy.getModifiedDate()
        );

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Pharmacy fetched successfully",
                HttpStatus.OK,
                dto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PostMapping("/add-member/{pharmacyId}/member/{userId}")
    public ResponseEntity<?> addMemberToPharmacy(
            @PathVariable Long pharmacyId,
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token
    ) {
        User currentUser = userAuthService.authenticateUser(token).orElse(null);
        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "User not found or unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId).orElse(null);
        if (pharmacy == null) {
            return ApiResponseHelper.errorResponse(
                    "Pharmacy not found",
                    HttpStatus.NOT_FOUND
            );
        }

        boolean isAccessible = phramacyAccessableFilter.isPharmacyAccessible(pharmacyId);
        if (!isAccessible) {
            return ApiResponseHelper.errorResponse(
                    "Pharmacy is not accessible",
                    HttpStatus.UNAUTHORIZED
            );
        }

        User userToAdd = userPharmaService.getUserById(userId);
        if (userToAdd == null) {
            return ApiResponseHelper.errorResponse(
                    "User to be added not found",
                    HttpStatus.NOT_FOUND
            );
        }

        if (!pharmacy.getCreatedBy().equals(currentUser)) {
            return ApiResponseHelper.errorResponse(
                    "You are not authorized to modify members of this pharmacy",
                    HttpStatus.UNAUTHORIZED
            );
        }

        if (pharmacy.getMembers().contains(userToAdd)) {
            return ApiResponseHelper.errorResponse(
                    "User is already a member of this pharmacy",
                    HttpStatus.CONFLICT
            );
        }

        pharmacy.getMembers().add(userToAdd);
        pharmacyRepository.save(pharmacy);

        return ApiResponseHelper.successResponse(
                "User added to pharmacy successfully",
                HttpStatus.OK
        );
    }
}
