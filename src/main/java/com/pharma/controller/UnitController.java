//package com.pharma.controller;
//
//import com.pharma.dto.DoctorDto;
//import com.pharma.dto.UnitDto;
//import com.pharma.dto.VariantDto;
//import com.pharma.entity.User;
//import com.pharma.exception.ResourceNotFoundException;
//import com.pharma.mapper.UnitMapper;
//import com.pharma.repository.UnitRepository;
//import com.pharma.service.UnitService;
//import com.pharma.utils.ApiResponseHelper;
//import com.pharma.utils.UserAuthService;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@CrossOrigin
//@AllArgsConstructor
//@RestController
//@RequestMapping("/pharma/unit")
//public class UnitController {
//
//    @Autowired
//    private UnitService unitService;
//
//    @Autowired
//    private UserAuthService userAuthService;
//
//    @PostMapping("/save")
//    public ResponseEntity<?> createDoctor(
//            @RequestHeader("Authorization") String token,
//            @RequestBody UnitDto unitDto
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        UnitDto savedUnit = unitService.createUnit(unitDto, currentUserOptional.get());
//        return ApiResponseHelper.successResponseWithDataAndMessage("Unit created successfully", HttpStatus.OK, savedUnit);
//    }
//
//    @GetMapping("/getAll")
//    public ResponseEntity<?> getAllDoctors(
//            @RequestHeader("Authorization") String token
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage("Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        List<UnitDto> units = unitService.getAllUnit(currentUserOptional.get().getId());
//        return ApiResponseHelper.successResponseWithDataAndMessage("Units retrieved successfully", HttpStatus.OK, units);
//    }
//
//
//    @GetMapping("/getById/{unitId}")
//    public ResponseEntity<?> getDoctorById(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("unitId") UUID unitId
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        Long createdById = currentUserOptional.get().getId();
//        UnitDto unitDto = unitService.getUnitById(createdById, unitId);
//
//        return ApiResponseHelper.successResponseWithDataAndMessage(
//                "Doctor retrieved successfully",
//                HttpStatus.OK,
//                unitDto
//        );
//    }
//
//    @PutMapping("/update/{unitId}")
//    public ResponseEntity<?> updateDoctorById(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("unitId") UUID unitId,
//            @RequestBody UnitDto unitDto
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        Long modifiedById = currentUserOptional.get().getId();
//
//        try {
//            UnitDto updatedUnit = unitService.updateUnit(modifiedById, unitId, unitDto);
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Unit updated successfully",
//                    HttpStatus.OK,
//                    updatedUnit
//            );
//        } catch (ResourceNotFoundException e) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    e.getMessage(),
//                    HttpStatus.NOT_FOUND,
//                    null
//            );
//        }
//    }
//
//    @DeleteMapping("/delete/{unitId}")
//    public ResponseEntity<?> deleteDoctorById(
//            @RequestHeader("Authorization") String token,
//            @PathVariable("unitId") UUID unitId
//    ) {
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Invalid token", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        Long createdById = currentUserOptional.get().getId();
//        try {
//            unitService.deleteUnit(createdById, unitId);
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    "Unit deleted successfully",
//                    HttpStatus.OK,
//                    null
//            );
//        } catch (ResourceNotFoundException e) {
//            return ApiResponseHelper.successResponseWithDataAndMessage(
//                    e.getMessage(),
//                    HttpStatus.NOT_FOUND,
//                    null
//            );
//        }
//    }
//
//}
