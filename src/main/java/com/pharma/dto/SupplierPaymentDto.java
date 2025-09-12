package com.pharma.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierPaymentDto {

    private UUID paymentId;
    private UUID supplierId;
    private Long pharmacyId;
    private LocalDate paymentDate;
    private String paymentMode;
    private String referenceNo;
    private BigDecimal totalAmount;
    private String remark;
    private Long createdBy;
    private LocalDate createdDate;
    private Long modifiedBy;
    private LocalDate modifiedDate;

    private List<SupplierPaymentDetailsDto> supplierPaymentDetailsDtos = new ArrayList<>();
}
