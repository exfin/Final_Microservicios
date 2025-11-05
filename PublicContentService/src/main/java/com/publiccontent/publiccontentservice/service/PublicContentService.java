package com.publiccontent.publiccontentservice.service;

import com.publiccontent.publiccontentservice.dto.PublicContentDTO;
import com.publiccontent.publiccontentservice.dto.Response;

import java.util.List;

public interface PublicContentService {

    Response<?> createPublicContent(PublicContentDTO publicContentDTO);

    Response<List<PublicContentDTO>> getApprovedPublicContent();

    Response<List<PublicContentDTO>> getApprovedFalsePublicContentById();

    Response<List<PublicContentDTO>> getAllEvents();

    Response<PublicContentDTO> updateApprovedById(PublicContentDTO publicContentDTO, Long id);
}
