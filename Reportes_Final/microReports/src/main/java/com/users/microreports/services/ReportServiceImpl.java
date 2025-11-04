package com.users.microreports.services;

import com.users.microreports.dao.ReportRepo;
import com.users.microreports.dtos.CustomUserPrincipal;
import com.users.microreports.dtos.ReportDTO;
import com.users.microreports.dtos.Response;
import com.users.microreports.enums.ReportSeverity;
import com.users.microreports.enums.ReportStatus;
import com.users.microreports.exceptions.BadRequestException;
import com.users.microreports.exceptions.NotFoundException;
import com.users.microreports.models.Report;
import com.users.microreports.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepo reportRepo;
    public final JwtUtil jwtUtil;


    @Override
    public Response<?> createReport(ReportDTO reportDTO) {
        if (reportDTO.getTitle() == null || reportDTO.getDescription() == null) {
            throw new BadRequestException("El título y la descripción son obligatorios");
        }

        Report report = new Report();
        report.setAnonymous(reportDTO.isAnonymous());
        report.setCreatedById(reportDTO.getCreatedBy());
        report.setIpHash(reportDTO.getIpHash());
        report.setTitle(reportDTO.getTitle());
        report.setDescription(reportDTO.getDescription());
        report.setSeverity(reportDTO.getSeverity() != null ? reportDTO.getSeverity() : ReportSeverity.MEDIUM);
        report.setVictimId(reportDTO.getVictimId());
        report.setStatus(ReportStatus.NEW);
        report.setCreatedAt(LocalDateTime.now());
        report.setUpdatedAt(LocalDateTime.now());

        Report saved = reportRepo.save(report);

        return  Response.builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Reporte Creado exitosamente")
                .data(toDTO(saved))
                .build();
    }

    @Override
    public Response<List<ReportDTO>> getAllReports() {
        List<Report> reports = reportRepo.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ReportDTO> dtos = reports.stream().map(this::toDTO).toList();
        return Response.<List<ReportDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(dtos.isEmpty() ? "No hay reportes" : "Reportes obtenidos con exito")
                .data(dtos)
                .build();
    }

    @Override
    public Response<List<ReportDTO>> getMyReports() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof CustomUserPrincipal principal)) {
            throw new BadRequestException("Usuario no autenticado");
        }

        Long userId = principal.getId();

        List<Report> reports = reportRepo.findByCreatedByIdOrderByCreatedAtDesc(userId);

        List<ReportDTO> reportDTOs = reports.stream().map(this::toDTO).toList();

        return Response.<List<ReportDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Reportes obtenidos correctamente")
                .data(reportDTOs)
                .build();
    }

    @Override
    public Response<List<ReportDTO>> getReportsByStatus(ReportStatus status) {
        List<Report> reports = reportRepo.findByStatusOrderByCreatedAtDesc(status);
        List<ReportDTO> dtos = reports.stream().map(this::toDTO).toList();
        return Response.<List<ReportDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(dtos.isEmpty() ? "No hay reportes" : "Reportes obtenidos con exito")
                .data(dtos)
                .build();
    }

    @Override
    public Response<ReportDTO> getReportById(Long id) {
        Report report = reportRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Reporte no encontrado"));
        return Response.<ReportDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Reporte obtenido con exito")
                .data(toDTO(report))
                .build();
    }

    @Override
    public Response<ReportDTO> updateReport(Long id, ReportDTO reportDTO) {
        Report report = reportRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Reporte no encontrado"));

        report.setStatus(reportDTO.getStatus() != null ? reportDTO.getStatus() : report.getStatus());
        report.setReviewNotes(reportDTO.getReviewNotes() != null ? reportDTO.getReviewNotes() : report.getReviewNotes());
        report.setReviewedById(reportDTO.getReviewedById() != null ? reportDTO.getReviewedById() : report.getReviewedById());
        report.setUpdatedAt(LocalDateTime.now());

        Report updated = reportRepo.save(report);
        return new Response<>(200, "Reporte actualizado correctamente", toDTO(updated));
    }

    private ReportDTO toDTO(Report report) {
        ReportDTO dto = new ReportDTO();
        dto.setId(report.getId());
        dto.setAnonymous(report.isAnonymous());
        dto.setCreatedBy(report.getCreatedById());
        dto.setIpHash(report.getIpHash());
        dto.setTitle(report.getTitle());
        dto.setDescription(report.getDescription());
        dto.setSeverity(report.getSeverity());
        dto.setVictimId(report.getVictimId());
        dto.setStatus(report.getStatus());
        dto.setReviewedById(report.getReviewedById());
        dto.setReviewNotes(report.getReviewNotes());
        dto.setCreatedAt(report.getCreatedAt());
        dto.setUpdatedAt(report.getUpdatedAt());
        return dto;
    }
}
