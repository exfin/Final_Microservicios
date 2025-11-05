package com.publiccontent.publiccontentservice.dao;

import com.publiccontent.publiccontentservice.models.PublicContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PublicContentRepo extends JpaRepository<PublicContent, Long> {

    List<PublicContent> findByApprovedTrueOrderByCreatedAtDesc();

    List<PublicContent> findByApprovedFalseOrderByCreatedAtDesc();
}

