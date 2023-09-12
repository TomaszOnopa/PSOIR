package com.example.commentservice.service;

import com.example.commentservice.dto.CommentResponse;
import com.example.commentservice.dto.NewCommentRequest;
import com.example.commentservice.model.Comment;
import com.example.commentservice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final WebClient.Builder webClientBuilder;

    public JSONObject getAllByArticleId(Long articleId) {
        List<CommentResponse> comments = commentRepository.findAllByArticleOrderByCreationDateDesc(articleId)
                .stream()
                .map(this::mapToCommentResponse)
                .toList();
        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();

        if (comments.size() != 0) {
            jArray.addAll(comments);
            jObject.put("comments", jArray);
        }
        return jObject;
    }

    @Transactional
    public CommentResponse addComment(NewCommentRequest request) {
        if (request.articleId() == null || request.content() == null)
            return null;

        //check if article exists
        boolean isArticleExist = webClientBuilder.build().get()
                .uri("http://article-service/api/article/exist",
                        uriBuilder -> uriBuilder.queryParam("id", request.articleId()).build())
                .retrieve()
                .bodyToMono(boolean.class)
                .block();
        if (!isArticleExist)
            return null;

        Comment comment = new Comment();

        comment.setArticle(request.articleId());
        comment.setContent(request.content());
        comment.setCreationDate(new Date());
        comment.setParentId(request.parentId());

        Comment result = commentRepository.save(comment);
        return mapToCommentResponse(result);
    }

    private CommentResponse mapToCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getCreationDate(),
                comment.getParentId(),
                comment.getContent()
        );
    }
}
