package me.hanjun.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hanjun.domain.Article;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddArticleRequest {

    @NotNull
    @Size(min = 1, max = 10)
    private String title;
    @NotNull
    private String content;


    public Article toEntity(String author) {

        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

}
