package com.pharma.constants;

import java.util.Map;
import java.util.Set;

public final class InventoryCsvMapping {

    private InventoryCsvMapping() {}

    // CSV headers we care about (normalized)
    public static final Set<String> ALLOWED_HEADERS = Set.of(
            "inventory name",
            "batch number",
            "gst%",
            "purchase price/ unit",
            "sale price/unit",
            "mrp",
            "expiry date",
            "current stock"
    );

    // CSV header → meaning
    public static final Map<String, InventoryCsvField> HEADER_MAPPING = Map.of(
            "inventory name", InventoryCsvField.ITEM_LOOKUP,
            "batch number", InventoryCsvField.BATCH_NO,
            "gst%", InventoryCsvField.GST_PERCENTAGE,
            "purchase price/ unit", InventoryCsvField.PURCHASE_PRICE_PER_UNIT,
            "sale price/unit", InventoryCsvField.MRP_SALE_PRICE_PER_UNIT,
            "mrp", InventoryCsvField.MRP_SALE_PRICE,
            "expiry date", InventoryCsvField.EXPIRY_DATE,
            "current stock", InventoryCsvField.PACKAGE_QUANTITY
    );
}
