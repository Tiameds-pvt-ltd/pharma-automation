package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.DoctorRepository;
import com.pharma.service.DoctorService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @PostMapping("/save")
    public ResponseEntity<?> createDoctor(
            @RequestHeader("Authorization") String token,
            @RequestBody DoctorDto doctorDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        DoctorDto savedDoctor = doctorService.createDoctor(doctorDto, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Doctor created successfully", HttpStatus.OK, savedDoctor);
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

        List<DoctorDto> doctors = doctorService.getAllDoctors(pharmacyId, currentUserOptional.get());
        return ApiResponseHelper.successResponseWithDataAndMessage("Doctors retrieved successfully", HttpStatus.OK, doctors);
    }


    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN', 'DESKROLE')")
    @GetMapping("/getById/{doctorId}")
    public ResponseEntity<?> getDoctorById(
            @RequestHeader("Authorization") String token,
            @PathVariable("doctorId") UUID doctorId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        DoctorDto doctorDto = doctorService.getDoctorById(pharmacyId, doctorId, currentUserOptional.get());

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Doctor retrieved successfully",
                HttpStatus.OK,
                doctorDto
        );
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN', 'ADMIN')")
    @PutMapping("/update/{doctorId}")
    public ResponseEntity<?> updateDoctorById(
            @RequestHeader("Authorization") String token,
            @PathVariable("doctorId") UUID doctorId,
            @RequestParam Long pharmacyId,
            @RequestBody DoctorDto doctorDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }


        try {
            DoctorDto updatedDoctor = doctorService.updateDoctor(pharmacyId, doctorId, doctorDto, currentUserOptional.get());
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
            @RequestHeader("Authorization") String token,
            @PathVariable("doctorId") UUID doctorId,
            @RequestParam Long pharmacyId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }


        try {
            doctorService.deleteDoctorById(pharmacyId, doctorId, currentUserOptional.get());
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
            @RequestHeader("Authorization") String token,
            @RequestParam Long pharmacyId,
            @RequestBody DoctorDto request
    ) {
        Optional<User> userOptional = userAuthService.authenticateUser(token);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", true));
        }

        User user = userOptional.get();

        // ✅ Validate user–pharmacy membership
        boolean isMember = user.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

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
