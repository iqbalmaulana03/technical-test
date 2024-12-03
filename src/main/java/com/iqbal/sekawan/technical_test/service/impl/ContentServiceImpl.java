package com.iqbal.sekawan.technical_test.service.impl;

import com.iqbal.sekawan.technical_test.dto.request.ContentRequest;
import com.iqbal.sekawan.technical_test.dto.request.SearchContentRequest;
import com.iqbal.sekawan.technical_test.dto.response.ContentResponse;
import com.iqbal.sekawan.technical_test.model.ContentItem;
import com.iqbal.sekawan.technical_test.repository.IContentRepository;
import com.iqbal.sekawan.technical_test.service.IContentService;
import com.iqbal.sekawan.technical_test.utils.ValidationUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ContentServiceImpl implements IContentService {

    private final IContentRepository repository;
    private final ValidationUtils utils;
    private final List<String> imageContentTypes = List.of("image/jpeg", "image/png", "image/gif", "image/webp");
    private final List<String> documentContentTypes = List.of("application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/zip");
    private final List<String> videoContentTypes = List.of("video/mp4", "video/avi", "video/mkv", "video/webm");
    private final Path directoryPath;

    private static final String CONTENT_NOT_FOUND = "content not found";

    public ContentServiceImpl(IContentRepository repository, ValidationUtils utils, @Value("${app.contentmanagent.directory-image-path}") String directoryPath) {
        this.repository = repository;
        this.utils = utils;
        this.directoryPath = Paths.get(directoryPath);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContentResponse add(ContentRequest request) {
        utils.validate(request);

        ContentItem contentItem = ContentItem.builder()
                .title(request.title())
                .description(request.description())
                .author(request.author())
                .publisher(request.publisher())
                .category(request.category())
                .language(request.language())
                .keywords(request.keywords())
                .licenseType(request.licenseType())
                .status(request.status())
                .tags(request.tags())
                .build();

        repository.save(contentItem);

        return toContentResponse(contentItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ContentResponse getBy(Long id) {
        return toContentResponse(
                repository.findById(id).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, CONTENT_NOT_FOUND)
                )
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContentResponse update(Long id, ContentRequest request) {

        ContentItem contentItem = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, CONTENT_NOT_FOUND)
        );

        contentItem.setTitle(request.title());
        contentItem.setDescription(request.description());
        contentItem.setAuthor(request.author());
        contentItem.setPublisher(request.publisher());
        contentItem.setCategory(request.category());
        contentItem.setLanguage(request.language());
        contentItem.setKeywords(request.keywords());
        contentItem.setLicenseType(request.licenseType());
        contentItem.setStatus(request.status());
        contentItem.setTags(request.tags());

        return toContentResponse(contentItem);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContentResponse> getAll(SearchContentRequest request) {
        Specification<ContentItem> specification = getContentItemSpecification(request);

        if (request.page() <= 0) SearchContentRequest.builder().page(1).build();

        Pageable pageable = PageRequest.of(request.page() - 1, request.size());
        Page<ContentItem> all = repository.findAll(specification, pageable);

        return new PageImpl<>(toContentResponseList(all.getContent()), pageable, all.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        ContentItem contentItem = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, CONTENT_NOT_FOUND)
        );

        repository.delete(contentItem);
    }

    @Override
    public ContentResponse uploadImage(MultipartFile file, Long id) throws IOException {
        saveValidation(file);

        ContentItem contentItem = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found")
        );

        String fileName = String.format("%d_%s", System.currentTimeMillis(), file.getOriginalFilename());
        Path filePath = directoryPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        if (imageContentTypes.contains(file.getContentType())) {
            contentItem.setImageFile(fileName);
        } else if (documentContentTypes.contains(file.getContentType())) {
            contentItem.setDocumentFile(fileName);
        } else if (videoContentTypes.contains(file.getContentType())) {
            contentItem.setVideoFile(fileName);
        }

        return toContentResponse(contentItem);
    }

    private Specification<ContentItem> getContentItemSpecification(SearchContentRequest request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (Objects.nonNull(request.title())){
                predicates.add(criteriaBuilder.like(root.get("title"), "%"+ request.title()+"%"));
            }

            if (Objects.nonNull(request.author())){
                predicates.add(criteriaBuilder.like(root.get("author"), "%"+ request.author()+"%"));
            }

            if (Objects.nonNull(request.publisher())){
                predicates.add(criteriaBuilder.like(root.get("publisher"), "%"+ request.publisher()+"%"));
            }

            if (Objects.nonNull(request.category())){
                predicates.add(criteriaBuilder.like(root.get("category"), "%"+ request.category()+"%"));
            }

            if (Objects.nonNull(request.language())){
                predicates.add(criteriaBuilder.like(root.get("language"), "%"+ request.language()+"%"));
            }

            if (Objects.nonNull(request.tags())){
                predicates.add(criteriaBuilder.like(root.get("tags"), "%"+ request.tags()+"%"));
            }

            return Objects.requireNonNull(query).where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }

    private ContentResponse toContentResponse(ContentItem contentItem){
        return ContentResponse.builder()
                .id(contentItem.getId())
                .title(contentItem.getTitle())
                .description(contentItem.getDescription())
                .author(contentItem.getAuthor())
                .publisher(contentItem.getPublisher())
                .category(contentItem.getCategory())
                .language(contentItem.getLanguage())
                .keywords(contentItem.getKeywords())
                .licenseType(contentItem.getLicenseType())
                .status(contentItem.getStatus())
                .tags(contentItem.getTags())
                .documentUrl(contentItem.getDocumentFile())
                .imageUrl(contentItem.getImageFile())
                .videoUrl(contentItem.getVideoFile())
                .build();
    }

    private List<ContentResponse> toContentResponseList(List<ContentItem> contentItems){
        return contentItems.parallelStream().map(this::toContentResponse).toList();
    }

    private void saveValidation(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is required");
        }

        String contentType = multipartFile.getContentType();
        if (contentType == null ||
                !(imageContentTypes.contains(contentType) || documentContentTypes.contains(contentType) || videoContentTypes.contains(contentType))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid content type");
        }

        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }
    }
}
