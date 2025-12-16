package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.ItemDto;
import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.PatientDetailsRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.PatientDetailsService;
import com.pharma.service.impl.PatientDetailsServiceImpl;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            @RequestHeader("Authorization") String token,
            @RequestBody PatientDetailsDto patientDetailsDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        PatientDetailsDto savedPatient = patientDetailsService.createPatient(patientDetailsDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Patient created successfully", HttpStatus.OK, savedPatient);
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDoctors(
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<PatientDetailsDto> patients = patientDetailsService.getAllPatient(pharmacyId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Patients retrieved successfully", HttpStatus.OK, patients);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{patientId}")
    public ResponseEntity<?> getDoctorById(
            @RequestHeader("Authorization") String token,
            @PathVariable("patientId") UUID patientId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        PatientDetailsDto patientDetailsDto = patientDetailsService.getPatientById(pharmacyId, patientId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Patient retrieved successfully",
                HttpStatus.OK,
                patientDetailsDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{patientId}")
    public ResponseEntity<?> updatePatientById(
            @RequestHeader("Authorization") String token,
            @PathVariable("patientId") UUID patientId,
            @RequestParam Long pharmacyId,
            @RequestBody PatientDetailsDto patientDetailsDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        try {
            PatientDetailsDto updatePatient = patientDetailsService.updatePatient(pharmacyId, patientId, patientDetailsDto, currentUserOptional.get());
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
            @RequestHeader("Authorization") String token,
            @PathVariable("patientId") UUID patientId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        try {
            patientDetailsService.deletePatientById(pharmacyId, patientId, currentUserOptional.get());
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
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId,
            @RequestBody PatientDetailsDto request
    ) {
        User user = userAuthService.authenticateUser(token)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED, "Unauthorized"
                        )
                );

        boolean isMember = userRepository.existsUserInPharmacy(
                user.getId(),
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
