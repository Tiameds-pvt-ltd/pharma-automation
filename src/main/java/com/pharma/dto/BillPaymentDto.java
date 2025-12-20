package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillPaymentDto {

    private UUID billPaymentId;
    private Long pharmacyId;
    private String paymentId;
    private UUID billId;
    private LocalDateTime paymentDate;
    private String paymentType;
    private BigDecimal paymentAmount;
    private BigDecimal returnAmount;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

}
