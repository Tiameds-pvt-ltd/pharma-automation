package com.pharma.service.impl;

import com.pharma.dto.PurchaseOrderDto;
import com.pharma.entity.PurchaseOrderEntity;
import com.pharma.entity.PurchaseOrderItemEntity;
import com.pharma.entity.User;
import com.pharma.mapper.PurchaseOrderMapper;
import com.pharma.repository.PurchaseOrderRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.PurchaseOrderService;
import com.pharma.utils.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        PurchaseOrderEntity purchaseOrderEntity = purchaseOrderMapper.toEntity(purchaseOrderDto);
        purchaseOrderEntity.setOrderId(UUID.randomUUID());
        purchaseOrderEntity.setCreatedBy(user.getId());
        purchaseOrderEntity.setCreatedDate(LocalDate.now());

        String newOrderId1 = generateOrderId1();
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
    public List<PurchaseOrderDto> getAllPurchaseOrders(Long createdById) {
        List<PurchaseOrderEntity> purchaseOrders = purchaseOrderRepository.findAllByCreatedBy(createdById);
        return purchaseOrders.stream()
                .map(purchaseOrderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PurchaseOrderDto getPurchaseOrderById(Long createdById, UUID orderId) {
        Optional<PurchaseOrderEntity> purchaseOrderEntity = purchaseOrderRepository.findByOrderIdAndCreatedBy(orderId, createdById);

        if (purchaseOrderEntity.isEmpty()) {
            throw new RuntimeException("Purchase order not found with ID: " + orderId + " for user ID: " + createdById);
        }
        return purchaseOrderMapper.toDto(purchaseOrderEntity.get());
    }


    @Override
    @Transactional
    public void deletePurchaseOrderById(Long createdById, UUID orderId) {
        Optional<PurchaseOrderEntity> purchaseOrderEntity = purchaseOrderRepository.findByOrderIdAndCreatedBy(orderId, createdById);
        if (purchaseOrderEntity.isEmpty()) {
            throw new RuntimeException("Purchase order not found with ID: " + orderId + " for user ID: " + createdById);
        }
        purchaseOrderRepository.delete(purchaseOrderEntity.get());
    }


    private String generateOrderId1() {
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        Optional<PurchaseOrderEntity> latestOrderOpt = purchaseOrderRepository.findLatestOrderForToday(datePart);

        int newSequence = 1;
        if (latestOrderOpt.isPresent()) {
            String lastOrderId1 = latestOrderOpt.get().getOrderId1();
            String[] parts = lastOrderId1.split("-");

            try {
                if (parts.length == 3) {
                    newSequence = Integer.parseInt(parts[2]) + 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing order sequence: " + lastOrderId1);
            }
        }
        return String.format("ORD-%s-%05d", datePart, newSequence);
    }


}
