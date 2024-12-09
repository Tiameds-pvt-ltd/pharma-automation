package com.project.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
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

    private List<StockItemDto> stockItems = new ArrayList<>();;



}
