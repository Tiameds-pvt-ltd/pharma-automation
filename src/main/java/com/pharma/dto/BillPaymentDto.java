package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillPaymentDto {

    private UUID billPaymentId;
    private Long pharmacyId;
    private UUID billId;
    private LocalDate billPaymentDate;
    private String billPaymentMode;
    private BigDecimal billPaidAmount;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
