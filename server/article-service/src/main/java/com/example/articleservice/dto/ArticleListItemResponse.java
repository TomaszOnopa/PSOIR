package com.example.articleservice.dto;

import java.util.Date;

public record ArticleListItemResponse(
        Long articleId,
        String title,
        Date date,
        String image
) {
}
