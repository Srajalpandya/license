package com.licence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.licence.entity.License;

import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, Long> {
    Optional<License> findByLicenseKey(String licenseKey);
}

