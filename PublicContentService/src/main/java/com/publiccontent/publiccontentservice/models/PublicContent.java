package com.publiccontent.publiccontentservice.models;

import java.time.LocalDateTime;

import com.publiccontent.publiccontentservice.enums.PublicContentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "public_content")
@AllArgsConstructor
@NoArgsConstructor
public class PublicContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PublicContentType type;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String bodyMd;

    private String imageUrl;

    private String altText;

    @Column(nullable = false)
    private long userId;

    private boolean approved;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
