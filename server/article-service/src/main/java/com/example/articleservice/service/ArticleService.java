package com.example.articleservice.service;

import com.example.articleservice.dto.ArticleListItemResponse;
import com.example.articleservice.dto.ArticleResponse;
import com.example.articleservice.model.Article;
import com.example.articleservice.model.Attachment;
import com.example.articleservice.repository.ArticleRepository;
import com.example.articleservice.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final AttachmentRepository attachmentRepository;

    public JSONObject getArticle(Long articleId) {
        JSONObject jObject = new JSONObject();

        Optional<ArticleResponse> article = articleRepository.findById(articleId)
                .map(this::mapToArticleResponse);
        article.ifPresent(articleDto -> jObject.put("article", articleDto));

        return jObject;
    }

    public JSONObject getArticlePage(int pageNum, int pageSize) {
        Pageable paging = PageRequest.of(pageNum-1, pageSize, Sort.by("date").descending());
        Page<Article> page = articleRepository.findAll(paging);

        return createPageData(page);
    }

    public JSONObject getArticlePageWithTitleContaining(String title, int pageNum, int pageSize) {
        Pageable paging = PageRequest.of(pageNum-1, pageSize, Sort.by("date").descending());
        Page<Article> page = articleRepository.findAllByTitleContaining(title, paging);

        return createPageData(page);
    }

    private JSONObject createPageData(Page<Article> page) {
        List<ArticleListItemResponse> articles = page.getContent().stream().map(this::mapToArticleListItemResponse).toList();

        JSONObject jObject = new JSONObject();
        JSONArray jArray = new JSONArray();
        jArray.addAll(articles);
        jObject.put("articles", jArray);
        jObject.put("totalItems", page.getTotalElements());
        jObject.put("totalPages", page.getTotalPages());

        return jObject;
    }

    private ArticleResponse mapToArticleResponse(Article article) {
        List<String> content = Arrays.stream(article.getContent().split("\n")).toList();
        List<String> filenames = new ArrayList<>();
        for (Attachment attachment : article.getAttachments())
            filenames.add(attachment.getFilename());
        return new ArticleResponse(article.getArticleId(), article.getTitle(), article.getDate(), content, filenames);
    }

    private ArticleListItemResponse mapToArticleListItemResponse(Article article) {
        return new ArticleListItemResponse(
                article.getArticleId(),
                article.getTitle(),
                article.getDate(),
                article.getAttachments().size() > 0 ? article.getAttachments().get(0).getFilename() : null
        );
    }

    public boolean isArticleExist(Long id) {
        return articleRepository.existsById(id);
    }
}
