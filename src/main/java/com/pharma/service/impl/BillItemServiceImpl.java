package com.pharma.service.impl;

import com.pharma.dto.GstSlabNetPayableDto;
import com.pharma.dto.ItemProfitByDoctorDto;
import com.pharma.dto.ItemProfitRowDto;
import com.pharma.entity.User;
import com.pharma.repository.BillItemRepository;
import com.pharma.repository.auth.UserRepository;
import com.pharma.service.BillItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
