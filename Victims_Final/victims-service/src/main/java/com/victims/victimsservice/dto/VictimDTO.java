package com.victims.victimsservice.dto;

import com.victims.victimsservice.enums.RiskLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VictimDTO {

    private String code;

    private String alias;

    private String notes;

    private RiskLevel riskLevel;

    private boolean active;

    private Long createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}