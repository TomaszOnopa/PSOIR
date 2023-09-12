package com.example.articleservice.dto;

import java.util.Date;
import java.util.List;

public record ArticleResponse(
        Long articleId,
        String title,
        Date date,
        List<String> content,
        List<String> attachments) {
}
