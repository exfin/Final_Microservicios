package com.users.microreports.dao;

import com.users.microreports.enums.ReportStatus;
import com.users.microreports.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepo extends JpaRepository<Report, Long> {
    List<Report> findByCreatedByIdOrderByCreatedAtDesc(Long userId);

    List<Report> findByReviewedByIdOrderByCreatedAtDesc(Long reviewerId);

    List<Report> findByStatusOrderByCreatedAtDesc(ReportStatus status);
}
