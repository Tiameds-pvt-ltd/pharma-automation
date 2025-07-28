package com.pharma.dto;

public class PackageQuantityDto {

    private Long packageQuantity;

    public PackageQuantityDto(Long packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

    public Long getPackageQuantity() {
        return packageQuantity;
    }

    public void setPackageQuantity(Long packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

}
