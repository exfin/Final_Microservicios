package com.incentive.incentiveservice.service;

import com.incentive.incentiveservice.dto.IncentiveEventDTO;
import com.incentive.incentiveservice.dto.Response;
import com.incentive.incentiveservice.enums.IncentiveType;
import com.incentive.incentiveservice.models.IncentiveEvent;
import com.incentive.incentiveservice.repo.IncentiveEventRepo;
import com.incentive.incentiveservice.exceptions.BadRequestException; // si tu excepción está en otro paquete, ajusta import
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncentiveEventServiceImpl implements IncentiveEventService {

    private final IncentiveEventRepo incentiveEventRepo;
    private final ModelMapper modelMapper;

    @Override
    public Response<?> createIncentiveEvent(IncentiveEventDTO incentiveEventDTO) {

        if (incentiveEventDTO.getUser() <= 0) {
            throw new BadRequestException("El beneficiario (user) es obligatorio y debe ser > 0");
        }
        if (incentiveEventDTO.getAssignedBy() <= 0) {
            throw new BadRequestException("El asignador (assignedBy) es obligatorio y debe ser > 0");
        }
        if (incentiveEventDTO.getName() == null || incentiveEventDTO.getName().isBlank()) {
            throw new BadRequestException("El nombre es obligatorio");
        }
        if (incentiveEventDTO.getDescription() == null || incentiveEventDTO.getDescription().isBlank()) {
            throw new BadRequestException("La descripción es obligatoria");
        }
        if (incentiveEventDTO.getType() == null) {
            throw new BadRequestException("El tipo de incentivo es obligatorio");
        }
        if (incentiveEventDTO.getPoints() == 0) {
            throw new BadRequestException("Los puntos no pueden ser 0");
        }

        IncentiveEvent entity = new IncentiveEvent();
        entity.setUser(incentiveEventDTO.getUser());               // ID plano
        entity.setAssignedBy(incentiveEventDTO.getAssignedBy());   // ID plano
        entity.setType(incentiveEventDTO.getType());
        entity.setName(incentiveEventDTO.getName().trim());
        entity.setDescription(incentiveEventDTO.getDescription().trim());
        entity.setPoints(incentiveEventDTO.getPoints());
        entity.setCreatedAt(LocalDateTime.now());

        IncentiveEvent saved = incentiveEventRepo.save(entity);

        IncentiveEventDTO responseDTO = modelMapper.map(saved, IncentiveEventDTO.class);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Incentivo creado con éxito")
                .data(responseDTO)
                .build();
    }

    @Override
    public Response<List<IncentiveEventDTO>> getAllIncentiveEvents() {
        List<IncentiveEventDTO> all = incentiveEventRepo.findAll()
                .stream()
                .map(ev -> modelMapper.map(ev, IncentiveEventDTO.class))
                .toList();

        return Response.<List<IncentiveEventDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(all.isEmpty() ? "No hay eventos de incentivos" : "Eventos de incentivos obtenidos con éxito")
                .data(all)
                .build();
    }

    @Override
    public Response<List<IncentiveEventDTO>> getAllByType(IncentiveType type) {
        List<IncentiveEventDTO> byType = incentiveEventRepo.findByTypeOrderByCreatedAtDesc(type)
                .stream()
                .map(ev -> modelMapper.map(ev, IncentiveEventDTO.class))
                .toList();

        return Response.<List<IncentiveEventDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(byType.isEmpty()
                        ? "No hay eventos de incentivos de tipo " + type
                        : "Eventos de incentivos obtenidos con éxito")
                .data(byType)
                .build();
    }

    @Override
    public Response<List<IncentiveEventDTO>> getUserEvents(Long id) {
        List<IncentiveEventDTO> userEvents = incentiveEventRepo.findByUserIdOrderByCreatedAtDesc(id)
                .stream()
                .map(ev -> modelMapper.map(ev, IncentiveEventDTO.class))
                .toList();

        return Response.<List<IncentiveEventDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(userEvents.isEmpty()
                        ? "No hay eventos de incentivos para el usuario con ID " + id
                        : "Eventos de incentivos obtenidos con éxito")
                .data(userEvents)
                .build();
    }

    @Override
    public Response<Integer> getUserBalance(Long id) {
        int balance = incentiveEventRepo.sumPointsByUser(id);
        return Response.<Integer>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Balance retrieved successfully")
                .data(balance)
                .build();
    }
}