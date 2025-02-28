package me.hanjun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.hanjun.domain.Article;
import me.hanjun.dto.AddArticleRequest;
import me.hanjun.dto.UpdateArticleRequest;
import me.hanjun.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    private BlogRepository blogRepository;

    @BeforeEach
    public void mockSetUp() {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        blogRepository.deleteAll();

    }

    @DisplayName("addArticle : 블로그 글 추가에 성공한다")
    @Test
    public void addArticle() throws Exception {
        //given
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";

        final AddArticleRequest userRequest = new AddArticleRequest(title, content);

        //when
        final String requestBody = objectMapper.writeValueAsString(userRequest);


        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));




        //then

        result.andExpect(status().isCreated());

        List<Article> articles = blogRepository.findAll();


        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);

    }

    @DisplayName("findAllArticles : 블로그 글 목록 조회에 성공한다")
    @Test
    public void findAllAriticles() throws Exception {

        //given
        // 블로그 글 저장
        final String url = "/api/articles";
        final String title = "title";
        final String content = "content";


        blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());



        //when
        // 목록 조회 api 호출

        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //then
        //응답코드가 200 ok이고 반환 값중 0번째 요소의 타이틀과 컨텐트가 같은 지 확인

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(content))
                .andExpect(jsonPath("$[0].title").value(title));
    }

    @DisplayName("findArticle : 블로그 글 조회에 성공한다")
    @Test
    public void findArticle() throws Exception {
        //given
        //블로그 글 저장
        final String url = "/api/articles/{id}";
        final String content = "content";
        final String title = "title";

        Article savedArticle = blogRepository.save(Article
                .builder()
                .content(content)
                .title(title)
                .build());

        //when
        // 저장한 블로그 글의 id 값으로 api 호출
        final ResultActions resultActions = mockMvc.perform(get(url
                ,savedArticle.getId()));
        // media 타입을 명시적으로 설정하지 않아도 spring에서 json으로 자동 지정


        //then
        //응답 코드가 200ok고 반환받은 content와 title이 같은 지 확인

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(content))
                .andExpect(jsonPath("$.title").value(title));
        // jsonpath의 사용
    }

    @DisplayName("deleteArticle : 블로그 글 삭제에 성공한다")
    @Test
    public void deleteArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        //when
        mockMvc.perform(delete(url, savedArticle.getId()))
                .andExpect(status().isOk());

        //then

        List<Article> articles = blogRepository.findAll();

        assertThat(articles).isEmpty();
    }

    @DisplayName("updateArticle : 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {

        //given
        //블로그 글을 저장하고, 글 수정에 필요한 요청 객체를 만든다
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "title";

        Article savedarticle = blogRepository.save(Article.builder()
                .content(content)
                .title(title)
                .build());

        final String newTitle = "new Title";
        final String newContent = "new Content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);


        //when
        //update api로 수정 요청을 보낸다. 요청타입은json, given에서 만든 객체를 본문으로 보낸다

        ResultActions result = mockMvc.perform(put(url, savedarticle.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));


        //then
        //응답 코드가 200 ok 블로그 글 id로 조회 후 수정되었는 지 확인
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedarticle.getId()).get();

        assertThat(article.getContent()).isEqualTo(newContent);
        assertThat(article.getTitle()).isEqualTo(newTitle);



        //then
    }


}