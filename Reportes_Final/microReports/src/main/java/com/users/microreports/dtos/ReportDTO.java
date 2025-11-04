package com.users.microreports.dtos;

import com.users.microreports.enums.ReportSeverity;
import com.users.microreports.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    private Long id;

    private boolean anonymous;

    private Long createdBy;

    private String ipHash;

    private String title;

    private String description;

    private ReportSeverity severity;

    private Long victimId;

    private ReportStatus status;

    private Long reviewedById;

    private String reviewNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
