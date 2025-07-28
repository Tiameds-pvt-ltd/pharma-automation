package com.pharma.utils;

import com.pharma.entity.Pharmacy;
import com.pharma.repository.PharmacyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhramacyAccessableFilter {

    @Autowired
    private final PharmacyRepository pharmacyRepository;

    public PhramacyAccessableFilter(PharmacyRepository pharmacyRepository) {
        this.pharmacyRepository = pharmacyRepository;
    }

    /**
     * Checks if a lab is accessible by ensuring it exists and is active.
     *
     * @param pharmacyId the ID of the lab
     * @return true if the lab exists and is active, false otherwise
     */
    public boolean isPharmacyAccessible(Long pharmacyId) {
        return pharmacyRepository.findById(pharmacyId)
                .filter(Pharmacy::getIsActive)
                .isPresent();
    }
}
