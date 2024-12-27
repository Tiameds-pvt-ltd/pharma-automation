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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillDto {

    private Long billId;
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
    private Long billNo;
    private Long enteredBy;

    private List<BillItemDto> billItemDtos = new ArrayList<>();
}
