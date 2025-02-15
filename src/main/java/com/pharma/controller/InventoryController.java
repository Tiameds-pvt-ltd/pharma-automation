package com.pharma.controller;

import com.pharma.dto.InventoryDto;
import com.pharma.dto.ItemDto;
import com.pharma.dto.StockItemDto;
import com.pharma.service.InventoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/inventory")
public class InventoryController {

    private InventoryService inventoryService;

    @GetMapping("/getAll")
    public ResponseEntity<List<InventoryDto>> getAllInventory(){
        List<InventoryDto> inventoryDtos = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventoryDtos);
    }

    @GetMapping("/expiredStock")
    public List<StockItemDto> getExpiredStock() {
        return inventoryService.getExpiredStock();
    }

}
