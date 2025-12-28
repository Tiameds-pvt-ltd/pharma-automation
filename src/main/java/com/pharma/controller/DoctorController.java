package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.DoctorRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.security.CustomUserDetails;
import com.pharma.service.DoctorService;
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
@RequestMapping("/pharma/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> createDoctor(
            @RequestBody DoctorDto doctorDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        DoctorDto savedDoctor = doctorService.createDoctor(doctorDto, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Doctor created successfully", HttpStatus.OK, savedDoctor);
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

        List<DoctorDto> doctors = doctorService.getAllDoctors(pharmacyId, currentUser.getUser());
        return ApiResponseHelper.successResponseWithDataAndMessage("Doctors retrieved successfully", HttpStatus.OK, doctors);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{doctorId}")
    public ResponseEntity<?> getDoctorById(
            @PathVariable("doctorId") UUID doctorId,
            @RequestParam Long pharmacyId,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }

        DoctorDto doctorDto = doctorService.getDoctorById(pharmacyId, doctorId, currentUser.getUser());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Doctor retrieved successfully",
                HttpStatus.OK,
                doctorDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{doctorId}")
    public ResponseEntity<?> updateDoctorById(
            @PathVariable("doctorId") UUID doctorId,
            @RequestParam Long pharmacyId,
            @RequestBody DoctorDto doctorDto,
            @AuthenticationPrincipal CustomUserDetails currentUser)
    {

        if (currentUser == null) {
            return ApiResponseHelper.errorResponse(
                    "Unauthorized",
                    HttpStatus.UNAUTHORIZED
            );
        }
        try {
            DoctorDto updatedDoctor = doctorService.updateDoctor(pharmacyId, doctorId, doctorDto, currentUser.getUser());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Item updated successfully",
                    HttpStatus.OK,
                    updatedDoctor
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
    @DeleteMapping("/delete/{doctorId}")
    public ResponseEntity<?> deleteDoctorById(
            @PathVariable("doctorId") UUID doctorId,
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
            doctorService.deleteDoctorById(pharmacyId, doctorId, currentUser.getUser());
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Doctor deleted successfully",
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
            @RequestBody DoctorDto request,
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

        boolean exists = doctorRepository
                .existsByDoctorNameAndDoctorMobileAndPharmacyId(
                        request.getDoctorName(),
                        request.getDoctorMobile(),
                        pharmacyId
                );

        return ResponseEntity.ok(Map.of("duplicate", exists));
    }



}
