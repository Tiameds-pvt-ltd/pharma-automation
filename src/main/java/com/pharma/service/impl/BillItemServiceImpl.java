package com.pharma.service.impl;

import com.pharma.dto.*;
import com.pharma.entity.User;
import com.pharma.repository.BillItemRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.BillItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BillItemServiceImpl implements BillItemService {

    private final BillItemRepository billItemRepository;
    private final UserRepository userRepository;

    @Override
    public List<ItemProfitRowDto> getItemProfitByMonth(UUID itemId, Long pharmacyId, String monthYear, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        YearMonth yearMonth = YearMonth.parse(
                monthYear,
                DateTimeFormatter.ofPattern("MM-yyyy")
        );

        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate   = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

        return billItemRepository.findItemProfitDetailsByMonth(
                itemId,
                pharmacyId,
                startDate,
                endDate
        );
    }


    @Override
    public List<GstSlabNetPayableDto> getNetGstSlabWise(Long pharmacyId, String monthYear, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        YearMonth yearMonth = YearMonth.parse(
                monthYear,
                DateTimeFormatter.ofPattern("MM-yyyy")
        );

        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate   = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

        return billItemRepository.findNetGstSlabWise(
                pharmacyId,
                startDate,
                endDate
        );
    }

    @Override
    public List<ItemProfitByDoctorDto> getDoctorWiseItemProfit(UUID doctorId, Long pharmacyId, String monthYear, User user) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        YearMonth yearMonth = YearMonth.parse(
                monthYear,
                DateTimeFormatter.ofPattern("MM-yyyy")
        );

        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate   = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

        return billItemRepository.findDoctorWiseItemProfit(
                doctorId,
                pharmacyId,
                startDate,
                endDate
        );
    }


    @Override
    public List<DailySalesCostProfitDto> getDailySalesCostProfit(
            Long pharmacyId,
            String monthYear,
            User user
    ) {
        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        YearMonth yearMonth = YearMonth.parse(
                monthYear,
                DateTimeFormatter.ofPattern("MM-yyyy")
        );

        LocalDateTime startDate = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDate   = yearMonth.plusMonths(1).atDay(1).atStartOfDay();

        return billItemRepository.findDailySalesCostProfitPharmacyWise(
                pharmacyId,
                startDate,
                endDate
        );
    }


    @Override
    public List<ItemProfitSummaryDto> getItemWiseProfitSummary(
            Long pharmacyId,
            String monthYear,
            String dateRange,
            User user
    ) {

        User persistentUser = userRepository.findByIdFetchPharmacies(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isMember = persistentUser.getPharmacies().stream()
                .anyMatch(p -> p.getPharmacyId().equals(pharmacyId));

        if (!isMember) {
            throw new RuntimeException("User does not belong to the selected pharmacy");
        }

        LocalDateTime startDate;
        LocalDateTime endDate;

        if (monthYear != null && !monthYear.isBlank()) {

            YearMonth yearMonth = YearMonth.parse(
                    monthYear,
                    DateTimeFormatter.ofPattern("MM-yyyy")
            );

            startDate = yearMonth.atDay(1).atStartOfDay();
            endDate   = yearMonth.plusMonths(1).atDay(1).atStartOfDay();
        }
        else if (dateRange != null && !dateRange.isBlank()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String[] parts = dateRange.split(" - ");

            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid dateRange format. Use dd-MM-yyyy - dd-MM-yyyy");
            }

            startDate = LocalDate.parse(parts[0], formatter).atStartOfDay();
            endDate   = LocalDate.parse(parts[1], formatter).plusDays(1).atStartOfDay();
        }
        else {
            throw new IllegalArgumentException("Either monthYear or dateRange must be provided");
        }

        return billItemRepository.findItemProfitSummary(
                pharmacyId,
                startDate,
                endDate
        );
    }


}
