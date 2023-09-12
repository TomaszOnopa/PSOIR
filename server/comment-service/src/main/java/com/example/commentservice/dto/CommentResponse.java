package com.example.commentservice.dto;

import java.util.Date;

public record CommentResponse(String commentId, Date creationDate, String parentId, String content) {
}
