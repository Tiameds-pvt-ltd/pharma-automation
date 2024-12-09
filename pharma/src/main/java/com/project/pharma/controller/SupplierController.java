package com.project.pharma.controller;

import com.project.pharma.dto.SupplierDto;
import com.project.pharma.service.SupplierService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(value = "http://localhost:3000")
@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/supplier")
public class SupplierController {

    private SupplierService supplierService;

    @PostMapping("/save")
    public ResponseEntity<SupplierDto> createSupplier(@RequestBody SupplierDto supplierDto){
        SupplierDto saveSupplier = supplierService.createSupplier(supplierDto);
        return new ResponseEntity<>(saveSupplier, HttpStatus.CREATED);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<SupplierDto> getSupplierById(@PathVariable("id") Integer supplierId){
        SupplierDto supplierDto = supplierService.getSupplierById(supplierId);
        return ResponseEntity.ok(supplierDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<SupplierDto>> getAllSupplier(){
        List<SupplierDto> supplierDtos = supplierService.getAllSupplier();
        return ResponseEntity.ok(supplierDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable("id") Integer supplierId, @RequestBody SupplierDto updatedSupplier) {
        SupplierDto supplierDto = supplierService.updateSupplier(supplierId, updatedSupplier);
        return ResponseEntity.ok(supplierDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSupplier(@PathVariable("id") Integer supplierId) {
        supplierService.deleteSupplier(supplierId);
        return ResponseEntity.ok("Supplier Deleted Successfully");
    }


}

