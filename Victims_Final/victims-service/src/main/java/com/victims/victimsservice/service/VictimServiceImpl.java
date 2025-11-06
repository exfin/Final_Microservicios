package com.victims.victimsservice.service;

import com.victims.victimsservice.dao.VictimRepo;
import com.victims.victimsservice.dto.Response;
import com.victims.victimsservice.dto.VictimDTO;
import com.victims.victimsservice.exceptions.BadRequestException;
import com.victims.victimsservice.exceptions.NotFoundException;
import com.victims.victimsservice.model.Victim;
import com.victims.victimsservice.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VictimServiceImpl implements VictimService {

    private final VictimRepo victimRepo;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    private String bearer() {
        String h = request.getHeader("Authorization");
        if (h == null) return "";
        h = h.trim();
        return h.toLowerCase().startsWith("bearer ") ? h.substring(7) : h;
    }

    @Override
    public Response<?> createVictim(VictimDTO victimDTO) {
        if (victimDTO.getCode() == null || victimDTO.getCode().isBlank())
            throw new BadRequestException("Victim code is required");
        if (victimDTO.getAlias() == null || victimDTO.getAlias().isBlank())
            throw new BadRequestException("Victim alias is required");

        String code = victimDTO.getCode().trim();
        victimRepo.findByCode(code).ifPresent(v -> {
            throw new BadRequestException("Victim code already exists");
        });

        Long userId = null;

        try {
            String token = bearer();
            userId = jwtUtil.extractUserId(token);
        } catch (Exception e) {
            throw new BadRequestException("No se pudo leer el token del usuario: " + e.getMessage());
        }

        if (userId == null) {
            throw new BadRequestException("No se pudo determinar el usuario autenticado");
        }

        Victim victimToSave = new Victim();
        victimToSave.setCode(code);
        victimToSave.setAlias(victimDTO.getAlias().trim());
        victimToSave.setNotes(victimDTO.getNotes());
        victimToSave.setRiskLevel(victimDTO.getRiskLevel());
        victimToSave.setActive(true);
        victimToSave.setCreatedBy(userId);           // ← solo ID
        victimToSave.setCreatedAt(LocalDateTime.now());
        victimToSave.setUpdatedAt(null);

        Victim saved = victimRepo.save(victimToSave);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Victim created successfully")
                .data(saved.getId())
                .build();
    }

    @Override
    public Response<VictimDTO> getVictimById(Long id) {
        Victim victim = victimRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Victim not found"));

        VictimDTO dto = modelMapper.map(victim, VictimDTO.class);
        return Response.<VictimDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Victim retrieved successfully")
                .data(dto)
                .build();
    }

    @Override
    public Response<List<VictimDTO>> getAllVictims() {
        List<VictimDTO> dtos = victimRepo.findAll().stream()
                .map(v -> modelMapper.map(v, VictimDTO.class))
                .toList();

        return Response.<List<VictimDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(dtos.isEmpty() ? "No victims found" : "Victims retrieved successfully")
                .data(dtos)
                .build();
    }

    @Override
    @Transactional
    public Response<?> updateVictim(VictimDTO victimDTO) {
        if (victimDTO.getCode() == null || victimDTO.getCode().isBlank())
            throw new BadRequestException("Victim code is required to update");

        Victim victim = victimRepo.findByCode(victimDTO.getCode().trim())
                .orElseThrow(() -> new NotFoundException("Victim not found"));

        if (victimDTO.getAlias() != null && !victimDTO.getAlias().isBlank())
            victim.setAlias(victimDTO.getAlias().trim());
        if (victimDTO.getNotes() != null)
            victim.setNotes(victimDTO.getNotes());
        if (victimDTO.getRiskLevel() != null)
            victim.setRiskLevel(victimDTO.getRiskLevel());
        // si quieres permitir activar/desactivar:
        // victim.setActive(victimDTO.isActive());
        victim.setUpdatedAt(LocalDateTime.now());

        // save no estrictamente necesario por @Transactional, pero explícito:
        victimRepo.save(victim);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Victim updated successfully")
                .build();
    }

    @Override
    public Response<?> deleteVictim(String code) {
        Victim victim = victimRepo.findByCode(code.trim())
                .orElseThrow(() -> new NotFoundException("Victim not found"));

        victimRepo.delete(victim);
        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Victim deleted successfully")
                .build();
    }

    @Override
    public Response<List<VictimDTO>> getMyVictims() {
        Long userId = null;

        try {
            String token = bearer();
            userId = jwtUtil.extractUserId(token);
        } catch (Exception e) {
            throw new BadRequestException("No se pudo leer el token del usuario: " + e.getMessage());
        }

        if (userId == null) {
            throw new BadRequestException("No se pudo determinar el usuario autenticado");
        }

        List<VictimDTO> dtos = victimRepo.findByCreatedBy(userId).stream()
                .map(v -> modelMapper.map(v, VictimDTO.class))
                .toList();

        return Response.<List<VictimDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(dtos.isEmpty() ? "No victims found" : "Victims retrieved successfully")
                .data(dtos)
                .build();
    }
}