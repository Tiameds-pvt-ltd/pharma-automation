package com.pharma.service.impl;

import com.pharma.dto.BillReturnDto;
import com.pharma.entity.*;
import com.pharma.mapper.BillReturnMapper;
import com.pharma.repository.BillReturnRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.BillReturnService;
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
public class BillReturnServiceImpl implements BillReturnService {

    @Autowired
    private BillReturnRepository billReturnRepository;

    @Autowired
    private BillReturnMapper billReturnMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public BillReturnDto createBillReturn(BillReturnDto billReturnDto, User user) {
        user = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        BillReturnEntity billReturnEntity = billReturnMapper.toEntity(billReturnDto);
        billReturnEntity.setBillReturnId(UUID.randomUUID());
        billReturnEntity.setCreatedBy(user.getId());
        billReturnEntity.setCreatedDate(LocalDate.now());

        String newBillReturnId1 = generateBillReturnId1();
        billReturnEntity.setBillReturnId1(newBillReturnId1);

        if (billReturnEntity.getBillReturnItemEntities() != null) {
            for (BillReturnItemEntity item : billReturnEntity.getBillReturnItemEntities()) {
                item.setBillReturnItemId(UUID.randomUUID());
                item.setCreatedBy(user.getId());
                item.setCreatedDate(LocalDate.now());
                item.setBillReturnEntity(billReturnEntity);
            }
        }

        BillReturnEntity savedEntity = billReturnRepository.save(billReturnEntity);
        return billReturnMapper.toDto(savedEntity);
    }

    @Transactional
    @Override
    public List<BillReturnDto> getAllBillReturn(Long createdById) {
        List<BillReturnEntity> billReturnEntities = billReturnRepository.findAllByCreatedBy(createdById);
        return billReturnEntities.stream()
                .map(billReturnMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BillReturnDto getBillReturnById(Long createdById, UUID billReturnId) {
        Optional<BillReturnEntity> billReturnEntity = billReturnRepository.findByBillReturnIdAndCreatedBy(billReturnId, createdById);

        if (billReturnEntity.isEmpty()) {
            throw new RuntimeException("Bill Return not found with ID: " + billReturnId + " for user ID: " + createdById);
        }
        return billReturnMapper.toDto(billReturnEntity.get());
    }

    @Transactional
    @Override
    public void deleteBillReturn(Long createdById, UUID billReturnId) {
        Optional<BillReturnEntity> billReturnEntity = billReturnRepository.findByBillReturnIdAndCreatedBy(billReturnId, createdById);
        if (billReturnEntity.isEmpty()) {
            throw new RuntimeException("Bill Return not found with ID: " + billReturnEntity + " for user ID: " + createdById);
        }
        billReturnRepository.delete(billReturnEntity.get());
    }

    private String generateBillReturnId1() {
        String yearPart = String.valueOf(LocalDate.now().getYear());

        Optional<BillReturnEntity> latestBillReturnOpt = billReturnRepository.findLatestBillReturnForYear(yearPart);

        int newSequence = 1;
        if (latestBillReturnOpt.isPresent()) {
            String lastBillReturnId1 = latestBillReturnOpt.get().getBillReturnId1();
            String[] parts = lastBillReturnId1.split("-");

            try {
                if (parts.length == 3) {
                    newSequence = Integer.parseInt(parts[2]) + 1;
                }
            } catch (NumberFormatException e) {
                System.err.println("Error parsing Bill Return sequence: " + lastBillReturnId1);
            }
        }

        return String.format("BILLRTN-%s-%05d", yearPart, newSequence);
    }
}
