package com.iqbal.sekawan.technical_test.service;

import com.iqbal.sekawan.technical_test.dto.request.ContentRequest;
import com.iqbal.sekawan.technical_test.dto.request.SearchContentRequest;
import com.iqbal.sekawan.technical_test.dto.response.ContentResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IContentService {

    ContentResponse add(ContentRequest request);

    ContentResponse getBy(Long id);

    ContentResponse update(Long id, ContentRequest request);

    Page<ContentResponse> getAll(SearchContentRequest request);

    void delete(Long id);

    ContentResponse uploadImage(MultipartFile file, Long id) throws IOException;
}
