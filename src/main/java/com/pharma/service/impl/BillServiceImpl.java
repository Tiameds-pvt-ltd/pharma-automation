package com.pharma.service.impl;

import com.pharma.dto.BillDto;
import com.pharma.entity.*;

import com.pharma.mapper.BillMapper;
import com.pharma.repository.BillRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.BillService;
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
public class BillServiceImpl implements BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public BillDto createBill(BillDto billDto, User user) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BillEntity billEntity = billMapper.toEntity(billDto);
        billEntity.setBillId(UUID.randomUUID());
        billEntity.setCreatedBy(user.getId());
        billEntity.setCreatedDate(LocalDate.now());

        String newBillId1 = generateBillId1();
        billEntity.setBillId1(newBillId1);

        if (billEntity.getBillItemEntities() != null) {
            for (BillItemEntity item : billEntity.getBillItemEntities()) {
                item.setItemId(UUID.randomUUID());
                item.setCreatedBy(user.getId());
                item.setCreatedDate(LocalDate.now());
                item.setBillEntity(billEntity);
            }
        }

        BillEntity savedBill = billRepository.save(billEntity);
        return billMapper.toDto(savedBill);
    }

    @Transactional
    @Override
    public List<BillDto> getAllBill(Long createdById) {
        List<BillEntity> billEntities = billRepository.findAllByCreatedBy(createdById);
        return billEntities.stream()
                .map(billMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BillDto getBillById(Long createdById, UUID billId) {
        Optional<BillEntity> billEntity = billRepository.findByBillIdAndCreatedBy(billId, createdById);

        if (billEntity.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + billId + " for user ID: " + createdById);
        }
        return billMapper.toDto(billEntity.get());
    }

    @Transactional
    @Override
    public void deleteBill(Long createdById, UUID billId) {
        Optional<BillEntity> billEntity = billRepository.findByBillIdAndCreatedBy(billId, createdById);
        if (billEntity.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + billId + " for user ID: " + createdById);
        }
        billRepository.delete(billEntity.get());
    }


    private String generateBillId1() {
        String yearPart = String.valueOf(LocalDate.now().getYear());

        Optional<BillEntity> latestBillOpt = billRepository.findLatestBillForYear(yearPart);

        int newSequence = 1;
        if (latestBillOpt.isPresent()) {
            String lastBillId1 = latestBillOpt.get().getBillId1();
            String[] parts = lastBillId1.split("-");

            try {
                if (parts.length == 3) {
                    newSequence = Integer.parseInt(parts[2]) + 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing Bill sequence: " + lastBillId1);
            }
        }

        return String.format("BILL-%s-%05d", yearPart, newSequence);
    }
}
