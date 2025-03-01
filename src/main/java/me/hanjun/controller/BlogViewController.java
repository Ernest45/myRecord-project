package me.hanjun.controller;

import lombok.RequiredArgsConstructor;
import me.hanjun.dto.ArticleListViewResponse;
import me.hanjun.dto.ArticleResponse;
import me.hanjun.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}
