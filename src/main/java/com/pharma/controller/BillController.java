package com.pharma.controller;

import com.pharma.dto.BillDto;
import com.pharma.service.BillService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/bill")
public class BillController {

    @Autowired
    private BillService billService;

    @PostMapping("/save")
    public ResponseEntity<BillDto> saveBill(@RequestBody BillDto billDto) {
        BillDto savedBill = billService.createBill(billDto);
        return ResponseEntity.ok(savedBill);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<BillDto> getBillById(@PathVariable("id") Long billId) {
        return ResponseEntity.ok(billService.getBillById(billId));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BillDto>> getAllBill() {
        return ResponseEntity.ok(billService.getAllBill());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<BillDto> updateBill(@PathVariable("id") Long billId, @RequestBody BillDto updatedBill) {
        BillDto updatedBillResponse = billService.updateBill(billId, updatedBill);
        return new ResponseEntity<>(updatedBillResponse, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBill(@PathVariable("id") Long billId){
        billService.deleteBill(billId);
        return ResponseEntity.ok("Bill Deleted Successfully");
    }
}
