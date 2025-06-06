package me.hanjun.service;

import lombok.RequiredArgsConstructor;
import me.hanjun.domain.Article;
import me.hanjun.dto.AddArticleRequest;
import me.hanjun.dto.UpdateArticleRequest;
import me.hanjun.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;


    public Article save(AddArticleRequest request,String userName) {



        return blogRepository.save(request.toEntity(userName));

    }

    public List<Article> findAll() {
        return blogRepository.findAll();
    }

    public Article findById(long id) {

        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found : " + id));
    }

    public void delete(long id) {
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found :" + id
                ));
        authorizeArticleAuthor(article);
        blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {

        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not fount" + id));


        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;


    }

    private static void authorizeArticleAuthor(Article article) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!article.getAuthor().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

}
