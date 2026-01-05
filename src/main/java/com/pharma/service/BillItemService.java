package com.pharma.service;

import com.pharma.dto.GstSlabNetPayableDto;
import com.pharma.dto.ItemProfitRowDto;
import com.pharma.entity.User;

import java.util.List;
import java.util.UUID;

public interface BillItemService {

    List<ItemProfitRowDto> getItemProfitByMonth(UUID itemId, Long pharmacyId, String monthYear, User user);

    List<GstSlabNetPayableDto> getNetGstSlabWise(Long pharmacyId, String monthYear, User user);
}
