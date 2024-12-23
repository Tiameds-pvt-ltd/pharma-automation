package com.pharma.controller;

import com.pharma.dto.PatientDetailsDto;
import com.pharma.dto.SupplierDto;
import com.pharma.service.PatientDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/patient")
public class PatientDetailsController {

    private PatientDetailsService patientDetailsService;

    @PostMapping("/save")
    public ResponseEntity<PatientDetailsDto> createPatient(@RequestBody PatientDetailsDto patientDetailsDto){
        PatientDetailsDto savePatient = patientDetailsService.createPatient(patientDetailsDto);
        return new ResponseEntity<>(savePatient, HttpStatus.CREATED);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<PatientDetailsDto> getPatientById(@PathVariable("id") Long patientId){
        PatientDetailsDto patientDetailsDto = patientDetailsService.getPatientById(patientId);
        return ResponseEntity.ok(patientDetailsDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<PatientDetailsDto>> getAllPatient(){
        List<PatientDetailsDto> patientDetailsDtos = patientDetailsService.getAllPatient();
        return ResponseEntity.ok(patientDetailsDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<PatientDetailsDto> updatePatient(@PathVariable("id") Long patientId, @RequestBody PatientDetailsDto updatePatient) {
        PatientDetailsDto patientDetailsDto = patientDetailsService.updatePatient(patientId, updatePatient);
        return ResponseEntity.ok(patientDetailsDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable("id") Long patientId) {
        patientDetailsService.deletePatient(patientId);
        return ResponseEntity.ok("Patient Deleted Successfully");
    }
}
