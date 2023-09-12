package com.example.articleservice.repository;

import com.example.articleservice.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByTitleContaining(String title, Pageable pageable);
}
