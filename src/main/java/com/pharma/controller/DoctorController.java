package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.service.DoctorService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllDoctors(
            @RequestHeader("Authorization") String token
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);

        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        List<DoctorDto> doctors = doctorService.getAllDoctors(currentUserOptional.get().getId());
        return ApiResponseHelper.successResponseWithDataAndMessage("Doctors retrieved successfully", HttpStatus.OK, doctors);
    }


    @GetMapping("/getById/{doctorId}")
    public ResponseEntity<?> getDoctorById(
            @RequestHeader("Authorization") String token,
            @PathVariable("doctorId") UUID doctorId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        DoctorDto doctorDto = doctorService.getDoctorById(createdById, doctorId);

        return ApiResponseHelper.successResponseWithDataAndMessage(
                "Doctor retrieved successfully",
                HttpStatus.OK,
                doctorDto
        );
    }


    @PutMapping("/update/{doctorId}")
    public ResponseEntity<?> updateDoctorById(
            @RequestHeader("Authorization") String token,
            @PathVariable("doctorId") UUID doctorId,
            @RequestBody DoctorDto doctorDto
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long modifiedById = currentUserOptional.get().getId();

        try {
            DoctorDto updatedDoctor = doctorService.updateDoctor(modifiedById, doctorId, doctorDto);
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



    @DeleteMapping("/delete/{doctorId}")
    public ResponseEntity<?> deleteDoctorById(
            @RequestHeader("Authorization") String token,
            @PathVariable("doctorId") UUID doctorId
    ) {
        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
        if (currentUserOptional.isEmpty()) {
            return ApiResponseHelper.successResponseWithDataAndMessage(
                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
        }

        Long createdById = currentUserOptional.get().getId();
        try {
            doctorService.deleteDoctorById(createdById, doctorId);
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


}
