package com.pharma.service.impl;

import com.pharma.dto.*;
import com.pharma.entity.*;

import com.pharma.mapper.BillMapper;
import com.pharma.repository.BillRepository;
import com.pharma.repository.InventoryDetailsRepository;
import com.pharma.repository.InventoryRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.BillService;
import com.pharma.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryDetailsRepository inventoryDetailsRepository;

    @Transactional
    @Override
    public BillDto createBill(BillDto billDto, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(billDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        BillEntity billEntity = billMapper.toEntity(billDto);
        billEntity.setBillId(UUID.randomUUID());
        billEntity.setCreatedBy(user.getId());
        billEntity.setCreatedDate(LocalDate.now());

        billEntity.setPharmacyId(billDto.getPharmacyId());

        String newBillId1 = generateBillId1(billDto.getPharmacyId());
        billEntity.setBillId1(newBillId1);

        if (billEntity.getBillItemEntities() != null) {
            for (BillItemEntity item : billEntity.getBillItemEntities()) {
                item.setBillItemId(UUID.randomUUID());
                item.setCreatedBy(user.getId());
                item.setCreatedDate(LocalDate.now());
                item.setBillEntity(billEntity);

                Optional<InventoryEntity> inventoryOpt = inventoryRepository.findByItemId(item.getItemId());
                if (inventoryOpt.isPresent()) {
                    InventoryEntity inventory = inventoryOpt.get();

                    synchronized (this) {
                        Long newQty = inventory.getPackageQuantity() - item.getPackageQuantity();
                        if (newQty < 0) {
                            throw new RuntimeException("Insufficient stock in inventory for item ID: " + item.getItemId());
                        }
                        inventory.setPackageQuantity(newQty);
                        inventory.setModifiedBy(user.getId());
                        inventory.setModifiedDate(LocalDate.now());
                        inventoryRepository.save(inventory);
                    }
                } else {
                    throw new RuntimeException("Inventory not found for item ID: " + item.getItemId());
                }

                Optional<InventoryDetailsEntity> detailsOpt = inventoryDetailsRepository
                        .findByItemIdAndBatchNo(item.getItemId(), item.getBatchNo());

                if (detailsOpt.isPresent()) {
                    InventoryDetailsEntity details = detailsOpt.get();
                    Long newQty = details.getPackageQuantity() - item.getPackageQuantity();
                    if (newQty < 0) {
                        throw new RuntimeException("Insufficient batch stock for item ID: " + item.getItemId() +
                                ", Batch No: " + item.getBatchNo());
                    }
                    details.setPackageQuantity(newQty);
                    details.setModifiedBy(user.getId());
                    details.setModifiedDate(LocalDate.now());
                    inventoryDetailsRepository.save(details);
                } else {
                    throw new RuntimeException("Inventory details not found for item ID: " + item.getItemId() +
                            ", Batch No: " + item.getBatchNo());
                }
            }
        }

        BillEntity savedBill = billRepository.save(billEntity);

        if ("Paid".equalsIgnoreCase(savedBill.getPaymentStatus()) &&
                savedBill.getBillPaymentEntities() != null) {

            for (BillPaymentEntity payment : savedBill.getBillPaymentEntities()) {
                payment.setBillPaymentId(UUID.randomUUID());
                payment.setCreatedBy(user.getId());
                payment.setCreatedDate(LocalDate.now());
                payment.setBillEntity(savedBill);
            }
        }

        return billMapper.toDto(savedBill);
    }

    @Override
    @Transactional
    public BillDto addBillPayment(BillPaymentDto billPaymentDto, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(billPaymentDto.getPharmacyId()));

        if (!isMember) {
            throw new RuntimeException("User does not belong to selected pharmacy");
        }

        BillEntity bill = billRepository.findById(billPaymentDto.getBillId())
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        BillPaymentEntity payment = billMapper.toEntityPayment(billPaymentDto);

        if (payment.getBillPaymentId() == null) {
            payment.setBillPaymentId(UUID.randomUUID());
        }
        payment.setBillPaymentDate(LocalDate.now());
        payment.setCreatedBy(user.getId());
        payment.setCreatedDate(LocalDate.now());
        payment.setBillEntity(bill);

        payment.setPharmacyId(billPaymentDto.getPharmacyId());

        bill.getBillPaymentEntities().add(payment);

        BillEntity updatedBill = billRepository.save(bill);

        return billMapper.toDto(updatedBill);
    }


    @Transactional
    @Override
    public List<BillDto> getAllBill(Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<BillEntity> billEntities = billRepository.findAllByPharmacyId(pharmacyId);
        return billEntities.stream()
                .map(billMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public BillDto getBillById(Long pharmacyId, UUID billId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<BillEntity> billEntity = billRepository.findByBillIdAndPharmacyId(billId, pharmacyId);

        if (billEntity.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + billId + " for pharmacy ID: " + pharmacyId);
        }
        return billMapper.toDto(billEntity.get());
    }

    @Transactional
    @Override
    public void deleteBill(Long pharmacyId, UUID billId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));
        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Optional<BillEntity> billEntity = billRepository.findByBillIdAndPharmacyId(billId, pharmacyId);
        if (billEntity.isEmpty()) {
            throw new RuntimeException("Bill not found with ID: " + billId + " for pharmacy ID: " + pharmacyId);
        }
        billRepository.delete(billEntity.get());
    }
    @Transactional
    private String generateBillId1(Long pharmacyId) {

        // YY = last 2 digits of year
        String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);

        Optional<BillEntity> latestBillOpt =
                billRepository.findLatestBillForYearAndPharmacy(
                        pharmacyId, yearPart
                );

        int nextSequence = 1;

        if (latestBillOpt.isPresent()) {
            String lastBillId1 = latestBillOpt.get().getBillId1();
            // Example: BILL-25-09 or BILL-25-123
            String[] parts = lastBillId1.split("-");

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

        return "BILL-" + yearPart + "-" + sequencePart;
    }



    @Transactional
    @Override
    public PackageQuantityDto getPackageQuantityDifference(UUID itemId, String batchNo, Long pharmacyId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        Object result = billRepository.getPackageQuantityRaw(itemId, batchNo, pharmacyId);

        if (result == null) {
            return new PackageQuantityDto(0L);
        }

        Long quantity;
        if (result instanceof Number) {
            quantity = ((Number) result).longValue();
        } else {
            quantity = Long.parseLong(result.toString());
        }

        return new PackageQuantityDto(quantity);
    }

    @Transactional
    @Override
    public BillingSummaryDto getSummaryByDate(Long pharmacyId, LocalDate selectedDate, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }
        return billRepository.getBillingSummaryByDateAndPharmacy(selectedDate, pharmacyId);
    }


    @Transactional
    @Override
    public PaymentSummaryDto getPaymentSummaryByDate(Long pharmacyId, LocalDate selectedDate, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        return billRepository.getPaymentSummaryByDateAndPharmacy(selectedDate, pharmacyId);
    }

    @Transactional
    @Override
    public List<BillingGstSummaryDto> getBillingGstSummary(Long pharmacyId, LocalDate inputDate, String inputMonth, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        if (inputDate != null) {
            return billRepository.getBillingGstSummaryByDate(pharmacyId, inputDate);
        } else if (inputMonth != null && !inputMonth.isBlank()) {
            return billRepository.getBillingGstSummaryByMonth(pharmacyId, inputMonth);
        }

        return List.of();
    }

}
