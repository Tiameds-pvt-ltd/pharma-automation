package com.pharma.service.impl;

import com.pharma.dto.SupplierPaymentDto;
import com.pharma.entity.*;
import com.pharma.mapper.SupplierPaymentMapper;
import com.pharma.repository.PurchaseReturnRepository;
import com.pharma.repository.StockRepository;
import com.pharma.repository.SupplierPaymentRepo;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.SupplierPaymentService;
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
public class SupplierPaymentServiceImpl implements SupplierPaymentService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SupplierPaymentMapper supplierPaymentMapper;

    @Autowired
    private SupplierPaymentRepo supplierPaymentRepo;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private PurchaseReturnRepository purchaseReturnRepository;

    @Transactional
    @Override
    public SupplierPaymentDto saveSupplierPayment(SupplierPaymentDto supplierPaymentDto, User user) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        SupplierPaymentEntity supplierPaymentEntity = supplierPaymentMapper.toEntity(supplierPaymentDto);
        supplierPaymentEntity.setPaymentId(UUID.randomUUID());
        supplierPaymentEntity.setCreatedBy(user.getId());
        supplierPaymentEntity.setCreatedDate(LocalDate.now());

        if (supplierPaymentEntity.getSupplierPaymentDetailsEntities() != null) {
            for (SupplierPaymentDetailsEntity supplierPaymentDetails : supplierPaymentEntity.getSupplierPaymentDetailsEntities()) {
                supplierPaymentDetails.setPaymentDetailsId(UUID.randomUUID());
                supplierPaymentDetails.setCreatedBy(user.getId());
                supplierPaymentDetails.setCreatedDate(LocalDate.now());
                supplierPaymentDetails.setSupplierPaymentEntity(supplierPaymentEntity);
            }
        }

        SupplierPaymentEntity savedEntity = supplierPaymentRepo.saveAndFlush(supplierPaymentEntity);

        if (savedEntity.getSupplierPaymentDetailsEntities() != null) {
            for (SupplierPaymentDetailsEntity supplierPaymentDetails : savedEntity.getSupplierPaymentDetailsEntities()) {
                if (supplierPaymentDetails.getInvId() != null) {
                    StockEntity stockEntity = stockRepository.findById(supplierPaymentDetails.getInvId())
                            .orElseThrow(() -> new RuntimeException("Stock not found with ID: " + supplierPaymentDetails.getInvId()));

                    stockEntity.setPaymentStatus("Paid");
                    stockEntity.setModifiedBy(user.getId());
                    stockEntity.setModifiedDate(LocalDate.now());

                    stockRepository.save(stockEntity);
                }
            }
        }

        stockRepository.flush();

        return supplierPaymentMapper.toDto(savedEntity);
    }


    @Transactional
    @Override
    public List<SupplierPaymentDto> getAllSupplierPayment(Long createdById) {
        List<SupplierPaymentEntity> supplierPaymentEntities = supplierPaymentRepo.findAllByCreatedBy(createdById);
        return supplierPaymentEntities.stream()
                .map(supplierPaymentMapper::toDto)
                .collect(Collectors.toList());    }

    @Transactional
    @Override
    public SupplierPaymentDto getSupplierPaymentById(Long createdById, UUID paymentId) {
        Optional<SupplierPaymentEntity> supplierPaymentEntity = supplierPaymentRepo.findByPaymentIdAndCreatedBy(paymentId, createdById);

        if (supplierPaymentEntity.isEmpty()) {
            throw new RuntimeException("Supplier Payment not found with ID: " + paymentId + " for user ID: " + createdById);
        }
        return supplierPaymentMapper.toDto(supplierPaymentEntity.get());
    }
}
