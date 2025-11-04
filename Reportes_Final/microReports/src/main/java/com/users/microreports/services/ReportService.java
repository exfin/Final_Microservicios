package com.users.microreports.services;

import com.users.microreports.dtos.ReportDTO;
import com.users.microreports.dtos.Response;
import com.users.microreports.enums.ReportStatus;

import java.util.List;

public interface ReportService {
    Response<?> createReport(ReportDTO reportDTO);

    Response<List<ReportDTO>> getAllReports();

    Response<List<ReportDTO>> getMyReports();

    Response<List<ReportDTO>> getReportsByStatus(ReportStatus status);

    Response<ReportDTO> getReportById(Long id);

    Response<ReportDTO> updateReport(Long id, ReportDTO reportDTO);
}
