package com.licence.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class License {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licenseKey;
    private LocalDateTime expiryDate;
    private int maxUsage;
    private int currentUsage;
    private boolean active;

    public boolean isValid() {
        return LocalDateTime.now().isBefore(expiryDate) && currentUsage < maxUsage && active;
    }
}

