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
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Service
public class ItemCSVUploaderService {

    private static final Logger LOGGER = Logger.getLogger(ItemCSVUploaderService.class.getName());
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("^\\d+(\\.\\d+)?$");
    private static final Pattern PERCENTAGE_PATTERN = Pattern.compile("^\\d+(\\.\\d+)?$");


    private static final Set<String> REQUIRED_COLUMNS = Set.of(
            "item_name", "generic_name", "gst_percentage", "hsn_no",
            "manufacturer", "mrp_sale_price", "mrp_sale_price_per_unit",
            "purchase_price", "purchase_price_per_unit", "purchase_unit",
            "unit_name", "variant_name"
    );

    @Autowired
    private ItemRepository itemRepository;

    /**
     * Uploads and processes a CSV file containing item data
     * Validates the CSV structure and data before saving to database
     * Returns a response map with success status and any validation errors
     */
    @Transactional
    public Map<String, Object> uploadItemCsv(MultipartFile file, User currentUser) {
        Map<String, Object> response = new HashMap<>();
        List<ItemEntity> itemEntities = new ArrayList<>();
        List<String> validationErrors = new ArrayList<>();

        // Check if user is authenticated
        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "User authentication failed.");
            return response;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            // Validate CSV headers against required columns
            Set<String> csvHeaders = csvParser.getHeaderMap().keySet();
            Set<String> missingHeaders = new HashSet<>(REQUIRED_COLUMNS);
            missingHeaders.removeAll(csvHeaders);

            if (!missingHeaders.isEmpty()) {
                validationErrors.add("Missing required columns: " + String.join(", ", missingHeaders));
            }

            int rowNumber = 1;
            for (CSVRecord record : csvParser) {
                rowNumber++;
                List<String> rowErrors = validateRow(record, rowNumber);
                if (!rowErrors.isEmpty()) {
                    validationErrors.addAll(rowErrors);
                    continue;
                }

                try {
                    ItemEntity entity = mapCsvToEntity(record, currentUser);
                    itemEntities.add(entity);
                } catch (Exception ex) {
                    validationErrors.add("Row " + rowNumber + ": Error mapping data - " + ex.getMessage());
                }
            }

            // If validation errors found, return them without saving
            if (!validationErrors.isEmpty()) {
                response.put("success", false);
                response.put("message", "CSV validation failed. Please fix the issues and try again.");
                response.put("validationErrors", validationErrors);
                return response;
            }

            // Save valid items to database
            if (!itemEntities.isEmpty()) {
                itemRepository.saveAll(itemEntities);
                response.put("success", true);
                response.put("message", "Items uploaded successfully.");
                response.put("processedItems", itemEntities.size());
                response.put("data", itemEntities);
            } else {
                response.put("success", false);
                response.put("message", "No valid records found in CSV.");
            }

