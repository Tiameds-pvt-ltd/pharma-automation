package com.pharma.service.impl;

import com.pharma.dto.StockDto;
import com.pharma.dto.StockItemDto;
import com.pharma.dto.StockSummaryDto;
import com.pharma.entity.*;
import com.pharma.exception.ResourceNotFoundException;
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
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(stockDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        StockEntity stockEntity = stockMapper.toEntity(stockDto);
        stockEntity.setInvId(UUID.randomUUID());
        stockEntity.setCreatedBy(user.getId());
        stockEntity.setCreatedDate(LocalDate.now());

        stockEntity.setPharmacyId(stockDto.getPharmacyId());

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

        Long pharmacyId = stockEntity.getPharmacyId();

        if (stockEntity.getStockItemEntities() != null && !stockEntity.getStockItemEntities().isEmpty()) {
            for (StockItemEntity stockItem : stockEntity.getStockItemEntities()) {
                Optional<InventoryEntity> existingStock = inventoryRepository.findByItemId(stockItem.getItemId());
                if (existingStock.isPresent()) {
                    InventoryEntity inventory = existingStock.get();

                    synchronized (this) {
                        inventory.setPackageQuantity(inventory.getPackageQuantity() + stockItem.getPackageQuantity());
                        inventory.setPharmacyId(pharmacyId);
                        inventory.setModifiedBy(user.getId());
                        inventory.setModifiedDate(LocalDate.now());
                        inventoryRepository.save(inventory);
                    }
                } else {
                    InventoryEntity newInventory = new InventoryEntity();
                    newInventory.setItemId(stockItem.getItemId());
                    newInventory.setPackageQuantity(stockItem.getPackageQuantity());
                    newInventory.setPharmacyId(pharmacyId);
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
                    existingDetail.setPharmacyId(pharmacyId);
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
                    newDetail.setPharmacyId(pharmacyId);
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
    public List<StockDto> getAllStocks(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<StockEntity> stockEntities = stockRepository.findAllByPharmacyId(pharmacyId);
        return stockEntities.stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public StockDto getStockById(Long pharmacyId, UUID invId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<StockEntity> stockEntity = stockRepository.findByInvIdAndPharmacyId(invId, pharmacyId);

        if (stockEntity.isEmpty()) {
            throw new RuntimeException("Stock not found with ID: " + invId + " for pharmacy ID: " + pharmacyId);
        }
        return stockMapper.toDto(stockEntity.get());

    }

    @Transactional
    @Override
    public void deleteStock(Long pharmacyId, UUID invId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<StockEntity> stockEntity = stockRepository.findByInvIdAndPharmacyId(invId, pharmacyId);
        if (stockEntity.isEmpty()) {
            throw new RuntimeException("Stock not found with ID: " + invId + " for pharmacy ID: " + pharmacyId);
        }
        stockRepository.delete(stockEntity.get());

    }

    @Transactional
    @Override
    public List<StockItemDto> getStockByItemId(Long pharmacyId, UUID itemId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<StockItemEntity> stockItems =
                stockItemRepository.findByItemIdAndPharmacyId(itemId, pharmacyId);

        return stockItems.stream().map(stockItem -> new StockItemDto(
                stockItem.getStockId(),
                stockItem.getItemId(),
                stockItem.getBatchNo(),
                stockItem.getPackageQuantity(),
                stockItem.getExpiryDate(),
                stockItem.getFreeItem(),
                stockItem.getDiscountPercentage(),
                stockItem.getDiscountAmount(),
                stockItem.getPurchasePrice(),
                stockItem.getMrpSalePrice(),
                stockItem.getPurchasePricePerUnit(),
                stockItem.getMrpSalePricePerUnit(),
                stockItem.getGstPercentage(),
                stockItem.getGstAmount(),
                stockItem.getAmount(),
                stockItem.getPharmacyId(),
                stockItem.getCreatedBy(),
                stockItem.getCreatedDate(),
                stockItem.getModifiedBy(),
                stockItem.getModifiedDate()
        )).collect(Collectors.toList());
    }


    @Transactional
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

    @Override
    @Transactional
    public List<StockItemDto> getItemsBySupplierId(Long pharmacyId, UUID supplierId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<StockItemEntity> entities =
                stockItemRepository.findItemsBySupplierIdAndPharmacyId(supplierId, pharmacyId);

        return entities.stream()
                .map(stockMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public void confirmPayment(Long pharmacyId, UUID invId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        StockEntity stockEntity = stockRepository.findByInvIdAndPharmacyId(invId, pharmacyId)
                .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + invId +  " for pharmacy ID: " + pharmacyId));

        stockEntity.setPaymentStatus("Paid");
        stockEntity.setModifiedBy(user.getId());
        stockEntity.setModifiedDate(LocalDate.now());

        stockRepository.save(stockEntity);
    }

    @Transactional
    @Override
    public boolean isBillNoExists(UUID supplierId, Long pharmacyId, int year, String purchaseBillNo, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<String> results = stockRepository.findBillNoBySupplierIdYearAndPharmacy(
                supplierId, pharmacyId, year, purchaseBillNo
        );

        return !results.isEmpty();
    }

    @Transactional
    @Override
    public List<StockSummaryDto> getStocksByPaymentStatusAndSupplierAndPharmacy(String paymentStatus, UUID supplierId, Long pharmacyId, User user){
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }
        return stockRepository.findStockSummariesByPaymentStatusAndSupplierIdAndPharmacyId(
                paymentStatus, supplierId, pharmacyId
        );
    }

    @Transactional
    @Override
    public StockItemDto updateStockItem(User user, Long modifiedById, Long pharmacyId, UUID invId, UUID itemId, String batchNo, StockItemDto updatedItem) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to this pharmacy");
        }

        // 2. Fetch stock item with pharmacy restriction
        StockItemEntity stockItem = stockItemRepository
                .findByInvIdItemIdBatchNoAndPharmacyId(invId, itemId, batchNo, pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Stock item not found for this pharmacy"));

        // 3. Update stock item
        stockItem.setPurchasePricePerUnit(updatedItem.getPurchasePricePerUnit());
        stockItem.setMrpSalePricePerUnit(updatedItem.getMrpSalePricePerUnit());
        stockItem.setExpiryDate(updatedItem.getExpiryDate());
        stockItem.setModifiedBy(modifiedById);
        stockItem.setModifiedDate(LocalDate.now());

        stockItemRepository.save(stockItem);

        // 4. Update inventory entry
        InventoryDetailsEntity inventory = inventoryDetailsRepository
                .findByItemIdAndBatchNoAndPharmacyId(itemId, batchNo, pharmacyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory not found for this pharmacy"));

        inventory.setPurchasePricePerUnit(updatedItem.getPurchasePricePerUnit());
        inventory.setMrpSalePricePerUnit(updatedItem.getMrpSalePricePerUnit());
        inventory.setExpiryDate(updatedItem.getExpiryDate());
        inventory.setModifiedBy(modifiedById);
        inventory.setModifiedDate(LocalDate.now());

        inventoryDetailsRepository.save(inventory);

        return updatedItem;
    }

}
