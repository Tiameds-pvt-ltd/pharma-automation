package com.project.pharma.controller;

import com.project.pharma.dto.ItemDto;
import com.project.pharma.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/item")
public class ItemController {

    private ItemService itemService;

    @PostMapping("/save")
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto itemDto){
        ItemDto saveItem = itemService.createItem(itemDto);
        return new ResponseEntity<>(saveItem, HttpStatus.CREATED);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable("id") Integer itemId){
        ItemDto itemDto = itemService.getItemById(itemId);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<ItemDto>> getAllItem(){
        List<ItemDto> itemDtos = itemService.getAllItem();
        return ResponseEntity.ok(itemDtos);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable("id") Integer itemId, @RequestBody ItemDto updatedItem){
        ItemDto itemDto = itemService.updateItem(itemId, updatedItem);
        return ResponseEntity.ok(itemDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable("id") Integer itemId){
        itemService.deleteItem(itemId);
        return ResponseEntity.ok("Supplier Deleted Successfully");
    }



}
