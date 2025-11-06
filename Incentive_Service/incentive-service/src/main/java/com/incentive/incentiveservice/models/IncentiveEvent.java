package com.incentive.incentiveservice.models;

import com.incentive.incentiveservice.enums.IncentiveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "incentive_events")
@AllArgsConstructor
@NoArgsConstructor
public class IncentiveEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long user;

    @Column(nullable = false)
    private Long assignedBy;

    @Enumerated(EnumType.STRING)
    private IncentiveType type;

    private String name;

    @Column(columnDefinition = "text")
    private String description;

    private int points;

    private LocalDateTime createdAt;
}
