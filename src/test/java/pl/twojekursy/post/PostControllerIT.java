package pl.twojekursy.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.twojekursy.BaseIT;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerIT extends BaseIT {

    @Test
    void givenWrongRequest_whenCreate_thenBadRequest() throws Exception {
        // given
        String content = "{}";

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.author").value("must not be blank"))
                .andExpect(jsonPath("$.text").value("must not be blank"))
                .andExpect(jsonPath("$.scope").value("must not be null"));
    }

    @Test
    void givenCorrectRequest_whenCreate_thenCreatePost() throws Exception {
        // given
        String text = "text";
        PostScope scope = PostScope.PUBLIC;
        String author = "author";
        LocalDateTime publicationDate = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);

        CreatePostRequest request = new CreatePostRequest(
                text,
                scope,
                author,
                publicationDate
        );

        String content = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        );

        // then
        resultActions.andExpect(status().isOk());

        List<Post> posts = entityManager.createQuery("select p from Post p left join fetch p.comments").getResultList();
        assertThat(posts).hasSize(1);
        Post post = posts.get(0);
        assertThat(post)
                .extracting(
                        Post::getId,
                        Post::getCreatedDateTime,
                        Post::getLastModifiedDateTime
                ).isNotNull();

        assertThat(post)
                .extracting(
                        Post::getVersion,
                        Post::getText,
                        Post::getAuthor,
                        Post::getPublicationDate,
                        Post::getScope,
                        Post::getStatus
                ).containsExactly(
                        0,
                        text,
                        author,
                        publicationDate,
                        scope,
                        PostStatus.ACTIVE
                );

        assertThat(post.getComments()).isEmpty();
    }
}