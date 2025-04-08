package me.hanjun.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import me.hanjun.domain.Article;
import me.hanjun.domain.User;
import me.hanjun.dto.AddArticleRequest;
import me.hanjun.dto.UpdateArticleRequest;
import me.hanjun.repository.BlogRepository;
import me.hanjun.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
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
    @Autowired
    protected UserRepository userRepository;

    private User user;

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();

        user = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(
                user,user.getPassword(),user.getAuthorities()));
    }

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

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");


        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
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
    public void findAllArticles() throws Exception {

        //given
        // 블로그 글 저장
        final String url = "/api/articles";

        Article savedArticle = createdDefaultArticle();





        //when
        // 목록 조회 api 호출

        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        //then
        //응답코드가 200 ok이고 반환 값중 0번째 요소의 타이틀과 컨텐트가 같은 지 확인

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$[0].title").value(savedArticle.getTitle()));
    }

    @DisplayName("findArticle : 블로그 글 조회에 성공한다")
    @Test
    public void findArticle() throws Exception {
        //given
        //블로그 글 저장
        final String url = "/api/articles/{id}";

        Article savedArticle = createdDefaultArticle();



        //when
        // 저장한 블로그 글의 id 값으로 api 호출
        final ResultActions resultActions = mockMvc.perform(get(url
                ,savedArticle.getId()));
        // media 타입을 명시적으로 설정하지 않아도 spring에서 json으로 자동 지정


        //then
        //응답 코드가 200ok고 반환받은 content와 title이 같은 지 확인

        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(savedArticle.getContent()))
                .andExpect(jsonPath("$.title").value(savedArticle.getTitle()));
        // jsonpath의 사용
    }

    @DisplayName("deleteArticle : 블로그 글 삭제에 성공한다")
    @Test
    public void deleteArticle() throws Exception {
        //given
        final String url = "/api/articles/{id}";

        Article savedArticle = createdDefaultArticle();

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
        Article savedArticle = createdDefaultArticle();

        final String newTitle = "new Title";
        final String newContent = "new Content";

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);


        //when
        //update api로 수정 요청을 보낸다. 요청타입은json, given에서 만든 객체를 본문으로 보낸다

        ResultActions result = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));


        //then
        //응답 코드가 200 ok 블로그 글 id로 조회 후 수정되었는 지 확인
        result.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).get();

        assertThat(article.getContent()).isEqualTo(newContent);
        assertThat(article.getTitle()).isEqualTo(newTitle);




    }

    @Test
    @DisplayName("addArticle : 아티클을 추가할 때 title이 null이면 실패한다")
    public void addArticleValidation() throws Exception {

        //given

        final String url = "/api/articles";
        final String title = null;
        final String content = "content";

        AddArticleRequest addArticle = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(addArticle);

        //principal 객체를 null로 목킹
        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");


        //when
        ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        //then
        actions.andExpect(status().isBadRequest());
    }


    @DisplayName("addArticle : 아티클을 추가할 때 title이 10자가 넘으면 실패한다")
    @Test
    public void addArticleSizeValidation() throws Exception {

        //given
        Faker faker = new Faker();

        final String url = "/api/articles";
        final String title1 = "asdjakodanojvabnvav";
        final String title = faker.lorem().characters(11);
        //faker 객체로 11자 이상 만들기
        final String content = "cotent";


        AddArticleRequest articleRequest = new AddArticleRequest(title, content);
        String requestBody = objectMapper.writeValueAsString(articleRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        //when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));


        //then
        result.andExpect(status().isBadRequest());
    }


    private Article createdDefaultArticle() {

        return blogRepository.save(Article.builder()
                .title("title")
                .author(user.getUsername())
                .content("content")
                .build());
    }


}