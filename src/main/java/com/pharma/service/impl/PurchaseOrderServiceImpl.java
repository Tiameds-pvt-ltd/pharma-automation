package com.pharma.service.impl;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.dto.PurchaseOrderItemDto;
import com.pharma.entity.PurchaseOrderEntity;
import com.pharma.entity.PurchaseOrderItemEntity;
import com.pharma.entity.User;
import com.pharma.exception.ResourceNotFoundException;
import com.pharma.mapper.PurchaseOrderMapper;
import com.pharma.repository.PurchaseOrderRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.PurchaseOrderService;
import com.pharma.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private PurchaseOrderMapper purchaseOrderMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public PurchaseOrderDto savePurchaseOrder(PurchaseOrderDto purchaseOrderDto, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(purchaseOrderDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        PurchaseOrderEntity purchaseOrderEntity = purchaseOrderMapper.toEntity(purchaseOrderDto);
        purchaseOrderEntity.setOrderId(UUID.randomUUID());
        purchaseOrderEntity.setCreatedBy(user.getId());
        purchaseOrderEntity.setCreatedDate(LocalDate.now());

        purchaseOrderEntity.setPharmacyId(purchaseOrderDto.getPharmacyId());

        String newOrderId1 = generateOrderId1(purchaseOrderDto.getPharmacyId());
        purchaseOrderEntity.setOrderId1(newOrderId1);

        if (purchaseOrderEntity.getPurchaseOrderItemEntities() != null) {
            for (PurchaseOrderItemEntity item : purchaseOrderEntity.getPurchaseOrderItemEntities()) {
                item.setOrderItemId(UUID.randomUUID());
                item.setCreatedBy(user.getId());
                item.setCreatedDate(LocalDate.now());
                item.setPurchaseOrderEntity(purchaseOrderEntity);
            }
        }

        PurchaseOrderEntity savedEntity = purchaseOrderRepository.save(purchaseOrderEntity);
        return purchaseOrderMapper.toDto(savedEntity);
    }

    @Override
    @Transactional
    public List<PurchaseOrderDto> getAllPurchaseOrders(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<PurchaseOrderEntity> purchaseOrders = purchaseOrderRepository.findAllByPharmacyId(pharmacyId);
        return purchaseOrders.stream()
                .map(purchaseOrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseOrderDto getPurchaseOrderById(Long pharmacyId, UUID orderId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<PurchaseOrderEntity> purchaseOrderEntity = purchaseOrderRepository.findByOrderIdAndPharmacyId(orderId, pharmacyId);

        if (purchaseOrderEntity.isEmpty()) {
            throw new RuntimeException("Purchase order not found with ID: " + orderId + " for pharmacy ID: " + pharmacyId);
        }
        return purchaseOrderMapper.toDto(purchaseOrderEntity.get());
    }


    @Override
    @Transactional
    public void deletePurchaseOrderById(Long pharmacyId, UUID orderId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<PurchaseOrderEntity> purchaseOrderEntity = purchaseOrderRepository.findByOrderIdAndPharmacyId(orderId, pharmacyId);
        if (purchaseOrderEntity.isEmpty()) {
            throw new RuntimeException("Purchase order not found with ID: " + orderId + " for pharmacy ID: " + pharmacyId);
        }
        purchaseOrderRepository.delete(purchaseOrderEntity.get());
    }

    @Transactional
    public String generateOrderId1(Long pharmacyId) {

        // YY = last 2 digits of year
        String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);

        Optional<PurchaseOrderEntity> latestOrderOpt =
                purchaseOrderRepository.findLatestOrderForYearAndPharmacy(
                        pharmacyId, yearPart
                );

        int nextSequence = 1;

        if (latestOrderOpt.isPresent()) {
            String lastOrderId = latestOrderOpt.get().getOrderId1();
            // Example: ORD-25-09 or ORD-25-123
            String[] parts = lastOrderId.split("-");

            if (parts.length == 3) {
                try {
                    nextSequence = Integer.parseInt(parts[2]) + 1;
                } catch (NumberFormatException ignored) {
                    nextSequence = 1;
                }
            }
        }

        String sequencePart = (nextSequence < 10)
                ? "0" + nextSequence
                : String.valueOf(nextSequence);

        return "ORD-" + yearPart + "-" + sequencePart;
    }


    @Override
    @Transactional
    public PurchaseOrderDto updatePurchaseOrder(Long pharmacyId, UUID orderId, PurchaseOrderDto updatedPurchaseOrder, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        PurchaseOrderEntity existingOrder = purchaseOrderRepository
                .findByOrderIdAndPharmacyId(orderId, pharmacyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Purchase order not found with ID: " + orderId
                        ));

        existingOrder.setOrderedDate(updatedPurchaseOrder.getOrderedDate());
        existingOrder.setIntendedDeliveryDate(updatedPurchaseOrder.getIntendedDeliveryDate());
        existingOrder.setTotalAmount(updatedPurchaseOrder.getTotalAmount());
        existingOrder.setTotalGst(updatedPurchaseOrder.getTotalGst());
        existingOrder.setGrandTotal(updatedPurchaseOrder.getGrandTotal());

        existingOrder.setModifiedBy(user.getId());
        existingOrder.setModifiedDate(LocalDate.now());

        Map<UUID, PurchaseOrderItemEntity> existingItemMap =
                existingOrder.getPurchaseOrderItemEntities()
                        .stream()
                        .filter(i -> i.getOrderItemId() != null)
                        .collect(Collectors.toMap(
                                PurchaseOrderItemEntity::getOrderItemId,
                                i -> i
                        ));

        List<PurchaseOrderItemEntity> updatedItems = new ArrayList<>();

        for (PurchaseOrderItemDto itemDto : updatedPurchaseOrder.getPurchaseOrderItemDtos()) {

            PurchaseOrderItemEntity itemEntity;

            if (itemDto.getOrderItemId() != null &&
                    existingItemMap.containsKey(itemDto.getOrderItemId())) {

                itemEntity = existingItemMap.get(itemDto.getOrderItemId());

            }
            else {
                itemEntity = new PurchaseOrderItemEntity();
                itemEntity.setOrderItemId(UUID.randomUUID());
                itemEntity.setCreatedBy(user.getId());
                itemEntity.setCreatedDate(LocalDate.now());
                itemEntity.setPurchaseOrderEntity(existingOrder);
            }

            itemEntity.setItemId(itemDto.getItemId());
            itemEntity.setPackageQuantity(itemDto.getPackageQuantity());
            itemEntity.setManufacturer(itemDto.getManufacturer());
            itemEntity.setGstPercentage(itemDto.getGstPercentage());
            itemEntity.setGstAmount(itemDto.getGstAmount());
            itemEntity.setAmount(itemDto.getAmount());
            itemEntity.setModifiedBy(user.getId());
            itemEntity.setModifiedDate(LocalDate.now());

            updatedItems.add(itemEntity);
        }

        existingOrder.getPurchaseOrderItemEntities().clear();
        existingOrder.getPurchaseOrderItemEntities().addAll(updatedItems);

        PurchaseOrderEntity savedOrder =
                purchaseOrderRepository.save(existingOrder);

        return purchaseOrderMapper.toDto(savedOrder);
    }
}
