package com.pharma.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockDto {

    private Long invId;
    private String supplierId;
    private String store;
    private String purchaseBillNo;
    private LocalDate purchaseDate;
    private Integer creditPeriod;
    private LocalDate paymentDueDate;
    private Integer invoiceAmount;
    private Double totalAmount;
    private Double totalGst;
    private Double totalDiscount;
    private Double grandTotal;
    private String paymentStatus="Pending";
    private String goodStatus="Received";

    private List<StockItemDto> stockItemDtos = new ArrayList<>();       // List of StockItemDto

}
