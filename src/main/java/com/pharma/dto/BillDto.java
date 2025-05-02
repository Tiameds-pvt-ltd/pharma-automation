package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {

    private UUID billId;
    private Long pharmacyId;
    private LocalDate billDate;
    private LocalTime billTime;
    private Long patientId;
    private Long doctorId;
    private String patientType;
    private BigDecimal subTotal;
    private BigDecimal totalGst;
    private BigDecimal totalDiscount;
    private BigDecimal grandTotal;
    private String paymentType;
    private String billStatus="Pending";
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

    private List<BillItemDto> billItemDtos = new ArrayList<>();
}
