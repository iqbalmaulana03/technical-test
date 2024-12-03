package com.iqbal.sekawan.technical_test.repository;

import com.iqbal.sekawan.technical_test.model.ContentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IContentRepository extends JpaRepository<ContentItem, Long>, JpaSpecificationExecutor<ContentItem> {
}
