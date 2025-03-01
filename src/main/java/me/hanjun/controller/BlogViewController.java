package me.hanjun.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hanjun.domain.Article;
import me.hanjun.dto.ArticleListViewResponse;
import me.hanjun.dto.ArticleResponse;
import me.hanjun.dto.ArticleViewResponse;
import me.hanjun.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BlogViewController {

    private final BlogService blogService;


    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles
                = blogService.findAll()
                .stream()
                .map(ArticleListViewResponse::new)
                .toList();

        model.addAttribute("articles", articles);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }
}
