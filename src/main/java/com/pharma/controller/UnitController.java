package com.pharma.controller;

import com.pharma.dto.UnitDto;
import com.pharma.service.UnitService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/unit")
public class UnitController {

    private final UnitService unitService;

    @PostMapping("/save")
    public ResponseEntity<UnitDto> createUnit(@RequestBody UnitDto unitDto){
        UnitDto saveUnit = unitService.createUnit(unitDto);
        return new ResponseEntity<>(saveUnit, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UnitDto>> getAllUnit(){
        List<UnitDto> unitDtos = unitService.getAllUnit();
        return ResponseEntity.ok(unitDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UnitDto> updateUnit(@PathVariable("id") Long unitId, @RequestBody UnitDto updateUnit) {
        UnitDto unitDto = unitService.updateUnit(unitId, updateUnit);
        return ResponseEntity.ok(unitDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUnit(@PathVariable("id") Long unitId) {
        unitService.deleteUnit(unitId);
        return ResponseEntity.ok("Unit Deleted Successfully");
    }


}
