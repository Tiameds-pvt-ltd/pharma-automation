package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.dto.ItemDto;
import com.pharma.dto.PatientDetailsDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.repository.PatientDetailsRepository;
import com.pharma.service.PatientDetailsService;
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
@RequestMapping("/pharma/patient")
public class PatientDetailsController {

    @Autowired
    private PatientDetailsService patientDetailsService;

    @Autowired
    private UserAuthService userAuthService;

    @Autowired
    private PatientDetailsRepository patientDetailsRepository;

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


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDoctors(
            @RequestHeader("Authorization") String token
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<PatientDetailsDto> patients = patientDetailsService.getAllPatient(currentUserOptional.get().getId());
        return ApiResponseHelper.successResponseWithDataAndMessage("Patients retrieved successfully", HttpStatus.OK, patients);
    }


    @GetMapping("/getById/{patientId}")
    public ResponseEntity<?> getDoctorById(
            @RequestHeader("Authorization") String token,
            @PathVariable("patientId") UUID patientId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        PatientDetailsDto patientDetailsDto = patientDetailsService.getPatientById(createdById, patientId);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Patient retrieved successfully",
                HttpStatus.OK,
                patientDetailsDto
        );
    }


    @PutMapping("/update/{patientId}")
    public ResponseEntity<?> updatePatientById(
            @RequestHeader("Authorization") String token,
            @PathVariable("patientId") UUID patientId,
            @RequestBody PatientDetailsDto patientDetailsDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long modifiedById = currentUserOptional.get().getId();

        try {
            PatientDetailsDto updatePatient = patientDetailsService.updatePatient(modifiedById, patientId, patientDetailsDto);
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

    @DeleteMapping("/delete/{patientId}")
    public ResponseEntity<?> deletePatientById(
            @RequestHeader("Authorization") String token,
            @PathVariable("patientId") UUID patientId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        try {
            patientDetailsService.deletePatientById(createdById, patientId);
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

    @PostMapping("/check-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkDuplicate(@RequestBody PatientDetailsDto request) {
        boolean exists = patientDetailsRepository.existsByPatientNameAndPhone(
                request.getPatientName(), request.getPhone()
        );
        return ResponseEntity.ok(Map.of("duplicate", exists));
    }
}
