package com.iqbal.sekawan.technical_test.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "content_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContentItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "content_item_id_seq")
    @SequenceGenerator(name = "content_item_id_seq", sequenceName = "content_item_id_seq", allocationSize = 1)
    private Long id;

    private String title;

    private String description;

    private String author;

    private String publisher;

    private String category;

    private String language;

    private String keywords;

    @Column(name = "license_type")
    private String licenseType;

    private String status;

    private String tags;

    // File Uploads (3 file types: Document, Video, Image)
    private String documentFile;

    private String videoFile;

    private String imageFile;
}
