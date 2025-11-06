package com.users.microreports.controllers;

import com.users.microreports.dtos.ReportDTO;
import com.users.microreports.dtos.Response;
import com.users.microreports.enums.ReportStatus;
import com.users.microreports.services.ReportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportServiceImpl reportService;

    @PostMapping("/create")
    public ResponseEntity<Response<?>> createReport(@RequestBody ReportDTO reportDTO) {
        return ResponseEntity.ok(reportService.createReport(reportDTO));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('DAEMON', 'SUPER_ADMIN')")
    public ResponseEntity<Response<?>>getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/my-reports")
    @PreAuthorize("hasRole('DAEMON')")
    public ResponseEntity<Response<List<ReportDTO>>> getMyReports() {
        return ResponseEntity.ok(reportService.getMyReports());
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('DAEMON')")
    public ResponseEntity<Response<List<ReportDTO>>> getReportsByStatus(@RequestBody Map<String, String> body) {
        ReportStatus status = ReportStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(reportService.getReportsByStatus(status));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('DAEMON')")
    public ResponseEntity<Response<ReportDTO>> getReportById(@PathVariable ("id") Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @PutMapping("/update-status/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response<ReportDTO>> updateReport(@PathVariable ("id") Long id, @RequestBody ReportDTO reportDTO) {
        return ResponseEntity.ok(reportService.updateReport(id, reportDTO));
    }
}
