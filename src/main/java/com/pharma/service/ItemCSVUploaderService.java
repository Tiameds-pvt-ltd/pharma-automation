package com.pharma.service;

import com.pharma.entity.ItemEntity;
import com.pharma.entity.User;
import com.pharma.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ItemCSVUploaderService {

    private static final Logger LOGGER = Logger.getLogger(ItemCSVUploaderService.class.getName());

    @Autowired
    private ItemRepository itemRepository;

    @Transactional
    public List<ItemEntity> uploadItemCsv(MultipartFile file, User currentUser) {
        List<ItemEntity> itemEntities = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();

        if (currentUser == null) {
            throw new RuntimeException("User authentication failed.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            int rowNumber = 1; // Start with header row + 1
            for (CSVRecord record : csvParser) {
                rowNumber++;
                try {
                    ItemEntity entity = new ItemEntity();
                    List<String> rowErrors = new ArrayList<>();

                    // Required fields validation
                    String itemName = getStringOrBlank(record, "item_name");
                    if (itemName.isEmpty()) {
                        rowErrors.add("Item Name is required");
                    }

                    String genericName = getStringOrBlank(record, "generic_name");
                    if (genericName.isEmpty()) {
                        rowErrors.add("Generic Name is required");
                    }

                    String gstPercentage = getStringOrBlank(record, "gst_percentage");
                    if (gstPercentage.isEmpty()) {
                        rowErrors.add("GST Percentage is required");
                    }

                    String hsnNo = getStringOrBlank(record, "hsn_no");
                    if (hsnNo.isEmpty()) {
                        rowErrors.add("HSN No is required");
                    }

                    String manufacturer = getStringOrBlank(record, "manufacturer");
                    if (manufacturer.isEmpty()) {
                        rowErrors.add("Manufacturer is required");
                    }

                    String mrpSalePrice = getStringOrBlank(record, "mrp_sale_price");
                    if (mrpSalePrice.isEmpty()) {
                        rowErrors.add("MRP Sale Price is required");
                    }

                    String mrpSalePricePerUnit = getStringOrBlank(record, "mrp_sale_price_per_unit");
                    if (mrpSalePricePerUnit.isEmpty()) {
                        rowErrors.add("MRP Sale Price Per Unit is required");
                    }

                    String purchasePrice = getStringOrBlank(record, "purchase_price");
                    if (purchasePrice.isEmpty()) {
                        rowErrors.add("Purchase Price is required");
                    }

                    String purchasePricePerUnit = getStringOrBlank(record, "purchase_price_per_unit");
                    if (purchasePricePerUnit.isEmpty()) {
                        rowErrors.add("Purchase Price Per Unit is required");
                    }

                    String purchaseUnit = getStringOrBlank(record, "purchase_unit");
                    if (purchaseUnit.isEmpty()) {
                        rowErrors.add("Purchase Unit is required");
                    }

                    String unitName = getStringOrBlank(record, "unit_name");
                    if (unitName.isEmpty()) {
                        rowErrors.add("Unit Name is required");
                    }

                    String variantName = getStringOrBlank(record, "variant_name");
                    if (variantName.isEmpty()) {
                        rowErrors.add("Variant Name is required");
                    }

                    // If any validation errors, add to the list and skip this row
                    if (!rowErrors.isEmpty()) {
                        validationErrors.add("Row " + (rowNumber - 1) + ": " + String.join(", ", rowErrors));
                        continue;
                    }

                    // Map CSV fields to entity
                    entity.setItemName(itemName);
                    entity.setGenericName(genericName);
                    entity.setGstPercentage(parseLongOrNull(record, "gst_percentage"));
                    entity.setHsnNo(hsnNo);
                    entity.setManufacturer(manufacturer);
                    entity.setMrpSalePrice(parseBigDecimalOrNull(record, "mrp_sale_price"));
                    entity.setMrpSalePricePerUnit(parseBigDecimalOrNull(record, "mrp_sale_price_per_unit"));
                    entity.setPurchasePrice(parseBigDecimalOrNull(record, "purchase_price"));
                    entity.setPurchasePricePerUnit(parseBigDecimalOrNull(record, "purchase_price_per_unit"));
                    entity.setPurchaseUnit(parseLongOrNull(record, "purchase_unit"));
                    entity.setUnitName(unitName);
                    entity.setVariantName(variantName);

                    // Set pharmacy ID from user context
                    entity.setPharmacyId(entity.getPharmacyId());

                    // Audit fields
                    entity.setCreatedBy(currentUser.getId());
                    entity.setCreatedDate(LocalDate.now());
                    entity.setModifiedBy(currentUser.getId());
                    entity.setModifiedDate(LocalDate.now());

                    itemEntities.add(entity);

                } catch (Exception ex) {
                    LOGGER.warning("Skipping row " + record.getRecordNumber() + " due to error: " + ex.getMessage());
                    validationErrors.add("Row " + (rowNumber - 1) + ": " + ex.getMessage());
                }
            }

            // If there are validation errors, throw exception with all errors
            if (!validationErrors.isEmpty()) {
                throw new RuntimeException("CSV validation failed: " + String.join("; ", validationErrors) +
                        ". All fields are mandatory. Please fill all fields and try again.");
            }

            // Save all valid entities
            if (!itemEntities.isEmpty()) {
                itemRepository.saveAll(itemEntities);
            }

            return itemEntities;

        } catch (Exception ex) {
            LOGGER.severe("Failed to process CSV file: " + ex.getMessage());
            throw new RuntimeException("Failed to process CSV file: " + ex.getMessage(), ex);
        }
    }

    // Helper methods
    private String getStringOrBlank(CSVRecord record, String column) {
        try {
            return record.get(column) != null ? record.get(column).trim() : "";
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    private Long parseLongOrNull(CSVRecord record, String column) {
        try {
            String value = getStringOrBlank(record, column);
            return value.isEmpty() ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid number format for column '" + column + "' at record " + record.getRecordNumber());
            throw new RuntimeException("Invalid number format for column '" + column + "'");
        }
    }

    private BigDecimal parseBigDecimalOrNull(CSVRecord record, String column) {
        try {
            String value = getStringOrBlank(record, column);
            return value.isEmpty() ? null : new BigDecimal(value);
        } catch (NumberFormatException e) {
            LOGGER.warning("Invalid decimal format for column '" + column + "' at record " + record.getRecordNumber());
            throw new RuntimeException("Invalid decimal format for column '" + column + "'");
        }
    }
}