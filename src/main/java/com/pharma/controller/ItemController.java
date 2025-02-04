package com.pharma.controller;


import com.pharma.dto.ItemDto;

import com.pharma.entity.User;
import com.pharma.service.ItemService;
import com.pharma.utils.ApiResponseHelper;
import com.pharma.utils.UserAuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/pharma/item")
public class ItemController {

    private UserAuthService userAuthService;


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

//    @GetMapping("/getAll")
//    public ResponseEntity<Map<String, Object>> getAllItem(@RequestHeader("Authorization") String token){
//
//        // Validate token format
//        Optional<User> currentUserOptional = userAuthService.authenticateUser(token);
//
//        // If user is not found, return unauthorized response
//        if (currentUserOptional.isEmpty()) {
//            return ApiResponseHelper.successResponseWithDataAndMessage("User not found", HttpStatus.UNAUTHORIZED, null);
//        }
//
//        System.out.println("currentUserOptional--------"+currentUserOptional);
//
//
//        List<ItemDto> itemDtos = itemService.getAllItem();
//        return ApiResponseHelper.successResponseWithDataAndMessage("Data Found", HttpStatus.OK,itemDtos);
//    }

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
