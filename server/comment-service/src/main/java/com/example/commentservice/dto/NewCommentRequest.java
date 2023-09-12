package com.example.commentservice.dto;

public record NewCommentRequest(Long articleId, String parentId, String content) {
}
