package com.licence.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.licence.entity.License;
import com.licence.service.LicenseService;

@RestController
@RequestMapping("/api/license")
public class LicenseController {

    private final LicenseService  licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @PostMapping("/generate")
    public ResponseEntity<License> generateLicense(@RequestParam int validityDays, @RequestParam int maxUsage) {
        return ResponseEntity.ok(licenseService.generateLicense(validityDays, maxUsage));
    }

    @GetMapping("/validate/{licenseKey}")
    public ResponseEntity<Boolean> validateLicense(@PathVariable String licenseKey) {
    	System.out.println("");
        return ResponseEntity.ok(licenseService.validateLicense(licenseKey));
    }
    @PostMapping("/deactivate/{licenseKey}")
    public ResponseEntity<String> deactivateLicense(@PathVariable String licenseKey) {
        licenseService.deactivateLicense(licenseKey);
        return ResponseEntity.ok("License deactivated successfully.");
    }
}
