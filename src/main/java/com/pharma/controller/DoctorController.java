package com.pharma.controller;

import com.pharma.dto.DoctorDto;
import com.pharma.service.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/doctor")
public class DoctorController {

    private DoctorService doctorService;

    @PostMapping("/save")
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody DoctorDto doctorDto){
        DoctorDto saveDoctor = doctorService.createDoctor(doctorDto);
        return new ResponseEntity<>(saveDoctor, HttpStatus.CREATED);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable("id") Long doctorId){
        DoctorDto doctorDto = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctorDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DoctorDto>> getAllDoctor(){
        List<DoctorDto> doctorDtos = doctorService.getAllDoctor();
        return ResponseEntity.ok(doctorDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable("id") Long doctorId, @RequestBody DoctorDto updateDoctor) {
        DoctorDto doctorDto = doctorService.updateDoctor(doctorId, updateDoctor);
        return ResponseEntity.ok(doctorDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteDoctor(@PathVariable("id") Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok("Doctor Deleted Successfully");
    }
}
