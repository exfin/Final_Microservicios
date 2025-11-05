package com.victims.victimsservice.model;

import com.victims.victimsservice.enums.RiskLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "victims")
@AllArgsConstructor
@NoArgsConstructor
public class Victim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String alias;

    private String notes;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    private boolean active;

    @Column(nullable = false)
    private long createdBy;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

