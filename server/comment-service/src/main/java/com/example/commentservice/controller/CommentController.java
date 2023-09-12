package com.example.commentservice.controller;

import com.example.commentservice.dto.CommentResponse;
import com.example.commentservice.dto.NewCommentRequest;
import com.example.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<?> get(@RequestParam Long articleId) {
        try {
            JSONObject comments = commentService.getAllByArticleId(articleId);
            if (comments.isEmpty())
                return ResponseEntity.badRequest().build();
            else
                return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody NewCommentRequest request) {
        try {
            CommentResponse comment = commentService.addComment(request);
            if (comment != null)
                return ResponseEntity.ok(comment);
            else
                return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
