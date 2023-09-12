package com.example.articleservice.controller;

import com.example.articleservice.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("get")
    public ResponseEntity<?> article(@RequestParam Long id) {
        try {
            JSONObject article = articleService.getArticle(id);
            if (article.isEmpty())
                return ResponseEntity.badRequest().build();
            else
                return ResponseEntity.ok(article);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("list")
    public ResponseEntity<?> articleList(@RequestParam(defaultValue = "1") int page, @RequestParam int size) {
        try {
            JSONObject articles = articleService.getArticlePage(page, size);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("list-by-title")
    public ResponseEntity<?> articleListTitleContaining(@RequestParam String title, @RequestParam(defaultValue = "1") int page, @RequestParam int size) {
        try {
            JSONObject articles = articleService.getArticlePageWithTitleContaining(title, page, size);
            return ResponseEntity.ok(articles);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("exist")
    @ResponseStatus(HttpStatus.OK)
    public boolean isArticleExist(@RequestParam Long id) {
        return articleService.isArticleExist(id);
    }
}
