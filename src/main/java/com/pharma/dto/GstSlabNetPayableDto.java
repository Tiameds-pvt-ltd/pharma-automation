package com.pharma.dto;

public interface GstSlabNetPayableDto {

    Long getGstPercentage();

    Double getOutputGstAmount();

    Double getInputGstAmount();

    Double getNetGstPayable();
}
