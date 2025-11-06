package com.publiccontent.publiccontentservice.controller;

import com.publiccontent.publiccontentservice.dto.PublicContentDTO;
import com.publiccontent.publiccontentservice.dto.Response;
import com.publiccontent.publiccontentservice.service.PublicContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class PublicContentController {

    private final PublicContentService publicContentService;

    @PostMapping("/create")
    public ResponseEntity<Response<?>> createPublicContent(@RequestBody PublicContentDTO publicContentDTO) {
        return ResponseEntity.ok(publicContentService.createPublicContent(publicContentDTO));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response<List<PublicContentDTO>>> getAllEvents() {
        return ResponseEntity.ok(publicContentService.getAllEvents());
    }

    @GetMapping("/approved")
    public ResponseEntity<Response<List<PublicContentDTO>>> getApprovedPublicContent() {
        return ResponseEntity.ok(publicContentService.getApprovedPublicContent());
    }

    @GetMapping("/not-approved")
    public ResponseEntity<Response<List<PublicContentDTO>>> getApprovedFalsePublicContentById() {
        return ResponseEntity.ok(publicContentService.getApprovedFalsePublicContentById());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Response<PublicContentDTO>> updateApprovedById(@PathVariable ("id") Long id, @RequestBody PublicContentDTO publicContentDTO ) {
        return ResponseEntity.ok(publicContentService.updateApprovedById(publicContentDTO, id));
    }
}

