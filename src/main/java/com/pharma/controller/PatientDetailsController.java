package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.ItemDto;
import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.PatientDetailsRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.PatientDetailsService;
import com.pharma.service.impl.PatientDetailsServiceImpl;
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
@RequestMapping("/pharma/patient")
public class PatientDetailsController {

    @Autowired
    private PatientDetailsService patientDetailsService;

    @Autowired
    private PatientDetailsServiceImpl patientDetailsServiceImpl;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private PatientDetailsRepository patientDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> createDoctor(
            @RequestBody PatientDetailsDto patientDetailsDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        PatientDetailsDto savedPatient = patientDetailsService.createPatient(patientDetailsDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Patient created successfully", HttpStatus.OK, savedPatient);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDoctors(
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        List<PatientDetailsDto> patients = patientDetailsService.getAllPatient(pharmacyId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Patients retrieved successfully", HttpStatus.OK, patients);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{patientId}")
    public ResponseEntity<?> getDoctorById(
            @PathVariable("patientId") UUID patientId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        PatientDetailsDto patientDetailsDto = patientDetailsService.getPatientById(pharmacyId, patientId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Patient retrieved successfully",
                HttpStatus.OK,
                patientDetailsDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{patientId}")
    public ResponseEntity<?> updatePatientById(
            @PathVariable("patientId") UUID patientId,
            @RequestParam Long pharmacyId,
            @RequestBody PatientDetailsDto patientDetailsDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        try {
            PatientDetailsDto updatePatient = patientDetailsService.updatePatient(pharmacyId, patientId, patientDetailsDto, currentUser.getUser());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Patient updated successfully",
                    HttpStatus.OK,
                    updatePatient
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
    @DeleteMapping("/delete/{patientId}")
    public ResponseEntity<?> deletePatientById(
            @PathVariable("patientId") UUID patientId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        Long createdById = currentUser.getUser().getId();
        try {
            patientDetailsService.deletePatientById(pharmacyId, patientId, currentUser.getUser());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Patient deleted successfully",
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
    public ResponseEntity<?> checkDuplicate(
            @RequestParam Long pharmacyId,
            @RequestBody PatientDetailsDto request,
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

        boolean exists = patientDetailsRepository
                .existsByFirstNameAndPhoneAndPharmacyId(
                        request.getFirstName(),
                        request.getPhone(),
                        pharmacyId
                );

        return ResponseEntity.ok(Map.of("duplicate", exists));
    }

}
