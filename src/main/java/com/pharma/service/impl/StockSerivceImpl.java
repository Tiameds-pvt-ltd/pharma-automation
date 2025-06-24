package com.pharma.service.impl;

import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.entity.*;
import com.pharma.mapper.StockMapper;


import com.pharma.repository.InventoryDetailsRepository;
import com.pharma.repository.InventoryRepository;
import com.pharma.repository.StockItemRepository;
import com.pharma.repository.StockRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.StockService;
import com.pharma.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StockSerivceImpl implements StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StockItemRepository stockItemRepository;

    @Autowired
    private InventoryDetailsRepository inventoryDetailsRepository;

    @Transactional
    @Override
    public StockDto saveStock(StockDto stockDto, User user) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        StockEntity stockEntity = stockMapper.toEntity(stockDto);
        stockEntity.setInvId(UUID.randomUUID());
        stockEntity.setCreatedBy(user.getId());
        stockEntity.setCreatedDate(LocalDate.now());

        if (stockEntity.getGoodStatus() == null || stockEntity.getGoodStatus().isEmpty()) {
            stockEntity.setGoodStatus("Received");
        }

        if (stockEntity.getPaymentStatus() == null || stockEntity.getPaymentStatus().isEmpty()) {
            stockEntity.setPaymentStatus("Pending");
        }

        String newGrnNo = generateGrnNo();
        stockEntity.setGrnNo(newGrnNo);

        if (stockEntity.getStockItemEntities() != null && !stockEntity.getStockItemEntities().isEmpty()) {
            for (StockItemEntity stockItem : stockEntity.getStockItemEntities()) {
                stockItem.setStockId(UUID.randomUUID());
                stockItem.setCreatedBy(user.getId());
                stockItem.setCreatedDate(LocalDate.now());
                stockItem.setStockEntity(stockEntity);
            }
        }

        StockEntity savedStock = stockRepository.save(stockEntity);

        if (stockEntity.getStockItemEntities() != null && !stockEntity.getStockItemEntities().isEmpty()) {
            for (StockItemEntity stockItem : stockEntity.getStockItemEntities()) {
                Optional<InventoryEntity> existingStock = inventoryRepository.findByItemId(stockItem.getItemId());
                if (existingStock.isPresent()) {
                    InventoryEntity inventory = existingStock.get();

                    synchronized (this) {
                        inventory.setPackageQuantity(inventory.getPackageQuantity() + stockItem.getPackageQuantity());
                        inventory.setModifiedBy(user.getId());
                        inventory.setModifiedDate(LocalDate.now());
                        inventoryRepository.save(inventory);
                    }
                } else {
                    InventoryEntity newInventory = new InventoryEntity();
                    newInventory.setItemId(stockItem.getItemId());
                    newInventory.setPackageQuantity(stockItem.getPackageQuantity());
                    newInventory.setCreatedBy(user.getId());
                    newInventory.setCreatedDate(LocalDate.now());

                    inventoryRepository.save(newInventory);
                }

                Optional<InventoryDetailsEntity> existingDetailOpt =
                        inventoryDetailsRepository.findByItemIdAndBatchNo(stockItem.getItemId(), stockItem.getBatchNo());

                if (existingDetailOpt.isPresent()) {
                    InventoryDetailsEntity existingDetail = existingDetailOpt.get();
                    existingDetail.setPackageQuantity(
                            existingDetail.getPackageQuantity() + stockItem.getPackageQuantity()
                    );
                    existingDetail.setModifiedBy(user.getId());
                    existingDetail.setModifiedDate(LocalDate.now());
                    inventoryDetailsRepository.save(existingDetail);
                } else {
                    InventoryDetailsEntity newDetail = new InventoryDetailsEntity();
                    newDetail.setItemId(stockItem.getItemId());
                    newDetail.setBatchNo(stockItem.getBatchNo());
                    newDetail.setPackageQuantity(stockItem.getPackageQuantity());
                    newDetail.setExpiryDate(stockItem.getExpiryDate());
                    newDetail.setPurchasePrice(stockItem.getPurchasePrice());
                    newDetail.setMrpSalePrice(stockItem.getMrpSalePrice());
                    newDetail.setPurchasePricePerUnit(stockItem.getPurchasePricePerUnit());
                    newDetail.setMrpSalePricePerUnit(stockItem.getMrpSalePricePerUnit());
                    newDetail.setGstPercentage(stockItem.getGstPercentage());
                    newDetail.setGstAmount(stockItem.getGstAmount());
                    newDetail.setCreatedBy(user.getId());
                    newDetail.setCreatedDate(LocalDate.now());
                    inventoryDetailsRepository.save(newDetail);
                }
            }
        }


        return stockMapper.toDto(stockEntity);
    }

    @Transactional
    @Override
    public List<StockDto> getAllStocks(Long createdById) {
        List<StockEntity> stockEntities = stockRepository.findAllByCreatedBy(createdById);
        return stockEntities.stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public StockDto getStockById(Long createdById, UUID invId) {
        Optional<StockEntity> stockEntity = stockRepository.findByInvIdAndCreatedBy(invId, createdById);

        if (stockEntity.isEmpty()) {
            throw new RuntimeException("Stock not found with ID: " + invId + " for user ID: " + createdById);
        }
        return stockMapper.toDto(stockEntity.get());

    }

    @Transactional
    @Override
    public void deleteStock(Long createdById, UUID invId) {
        Optional<StockEntity> stockEntity = stockRepository.findByInvIdAndCreatedBy(invId, createdById);
        if (stockEntity.isEmpty()) {
            throw new RuntimeException("Stock not found with ID: " + invId + " for user ID: " + createdById);
        }
        stockRepository.delete(stockEntity.get());

    }

    @Transactional
    @Override
    public List<StockItemDto> getStockByItemId(Long createdById, UUID itemId) {
        List<StockItemEntity> stockItems = stockItemRepository.findByItemIdAndCreatedBy(itemId, createdById);

        return stockItems.stream().map(stockItem -> new StockItemDto(
                stockItem.getStockId(),
                stockItem.getItemId(),
                stockItem.getPharmacyId(),
                stockItem.getBatchNo(),
                stockItem.getPackageQuantity(),
                stockItem.getExpiryDate(),
                stockItem.getFreeItem(),
                stockItem.getDiscount(),
                stockItem.getPurchasePrice(),
                stockItem.getMrpSalePrice(),
                stockItem.getPurchasePricePerUnit(),
                stockItem.getMrpSalePricePerUnit(),
                stockItem.getGstPercentage(),
                stockItem.getGstAmount(),
                stockItem.getAmount(),
                stockItem.getCreatedBy(),
                stockItem.getCreatedDate(),
                stockItem.getModifiedBy(),
                stockItem.getModifiedDate()
        )).collect(Collectors.toList());
    }



    private String generateGrnNo() {
        String yearPart = String.valueOf(LocalDate.now().getYear());

        Optional<StockEntity> latestGrnOpt = stockRepository.findLatestGrnNo(yearPart);

        int newSequence = 1;
        if (latestGrnOpt.isPresent()) {
            String lastGrnNo = latestGrnOpt.get().getGrnNo();
            String[] parts = lastGrnNo.split("-");

            try {
                if (parts.length == 3) {
                    newSequence = Integer.parseInt(parts[2]) + 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing GRN No sequence: " + lastGrnNo);
            }
        }

        return String.format("GRN-%s-%05d", yearPart, newSequence);
    }

    public List<StockItemEntity> getItemsBySupplierId(UUID supplierId) {
        return stockItemRepository.findItemsBySupplierId(supplierId);
    }

    @Transactional
    @Override
    public void confirmPayment(Long createdById, UUID invId) {
        StockEntity stockEntity = stockRepository.findByInvIdAndCreatedBy(invId, createdById)
                .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + invId + " for user ID: " + createdById));

        stockEntity.setPaymentStatus("Paid");
        stockEntity.setModifiedBy(createdById);
        stockEntity.setModifiedDate(LocalDate.now());

        stockRepository.save(stockEntity);
    }

}
