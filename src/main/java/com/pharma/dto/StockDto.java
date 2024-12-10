package com.pharma.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {

    private Integer invId;
    private String supplierId;
    private String store;
    private String purchaseBillNo;
    private Date purchaseDate;
    private Integer creditPeriod;
    private Date paymentDueDate;
    private Integer invoiceAmount;
    private Double totalAmount;
    private Double totalGst;
    private Double totalDiscount;
    private Double grandTotal;
    private String paymentStatus="Pending";
    private String goodStatus="Received";

    private List<StockItemDto> stockItemDtos = new ArrayList<>();       // List of StockItemDto

}
