package com.users.microreports.models;

import com.users.microreports.enums.ReportSeverity;
import com.users.microreports.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "reports")
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean anonymous;

    @Column(nullable = false)
    private Long createdById;

    private String ipHash;

    @Column(nullable = false )
    private String title;

    @Column(nullable = false )
    private String description;

    @Enumerated(EnumType.STRING)
    private ReportSeverity severity;

    @Column(nullable = false )
    private long victimId;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Column(nullable = false )
    private Long reviewedById;

    @Column(columnDefinition = "text")
    private String reviewNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
