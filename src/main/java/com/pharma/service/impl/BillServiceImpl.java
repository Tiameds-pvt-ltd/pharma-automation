package com.pharma.service.impl;

import com.pharma.dto.*;
import com.pharma.entity.*;

import com.pharma.mapper.BillMapper;
import com.pharma.repository.*;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.BillService;
import com.pharma.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
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

    @Autowired
    private BillPaymentRepository billPaymentRepository;

    private boolean isValidPayment(BillPaymentEntity payment) {
        return payment.getPaymentType() != null
                && !payment.getPaymentType().isBlank()
                && payment.getPaymentAmount() != null
                && payment.getPaymentAmount().compareTo(BigDecimal.ZERO) > 0;
    }


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
                item.setPharmacyId(billDto.getPharmacyId());

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


//        if (billEntity.getBillPaymentEntities() != null &&
//                !billEntity.getBillPaymentEntities().isEmpty()) {
//
//            int sequence = generatePaymentId(billDto.getPharmacyId());
//            String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);
//
//            for (BillPaymentEntity payment : billEntity.getBillPaymentEntities()) {
//                String paymentId = "PAY-" + yearPart + "-" + String.format("%02d", sequence);
//
//                payment.setBillPaymentId(UUID.randomUUID());
//                payment.setPaymentId(paymentId);
//                payment.setPaymentDate(LocalDateTime.now());
//                payment.setCreatedBy(user.getId());
//                payment.setCreatedDate(LocalDate.now());
//                payment.setBillEntity(billEntity);
//
//                sequence++;
//            }
//        }
        if (billEntity.getBillPaymentEntities() != null) {

            int sequence = generatePaymentId(billDto.getPharmacyId());
            String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);

            Iterator<BillPaymentEntity> iterator =
                    billEntity.getBillPaymentEntities().iterator();

            while (iterator.hasNext()) {
                BillPaymentEntity payment = iterator.next();

                // ❌ REMOVE EMPTY / INVALID PAYMENTS
                if (!isValidPayment(payment)) {
                    iterator.remove();   // <-- THIS PREVENTS DB INSERT
                    continue;
                }

                // ✅ VALID PAYMENT → SAVE
                String paymentId = "PAY-" + yearPart + "-" + String.format("%02d", sequence);

                payment.setBillPaymentId(UUID.randomUUID());
                payment.setPaymentId(paymentId);
                payment.setPaymentDate(LocalDateTime.now());
                payment.setCreatedBy(user.getId());
                payment.setCreatedDate(LocalDate.now());
                payment.setBillEntity(billEntity);

                sequence++;
            }
        }

        BillEntity savedBill = billRepository.save(billEntity);
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

        int sequence = generatePaymentId(billPaymentDto.getPharmacyId());
        String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);

        if (payment.getBillPaymentId() == null) {
            payment.setBillPaymentId(UUID.randomUUID());
        }
        payment.setPaymentId("PAY-" + yearPart + "-" + String.format("%02d", sequence));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setCreatedBy(user.getId());
        payment.setCreatedDate(LocalDate.now());
        payment.setBillEntity(bill);
        payment.setPharmacyId(billPaymentDto.getPharmacyId());
        bill.getBillPaymentEntities().add(payment);

        BigDecimal currentBalance = bill.getBalanceDue();
        if (currentBalance == null) {
            currentBalance = bill.getGrandTotal();
        }

        BigDecimal newBalance = currentBalance.subtract(billPaymentDto.getPaymentAmount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            newBalance = BigDecimal.ZERO;
        }

        bill.setBalanceDue(newBalance);

        bill.setPaymentStatus(
                newBalance.compareTo(BigDecimal.ZERO) == 0 ? "Paid" : "Partial"
        );

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
        String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);
        Optional<BillEntity> latestBillOpt =
                billRepository.findLatestBillForYearAndPharmacy(
                        pharmacyId, yearPart
                );

        int nextSequence = 1;
        if (latestBillOpt.isPresent()) {
            String lastBillId1 = latestBillOpt.get().getBillId1();
            String[] parts = lastBillId1.split("-");

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


    @Transactional
    public int generatePaymentId(Long pharmacyId) {

        String yearPart = String.valueOf(LocalDate.now().getYear()).substring(2);

        Optional<BillPaymentEntity> latestPaymentOpt =
                billPaymentRepository.findLatestPaymentForYearAndPharmacyForUpdate(
                        pharmacyId, yearPart
                );

        if (latestPaymentOpt.isPresent()) {
            String lastPaymentId = latestPaymentOpt.get().getPaymentId();
            String[] parts = lastPaymentId.split("-");

            if (parts.length == 3) {
                try {
                    return Integer.parseInt(parts[2]) + 1;
                } catch (NumberFormatException ignored) {}
            }
        }

        return 1;
    }

    @Transactional
    @Override
    public List<BillDto> getBillsByPatientId(Long pharmacyId, UUID patientId, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        List<BillEntity> billEntities =
                billRepository.findAllByPatientIdAndPharmacyId(patientId, pharmacyId);

        return billEntities.stream()
                .map(billMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional
    @Override
    public List<BatchWiseProfitDto> getBatchWiseProfitBetweenDates(
            Long pharmacyId,
            LocalDate fromDate,
            LocalDate toDate,
            User user
    ) {

        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies()
                .stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        LocalDateTime fromDateTime = fromDate.atStartOfDay();
        LocalDateTime toDateTime   = toDate.plusDays(1).atStartOfDay();

        return billRepository.getBatchWiseProfitBetweenDates(
                fromDateTime,
                toDateTime,
                pharmacyId
        );
    }
}
