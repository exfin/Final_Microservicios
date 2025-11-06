package com.incentive.incentiveservice.dto;


import com.incentive.incentiveservice.enums.IncentiveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncentiveEventDTO {

    private long user;

    private long assignedBy;

    private IncentiveType type;

    private String name;

    private String description;

    private int points;

    private LocalDateTime createdAt;

}