            return response;

        } catch (Exception ex) {
            response.put("success", false);
            response.put("message", "Failed to process CSV: " + ex.getMessage());
            return response;
        }
    }


    /**
     * Validates a single row from the CSV file
     * Checks for required fields and validates numeric field formats
     * Returns a list of validation errors for the row
     */
    private List<String> validateRow(CSVRecord record, int rowNumber) {
        List<String> errors = new ArrayList<>();

        // Check required fields
        for (String column : REQUIRED_COLUMNS) {
            String value = getStringOrBlank(record, column);
            if (value.isEmpty()) {
                errors.add("Row " + rowNumber + ", Column '" + column + "': This field is required");
            }
        }

        // Validate numeric fields
        validateNumericField(record, "gst_percentage", rowNumber, errors, true);
        validateNumericField(record, "mrp_sale_price", rowNumber, errors, false);
        validateNumericField(record, "mrp_sale_price_per_unit", rowNumber, errors, false);
        validateNumericField(record, "purchase_price", rowNumber, errors, false);
        validateNumericField(record, "purchase_price_per_unit", rowNumber, errors, false);
        validateNumericField(record, "purchase_unit", rowNumber, errors, true);

        return errors;
    }

    /**
     * Validates a numeric field in the CSV record
     * Checks if the value matches the expected numeric pattern
     * Adds validation errors to the provided list if validation fails
     */
    private void validateNumericField(CSVRecord record, String column, int rowNumber,
                                      List<String> errors, boolean allowInteger) {
        try {
            String value = getStringOrBlank(record, column);
            if (!value.isEmpty()) {
                if (allowInteger) {
                    if (!NUMERIC_PATTERN.matcher(value).matches()) {
                        errors.add("Row " + rowNumber + ", Column '" + column + "': Must be a valid number");
                    }
                } else {
                    // For decimal fields, try parsing to validate
                    new BigDecimal(value);
                }
            }
        } catch (NumberFormatException e) {
            errors.add("Row " + rowNumber + ", Column '" + column + "': Must be a valid number");
        } catch (IllegalArgumentException e) {
            // Column not found, but we already checked required fields
        }
    }

    /**
     * Maps a CSV record to an ItemEntity object
     * Parses and converts CSV string values to appropriate data types
     * Sets audit fields using the current user context
     */
    private ItemEntity mapCsvToEntity(CSVRecord record, User currentUser) {
        ItemEntity entity = new ItemEntity();

        entity.setItemName(getStringOrBlank(record, "item_name"));
        entity.setGenericName(getStringOrBlank(record, "generic_name"));
        entity.setGstPercentage(parseLongOrNull(record, "gst_percentage"));
        entity.setHsnNo(getStringOrBlank(record, "hsn_no"));
        entity.setManufacturer(getStringOrBlank(record, "manufacturer"));
        entity.setMrpSalePrice(parseBigDecimalOrNull(record, "mrp_sale_price"));
        entity.setMrpSalePricePerUnit(parseBigDecimalOrNull(record, "mrp_sale_price_per_unit"));
        entity.setPurchasePrice(parseBigDecimalOrNull(record, "purchase_price"));
        entity.setPurchasePricePerUnit(parseBigDecimalOrNull(record, "purchase_price_per_unit"));
        entity.setPurchaseUnit(parseLongOrNull(record, "purchase_unit"));
        entity.setUnitName(getStringOrBlank(record, "unit_name"));
        entity.setVariantName(getStringOrBlank(record, "variant_name"));

        // Set pharmacy ID from user context
        entity.setPharmacyId(entity.getPharmacyId());

        // Audit fields
        entity.setCreatedBy(currentUser.getId());
        entity.setCreatedDate(LocalDate.now());
        entity.setModifiedBy(currentUser.getId());
        entity.setModifiedDate(LocalDate.now());

        return entity;
    }

    // Helper methods

    /**
     * Safely retrieves a string value from CSV record or returns empty string
     * Handles cases where column might not exist in the record
     */
    private String getStringOrBlank(CSVRecord record, String column) {
        try {
            return record.get(column) != null ? record.get(column).trim() : "";
        } catch (IllegalArgumentException e) {
            return "";
        }
    }

    /**
     * Parses a long value from CSV record or returns null if empty
     * Throws RuntimeException for invalid number formats
     */
    private Long parseLongOrNull(CSVRecord record, String column) {
        try {
            String value = getStringOrBlank(record, column);
            return value.isEmpty() ? null : Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid number format for column '" + column + "'");
        }
    }

    /**
     * Parses a BigDecimal value from CSV record or returns null if empty
     * Throws RuntimeException for invalid decimal formats
     */
    private BigDecimal parseBigDecimalOrNull(CSVRecord record, String column) {
        try {
            String value = getStringOrBlank(record, column);
            return value.isEmpty() ? null : new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid decimal format for column '" + column + "'");
        }
    }

    // Custom exception for CSV validation errors

    /**
     * Custom exception class for CSV validation errors
     * Contains a list of validation error messages
     */
    public static class CsvValidationException extends RuntimeException {
        private final List<String> validationErrors;

        public CsvValidationException(String message, List<String> validationErrors) {
            super(message);
            this.validationErrors = validationErrors;
        }

        public List<String> getValidationErrors() {
            return validationErrors;
        }

        @Override
        public String getMessage() {
            return super.getMessage() + ". Errors: " + String.join("; ", validationErrors);
        }
    }
}