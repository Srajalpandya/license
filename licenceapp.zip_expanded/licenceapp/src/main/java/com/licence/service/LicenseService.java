package com.licence.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.licence.entity.License;
import com.licence.repository.LicenseRepository;
import com.licence.utils.LicenseUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class LicenseService {

    private final LicenseRepository licenseRepository;

    @Autowired
    private LicenseUtils licenseUtils;

    public LicenseService(LicenseRepository licenseRepository) {
        this.licenseRepository = licenseRepository;
    }

    public License generateLicense(int validityDays, int maxUsage) {
        License license = new License();
        license.setLicenseKey(licenseUtils.generateLicenseKey());
        license.setExpiryDate(LocalDateTime.now().plusDays(validityDays));
        license.setMaxUsage(maxUsage);
        license.setCurrentUsage(0);
        license.setActive(true);
        return licenseRepository.save(license);
    }

    public boolean validateLicense(String licenseKey) {
        Optional<License> licenseOpt = licenseRepository.findByLicenseKey(licenseKey);
        if (licenseOpt.isPresent()) {
            License license = licenseOpt.get();
            if (license.isValid()) {
                license.setCurrentUsage(license.getCurrentUsage() + 1);
                licenseRepository.save(license);
                return true;
            }
        }
        return false;
    }

    public void deactivateLicense(String licenseKey) {
        Optional<License> licenseOpt = licenseRepository.findByLicenseKey(licenseKey);
        licenseOpt.ifPresent(license -> {
            license.setActive(false);
            licenseRepository.save(license);
        });
    }
}

