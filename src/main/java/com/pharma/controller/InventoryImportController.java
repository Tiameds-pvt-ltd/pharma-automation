package com.pharma.controller;

import com.pharma.entity.User;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.impl.InventoryImportService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/pharma/inventory-import")
public class InventoryImportController {

    private final InventoryImportService service;
    private final UserRepository userRepository;

    public InventoryImportController(
            InventoryImportService service,
            UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @PostMapping("/import")
    public String importInventory(
            @RequestParam("file") MultipartFile file,
            @RequestParam("pharmacyId") Long pharmacyId,
            Authentication authentication
    ) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        service.importCsv(file, pharmacyId, user.getId());

        return "Inventory import successful";
    }
}
