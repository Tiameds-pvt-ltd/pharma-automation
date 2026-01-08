package com.pharma.service.impl;

import com.pharma.constants.InventoryCsvField;
import com.pharma.constants.InventoryCsvMapping;
import com.pharma.entity.InventoryDetailsEntity;
import com.pharma.entity.ItemEntity;
import com.pharma.repository.InventoryDetailsRepository;
import com.pharma.repository.ItemRepository;
import com.pharma.utils.CsvUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InventoryImportService {

    private final ItemRepository itemRepository;
    private final InventoryDetailsRepository inventoryRepo;

    public InventoryImportService(
            ItemRepository itemRepository,
            InventoryDetailsRepository inventoryRepo) {
        this.itemRepository = itemRepository;
        this.inventoryRepo = inventoryRepo;
    }

    private static final DateTimeFormatter CSV_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");


    @Transactional
    public void importCsv(MultipartFile file, Long pharmacyId, Long userId) {

        // 🔹 Debug logs
        System.out.println("Inventory CSV Import Started");
        System.out.println("File: " + file.getOriginalFilename());
        System.out.println("Size: " + file.getSize());
        System.out.println("PharmacyId: " + pharmacyId);

        // 🔹 Cache item_name → item_id
        Map<String, UUID> itemCache =
                itemRepository.findAll().stream()
                        .collect(Collectors.toMap(
                                i -> CsvUtils.normalize(i.getItemName()),
                                ItemEntity::getItemId
                        ));

        List<InventoryDetailsEntity> batch = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (
                InputStreamReader reader = new InputStreamReader(file.getInputStream());
                CSVParser parser = CSVFormat.DEFAULT
                        .withFirstRecordAsHeader()
                        .withIgnoreEmptyLines()
                        .withTrim()
                        .withAllowMissingColumnNames() // 🔥 IMPORTANT FIX
                        .parse(reader)
        ) {

            for (CSVRecord record : parser) {

                // 🔹 Skip completely empty rows
                if (record.toMap().values().stream()
                        .allMatch(v -> v == null || v.isBlank())) {
                    continue;
                }

                InventoryDetailsEntity inv = new InventoryDetailsEntity();

                for (String header : record.toMap().keySet()) {

                    // 🔥 SKIP EMPTY HEADERS (caused by trailing commas)
                    if (header == null || header.isBlank()) {
                        continue;
                    }

                    String normalizedHeader = CsvUtils.normalize(header);

                    if (!InventoryCsvMapping.ALLOWED_HEADERS.contains(normalizedHeader)) {
                        continue;
                    }

                    InventoryCsvField field =
                            InventoryCsvMapping.HEADER_MAPPING.get(normalizedHeader);

                    if (field == null) continue;

                    String value = record.get(header).trim();

                    try {
                        switch (field) {

                            case ITEM_LOOKUP -> {
                                UUID itemId =
                                        itemCache.get(CsvUtils.normalize(value));
                                if (itemId == null) {
                                    throw new RuntimeException(
                                            "Item not found: " + value
                                    );
                                }
                                inv.setItemId(itemId);
                            }

                            case BATCH_NO ->
                                    inv.setBatchNo(value);

                            case GST_PERCENTAGE ->
                                    inv.setGstPercentage(Long.parseLong(value));

                            case PURCHASE_PRICE_PER_UNIT ->
                                    inv.setPurchasePricePerUnit(new BigDecimal(value));

                            case MRP_SALE_PRICE_PER_UNIT ->
                                    inv.setMrpSalePricePerUnit(new BigDecimal(value));

                            case MRP_SALE_PRICE ->
                                    inv.setMrpSalePrice(new BigDecimal(value));

                            case EXPIRY_DATE -> {
                                try {
                                    inv.setExpiryDate(LocalDate.parse(value, CSV_DATE_FORMAT));
                                } catch (DateTimeParseException ex) {
                                    throw new RuntimeException(
                                            "Invalid date format. Expected dd-MM-yyyy, found: " + value
                                    );
                                }
                            }


                            case PACKAGE_QUANTITY ->
                                    inv.setPackageQuantity(Long.parseLong(value));
                        }

                    } catch (Exception e) {
                        errors.add(
                                "Row " + record.getRecordNumber()
                                        + " [" + header + "] → " + e.getMessage()
                        );
                    }
                }

                // 🔹 System-controlled fields
                inv.setPharmacyId(pharmacyId);
                inv.setCreatedBy(userId);
                inv.setCreatedDate(LocalDate.now());

                batch.add(inv);
            }

            // 🔹 Validation failure
            if (!errors.isEmpty()) {
                throw new RuntimeException(
                        "CSV validation failed:\n" + String.join("\n", errors)
                );
            }

            inventoryRepo.saveAll(batch);

        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file", e);
        }
    }
}
