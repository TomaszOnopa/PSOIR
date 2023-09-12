package com.example.articleservice.repository;

import com.example.articleservice.model.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    boolean existsByFilename(String filename);
}
