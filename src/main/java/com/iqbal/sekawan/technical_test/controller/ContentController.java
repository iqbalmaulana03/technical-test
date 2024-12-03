package com.iqbal.sekawan.technical_test.controller;

import com.iqbal.sekawan.technical_test.dto.request.ContentRequest;
import com.iqbal.sekawan.technical_test.dto.request.SearchContentRequest;
import com.iqbal.sekawan.technical_test.dto.response.ContentResponse;
import com.iqbal.sekawan.technical_test.dto.response.PagingResponse;
import com.iqbal.sekawan.technical_test.dto.response.WebResponse;
import com.iqbal.sekawan.technical_test.service.IContentService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
@Slf4j
public class ContentController {

    private final IContentService contentService;

    @PostMapping()
    @CircuitBreaker(name = "content", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "content")
    @Retry(name = "content")
    public ResponseEntity<WebResponse<ContentResponse>> add(@RequestBody ContentRequest request){
        WebResponse<ContentResponse> response = WebResponse.<ContentResponse>builder()
                .status(HttpStatus.CREATED.getReasonPhrase())
                .message("successfully created a new content")
                .data(contentService.add(request))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<ContentResponse>> get(@PathVariable(name = "id") Long contentId){
        WebResponse<ContentResponse> response = WebResponse.<ContentResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get by id a content")
                .data(contentService.getBy(contentId))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<WebResponse<List<ContentResponse>>> get(@RequestParam(name = "title", required = false) String title,
                                                                  @RequestParam(name = "author", required = false) String author,
                                                                  @RequestParam(name = "publisher", required = false) String publisher,
                                                                  @RequestParam(name = "category", required = false) String category,
                                                                  @RequestParam(name = "language", required = false) String language,
                                                                  @RequestParam(name = "tags", required = false) String tags,
                                                                  @RequestParam(name = "page", defaultValue = "1") Integer page,
                                                                  @RequestParam(name = "size", defaultValue = "10") Integer size) {

        SearchContentRequest request = SearchContentRequest.builder()
                .title(title)
                .author(author)
                .publisher(publisher)
                .category(category)
                .language(language)
                .tags(tags)
                .page(page)
                .size(size)
                .build();

        Page<ContentResponse> search = contentService.getAll(request);

        PagingResponse pagingResponse = PagingResponse.builder()
                .page(request.page())
                .size(size)
                .totalElements(search.getTotalElements())
                .totalPage(search.getTotalPages())
                .build();

        WebResponse<List<ContentResponse>> response = WebResponse.<List<ContentResponse>>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully get all data a content")
                .data(search.getContent())
                .paging(pagingResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<ContentResponse>> update(@PathVariable(name = "id") Long contentId,
                                                               @RequestBody ContentRequest request){
        WebResponse<ContentResponse> response = WebResponse.<ContentResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully updated a content")
                .data(contentService.update(contentId, request))
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<String>> delete(@PathVariable(name = "id") Long contentId){

        contentService.delete(contentId);

        WebResponse<String> response = WebResponse.<String>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully deleted a content")
                .data("OK")
                .build();

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{id}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WebResponse<ContentResponse>> uploadAvatar(@PathVariable("id") Long contentId,
                                                                      @RequestParam("file-name") MultipartFile fileName) throws IOException {

        WebResponse<ContentResponse> response = WebResponse.<ContentResponse>builder()
                .status(HttpStatus.OK.getReasonPhrase())
                .message("successfully upload image content")
                .data(contentService.uploadImage(fileName, contentId))
                .build();

        return ResponseEntity.ok(response);
    }

    @SuppressWarnings("unused")
    public CompletableFuture<String> fallbackMethod(ContentRequest contentRequest, RuntimeException runtimeException) {
        log.info("Cannot Place Order Executing Fallback logic");
        return CompletableFuture.supplyAsync(() -> "Oops! Something went wrong, please create content after some time!");
    }
}
