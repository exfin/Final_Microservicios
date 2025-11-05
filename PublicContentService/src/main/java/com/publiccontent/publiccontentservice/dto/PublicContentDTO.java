package com.publiccontent.publiccontentservice.dto;

import com.publiccontent.publiccontentservice.enums.PublicContentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicContentDTO {

    private Long id;

    private PublicContentType type;

    private String title;

    private String bodyMd;

    private String imageUrl;

    private String altText;

    private long userId;

    private boolean approved;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

