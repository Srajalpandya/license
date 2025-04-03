package com.licence.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.licence.entity.License;
import com.licence.repository.LicenseRepository;


import java.util.Optional;
import java.util.UUID;
@Component
public class LicenseUtils {
    @Autowired
    private LicenseRepository licenseRepository;
    public String generateLicenseKey() {
        String key = UUID.randomUUID().toString();
        Optional<License> license = licenseRepository.findByLicenseKey(key);
        if(license.isPresent()){
            return generateLicenseKey();
        }
         return key;
    }
}
