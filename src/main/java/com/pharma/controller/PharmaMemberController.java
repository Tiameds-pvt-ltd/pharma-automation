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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/pharma/admin")
public class PharmaMemberController {

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private PharmacyRepository pharmacyRepository;

    @Autowired
    private PhramacyAccessableFilter phramacyAccessableFilter;

    @Autowired
    private UserPharmaService userPharmaService;

    @Transactional
    @GetMapping("/get-user-pharma")
    public ResponseEntity<?> getUserPharma(
            @RequestHeader("Authorization") String token
    ) {

        // Check if the user is authenticated
        User currentUser = userAuthService.authenticateUser(token).orElse(null);
        if (currentUser == null)
            return ApiResponseHelper.errorResponse("User not found or unauthorized", HttpStatus.UNAUTHORIZED);
        Set<Pharmacy> pharmacy = pharmacyRepository.findPharmacyByUserId(currentUser.getId());
        if (pharmacy.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("No Pharmacy found", HttpStatus.OK, null);
        }
        List<PharmacyDto> pharmacyDtos = pharmacy.stream()
                .map(pharmacy1 -> new PharmacyDto(
                        pharmacy1.getPharmacyId(),
                        pharmacy1.getName(),
                        pharmacy1.getAddress(),
                        pharmacy1.getCity(),
                        pharmacy1.getState(),
                        pharmacy1.getDescription(),
                        pharmacy1.getIsActive(),
                        pharmacy1.getGstNo(),
                        pharmacy1.getLicenseNo(),
                        pharmacy1.getPharmaLogo(),
                        pharmacy1.getPharmaZip(),
                        pharmacy1.getPharmaCountry(),
                        pharmacy1.getPharmaPhone(),
                        pharmacy1.getPharmaEmail(),
                        pharmacy1.getCreatedBy() != null ? pharmacy1.getCreatedBy().getId() : null,
                        pharmacy1.getCreatedDate(),
                        pharmacy1.getModifiedBy(),
                        pharmacy1.getModifiedDate()
                ))
                .toList();
        return ApiResponseHelper.successResponseWithDataAndMessage("Pharmacy fetched successfully", HttpStatus.OK, pharmacyDtos);
    }


    @PostMapping("/add-member/{pharmacyId}/member/{userId}")
    public ResponseEntity<?> addMemberToPharmacy(
            @PathVariable Long pharmacyId,
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {

        // Check if the user is authenticated
        User currentUser = userAuthService.authenticateUser(token).orElse(null);
        if (currentUser == null)
            return ApiResponseHelper.errorResponse("User not found or unauthorized", HttpStatus.UNAUTHORIZED);

        // Check if the pharmacy exists
        Pharmacy pharmacy = pharmacyRepository.findById(pharmacyId).orElse(null);
        if (pharmacy == null)
            return ApiResponseHelper.errorResponse("Pharmacy not found", HttpStatus.NOT_FOUND);


        // Check if the pharmacy is active
        boolean isAccessible = phramacyAccessableFilter.isPharmacyAccessible(pharmacyId);
        if (isAccessible == false) {
            return ApiResponseHelper.errorResponse("Pharmacy is not accessible", HttpStatus.UNAUTHORIZED);
        }

        // Check if the user exists (assuming you have a UserRepository or similar)
        User userToAdd = userPharmaService.getUserById(userId);
        if (userToAdd == null)
            return ApiResponseHelper.errorResponse("User to be added not found", HttpStatus.NOT_FOUND);

        //check creater of the pharmacy
        if (!pharmacy.getCreatedBy().equals(currentUser)) {
            return ApiResponseHelper.errorResponse("You are not authorized to get members of this pharmacy", HttpStatus.UNAUTHORIZED);
        }
        // Add the user to the pharmacy's members
        if (pharmacy.getMembers().contains(userToAdd)) {
            return ApiResponseHelper.errorResponse("User is already a member of this pharmacy", HttpStatus.CONFLICT);
        }
        pharmacy.getMembers().add(userToAdd);
        pharmacyRepository.save(pharmacy);
        return ApiResponseHelper.successResponse("User added to pharmacy successfully", HttpStatus.OK);
    }

}
