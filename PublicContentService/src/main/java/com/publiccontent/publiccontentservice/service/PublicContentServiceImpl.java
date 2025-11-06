package com.publiccontent.publiccontentservice.service;

import com.publiccontent.publiccontentservice.dao.PublicContentRepo;
import com.publiccontent.publiccontentservice.dto.PublicContentDTO;
import com.publiccontent.publiccontentservice.dto.Response;
import com.publiccontent.publiccontentservice.exceptions.BadRequestException;
import com.publiccontent.publiccontentservice.exceptions.NotFoundException;
import com.publiccontent.publiccontentservice.models.PublicContent;
import com.publiccontent.publiccontentservice.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicContentServiceImpl implements PublicContentService {

    private final PublicContentRepo publicContentRepo;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    // helper local para quitar "Bearer "
    private String bearer() {
        String h = request.getHeader("Authorization");
        if (h == null) return "";
        h = h.trim();
        return h.toLowerCase().startsWith("bearer ") ? h.substring(7) : h;
    }

    @Override
    public Response<?> createPublicContent(PublicContentDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank())
            throw new BadRequestException("El título es obligatorio");
        if (dto.getBodyMd() == null || dto.getBodyMd().isBlank())
            throw new BadRequestException("El contenido es obligatorio");

        // Intentamos extraer el userId del token (sin romper si no existe)
        Long userId = null;
        try {
            String token = bearer();
            userId = jwtUtil.extractUserId(token);
        } catch (Exception e) {
            System.out.println("No se pudo extraer userId del token: " + e.getMessage());
        }

        PublicContent entity = new PublicContent();
        entity.setTitle(dto.getTitle().trim());
        entity.setBodyMd(dto.getBodyMd().trim());
        entity.setType(dto.getType());
        entity.setImageUrl(dto.getImageUrl());
        entity.setAltText(dto.getAltText());
        entity.setApproved(false);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        // Solo asigna el userId si existe
        if (userId != null) {
            entity.setUserId(userId);
        }

        PublicContent saved = publicContentRepo.save(entity);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Contenido público creado con éxito")
                .data(modelMapper.map(saved, PublicContentDTO.class))
                .build();
    }


    @Override
    public Response<List<PublicContentDTO>> getApprovedPublicContent() {
        List<PublicContentDTO> list = publicContentRepo
                .findByApprovedTrueOrderByCreatedAtDesc()
                .stream().map(pc -> modelMapper.map(pc, PublicContentDTO.class)).toList();

        return Response.<List<PublicContentDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(list.isEmpty() ? "No hay contenidos aprobados" : "Contenidos aprobados encontrados")
                .data(list)
                .build();
    }

    @Override
    public Response<List<PublicContentDTO>> getApprovedFalsePublicContentById() {

        List<PublicContentDTO> list = publicContentRepo
                .findByApprovedFalseOrderByCreatedAtDesc()
                .stream().map(pc -> modelMapper.map(pc, PublicContentDTO.class)).toList();

        return Response.<List<PublicContentDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(list.isEmpty() ? "No hay contenidos pendientes de aprobación" : "Contenidos pendientes de aprobación encontrados")
                .data(list)
                .build();
    }

    @Override
    public Response<List<PublicContentDTO>> getAllEvents() {
        List<PublicContentDTO> list = publicContentRepo.findAll()
                .stream().map(pc -> modelMapper.map(pc, PublicContentDTO.class)).toList();

        return Response.<List<PublicContentDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message(list.isEmpty() ? "No hay eventos" : "Eventos encontrados")
                .data(list)
                .build();
    }

    @Override
    public Response<PublicContentDTO> updateApprovedById(PublicContentDTO dto, Long id) {
        PublicContent pc = publicContentRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Contenido público no encontrado con id: " + id));

        pc.setApproved(dto.isApproved());
        pc.setUpdatedAt(LocalDateTime.now());

        PublicContent updated = publicContentRepo.save(pc);

        return Response.<PublicContentDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Contenido público actualizado con éxito")
                .data(modelMapper.map(updated, PublicContentDTO.class))
                .build();
    }
}