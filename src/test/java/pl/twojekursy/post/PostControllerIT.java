package pl.twojekursy.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import pl.twojekursy.BaseIT;
import pl.twojekursy.comment.Comment;
import pl.twojekursy.test.helper.PostCreator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerIT extends BaseIT {

    @Autowired
    private PostCreator postCreator;

    @Test
    void givenWrongRequest_whenCreate_thenBadRequest() throws Exception {
        // given
        CreatePostRequest request = new CreatePostRequest(null, null, null, null);
        // when
        ResultActions resultActions = performPost("/api/posts", request);

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

        // when
        ResultActions resultActions = performPost("/api/posts", request);

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

    @Test
    void givenWrongRequest_whenUpdate_thenBadRequest() throws Exception {
        // given
        UpdatePostRequest request = new UpdatePostRequest(null, null, null);
        Long id = 1000L;

        // when
        ResultActions resultActions = performPut("/api/posts/{id}", id, request);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.scope").value("must not be null"))
                .andExpect(jsonPath("$.text").value("must not be blank"))
                .andExpect(jsonPath("$.version").value("must not be null"));
    }

    @Test
    void givenNotExistingPost_whenUpdate_thenNotFound() throws Exception {
        // given
        UpdatePostRequest request = new UpdatePostRequest(0, "text", PostScope.PUBLIC);
        Long id = 1000L;

        // when
        ResultActions resultActions = performPut("/api/posts/{id}", id, request);

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().string(is(emptyString())))
        ;
    }

    @Test
    void givenCorrectRequest_whenUpdate_thenUpdatePost() throws Exception {
        // given
        Post post = postCreator.createPostWithOneComment();

        String newText = "newText";
        PostScope newScope = PostScope.PRIVATE;
        UpdatePostRequest request = new UpdatePostRequest(post.getVersion(), newText, newScope);
        Long id = post.getId();

        // when
        ResultActions resultActions = performPut("/api/posts/{id}", id, request);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(is(emptyString())));

        Post updatedPost = entityManager.createQuery("select p from Post p left join fetch p.comments where p.id=:id", Post.class)
                .setParameter("id", id)
                .getSingleResult();

        assertThat(updatedPost.getLastModifiedDateTime()).isAfter(post.getCreatedDateTime());
        assertThat(updatedPost.getCreatedDateTime()).isEqualToIgnoringNanos(post.getCreatedDateTime());

        assertThat(updatedPost)
                .extracting(
                        Post::getVersion,
                        Post::getText,
                        Post::getAuthor,
                        Post::getPublicationDate,
                        Post::getScope,
                        Post::getStatus
                ).containsExactly(
                        post.getVersion()+1,
                        newText,
                        post.getAuthor(),
                        post.getPublicationDate(),
                        newScope,
                        post.getStatus()
                );

        assertThat(updatedPost.getComments()).hasSize(1);
        Comment comment = updatedPost.getComments().iterator().next();
        assertThat(comment.getLastModifiedDateTime()).isEqualToIgnoringNanos(comment.getCreatedDateTime());
    }

    @Test
    void givenWrongVersion_whenUpdate_thenConflict() throws Exception {
        // given
        Post post = postCreator.createPost();
        int wrongVersion = post.getVersion() + 1;

        UpdatePostRequest request = new UpdatePostRequest(wrongVersion, "newText", PostScope.PRIVATE);
        Long id = post.getId();

        // when
        ResultActions resultActions = performPut("/api/posts/{id}", id, request);

        // then
        resultActions.andExpect(status().isConflict())
                .andExpect(content().string(is(emptyString())));

        Post shouldntBeUpdatedPost = entityManager.find(Post.class, id);

        assertThat(shouldntBeUpdatedPost.getLastModifiedDateTime()).isEqualToIgnoringNanos(post.getCreatedDateTime());
        assertThat(shouldntBeUpdatedPost.getCreatedDateTime()).isEqualToIgnoringNanos(post.getCreatedDateTime());

        assertThat(shouldntBeUpdatedPost)
                .extracting(
                        Post::getVersion,
                        Post::getText,
                        Post::getAuthor,
                        Post::getPublicationDate,
                        Post::getScope,
                        Post::getStatus
                ).containsExactly(
                        post.getVersion(),
                        post.getText(),
                        post.getAuthor(),
                        post.getPublicationDate(),
                        post.getScope(),
                        post.getStatus()
                );
    }
}