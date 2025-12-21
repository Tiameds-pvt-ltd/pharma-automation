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
import java.util.*;
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

        String newGrnNo = generateGrnNo(stockDto.getPharmacyId());
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
    private String generateGrnNo(Long pharmacyId) {

        // YY = last 2 digits of year
        String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);

        Optional<StockEntity> latestGrnOpt =
                stockRepository.findLatestGrnNoForYearAndPharmacy(
                        pharmacyId, yearPart
                );

        int nextSequence = 1;

        if (latestGrnOpt.isPresent()) {
            String lastGrnNo = latestGrnOpt.get().getGrnNo();
            // Example: GRN-25-09 or GRN-25-123
            String[] parts = lastGrnNo.split("-");

            if (parts.length == 3) {
                try {
                    nextSequence = Integer.parseInt(parts[2]) + 1;
                } catch (NumberFormatException ignored) {
                    nextSequence = 1;
                }
            }
        }

        // Pad only for 1–9
        String sequencePart = (nextSequence < 10)
                ? "0" + nextSequence
                : String.valueOf(nextSequence);

        return "GRN-" + yearPart + "-" + sequencePart;
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

    @Override
    @Transactional
    public StockDto updateStock(Long pharmacyId, UUID invId, StockDto updatedStock, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        StockEntity existingStock = stockRepository
                .findByInvIdAndPharmacyId(invId, pharmacyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Stock not found")
                );

        existingStock.setPurchaseBillNo(updatedStock.getPurchaseBillNo());
        existingStock.setPurchaseDate(updatedStock.getPurchaseDate());
        existingStock.setCreditPeriod(updatedStock.getCreditPeriod());
        existingStock.setPaymentDueDate(updatedStock.getPaymentDueDate());
        existingStock.setInvoiceAmount(updatedStock.getInvoiceAmount());
        existingStock.setTotalAmount(updatedStock.getTotalAmount());
        existingStock.setTotalCgst(updatedStock.getTotalCgst());
        existingStock.setTotalSgst(updatedStock.getTotalSgst());
        existingStock.setTotalDiscountPercentage(updatedStock.getTotalDiscountPercentage());
        existingStock.setTotalDiscountAmount(updatedStock.getTotalDiscountAmount());
        existingStock.setGrandTotal(updatedStock.getGrandTotal());
        existingStock.setPaymentStatus(updatedStock.getPaymentStatus());
        existingStock.setGoodStatus(updatedStock.getGoodStatus());
        existingStock.setGrnNo(updatedStock.getGrnNo());

        existingStock.setModifiedBy(user.getId());
        existingStock.setModifiedDate(LocalDate.now());

        Map<UUID, StockItemEntity> existingItemMap =
                existingStock.getStockItemEntities()
                        .stream()
                        .collect(Collectors.toMap(
                                StockItemEntity::getStockId,
                                i -> i
                        ));

        for (StockItemDto itemDto : updatedStock.getStockItemDtos()) {

            if (itemDto.getStockId() == null) {
                throw new RuntimeException("StockId is mandatory for update");
            }

            StockItemEntity itemEntity = existingItemMap.get(itemDto.getStockId());

            if (itemEntity == null) {
                throw new RuntimeException("Stock item not found");
            }

            // ❌ HARD RULES — IDENTITY MUST NOT CHANGE
            if (!Objects.equals(itemEntity.getBatchNo(), itemDto.getBatchNo())) {
                throw new RuntimeException("Batch number change is not allowed");
            }

            if (!Objects.equals(itemEntity.getItemId(), itemDto.getItemId())) {
                throw new RuntimeException("Item change is not allowed");
            }

            Long oldQty = itemEntity.getPackageQuantity();
            Long newQty = itemDto.getPackageQuantity();
            Long delta = newQty - oldQty;

            if (delta == 0) {
                continue; // No change
            }

            itemEntity.setPackageQuantity(newQty);
            itemEntity.setExpiryDate(itemDto.getExpiryDate());
            itemEntity.setFreeItem(itemDto.getFreeItem());
            itemEntity.setPurchasePrice(itemDto.getPurchasePrice());
            itemEntity.setMrpSalePrice(itemDto.getMrpSalePrice());
            itemEntity.setPurchasePricePerUnit(itemDto.getPurchasePricePerUnit());
            itemEntity.setMrpSalePricePerUnit(itemDto.getMrpSalePricePerUnit());
            itemEntity.setGstPercentage(itemDto.getGstPercentage());
            itemEntity.setGstAmount(itemDto.getGstAmount());
            itemEntity.setDiscountPercentage(itemDto.getDiscountPercentage());
            itemEntity.setDiscountAmount(itemDto.getDiscountAmount());
            itemEntity.setAmount(itemDto.getAmount());

            itemEntity.setModifiedBy(user.getId());
            itemEntity.setModifiedDate(LocalDate.now());

            stockItemRepository.save(itemEntity);

            InventoryEntity inventory = inventoryRepository
                    .findByItemIdAndPharmacyId(
                            itemEntity.getItemId(),
                            pharmacyId
                    )
                    .orElseThrow(() ->
                            new RuntimeException("Inventory not found")
                    );

            Long newInventoryQty = inventory.getPackageQuantity() + delta;

            if (newInventoryQty < 0) {
                throw new RuntimeException("Inventory cannot be negative");
            }

            inventory.setPackageQuantity(newInventoryQty);
            inventory.setModifiedBy(user.getId());
            inventory.setModifiedDate(LocalDate.now());

            inventoryRepository.save(inventory);

            InventoryDetailsEntity details =
                    inventoryDetailsRepository
                            .findByItemIdAndBatchNoAndPharmacyId(
                                    itemEntity.getItemId(),
                                    itemEntity.getBatchNo(),
                                    pharmacyId
                            )
                            .orElseThrow(() ->
                                    new RuntimeException("Inventory batch not found")
                            );

            Long newBatchQty = details.getPackageQuantity() + delta;

            if (newBatchQty < 0) {
                throw new RuntimeException("Batch quantity cannot be negative");
            }

            details.setPackageQuantity(newBatchQty);
            details.setExpiryDate(itemDto.getExpiryDate());
            details.setPurchasePrice(itemDto.getPurchasePrice());
            details.setMrpSalePrice(itemDto.getMrpSalePrice());
            details.setPurchasePricePerUnit(itemDto.getPurchasePricePerUnit());
            details.setMrpSalePricePerUnit(itemDto.getMrpSalePricePerUnit());
            details.setGstPercentage(itemDto.getGstPercentage());
            details.setGstAmount(itemDto.getGstAmount());

            details.setModifiedBy(user.getId());
            details.setModifiedDate(LocalDate.now());

            inventoryDetailsRepository.save(details);
        }

        StockEntity savedStock = stockRepository.save(existingStock);
        return stockMapper.toDto(savedStock);
    }
    }



