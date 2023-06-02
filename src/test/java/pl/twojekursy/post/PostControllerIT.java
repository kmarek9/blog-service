package pl.twojekursy.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import pl.twojekursy.BaseIT;
import pl.twojekursy.comment.Comment;
import pl.twojekursy.test.helper.CommentCreator;
import pl.twojekursy.test.helper.PostCreator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostControllerIT extends BaseIT {

    private static final String API_POSTS_URL_PREFIX = "/api/posts";
    @Autowired
    private PostCreator postCreator;

    @Autowired
    private CommentCreator commentCreator;


    @Test
    void givenWrongRequest_whenCreate_thenBadRequest() throws Exception {
        // given
        CreatePostRequest request = new CreatePostRequest(null, null, null);
        // when
        ResultActions resultActions = performPost(API_POSTS_URL_PREFIX, request);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(3)))
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
                publicationDate
        );

        // when
        ResultActions resultActions = performPost(API_POSTS_URL_PREFIX, request);

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
        ResultActions resultActions = performPut(API_POSTS_URL_PREFIX + "/{id}", id, request);

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
        ResultActions resultActions = performPut(API_POSTS_URL_PREFIX + "/{id}", id, request);

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
        ResultActions resultActions = performPut(API_POSTS_URL_PREFIX + "/{id}", id, request);

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
        ResultActions resultActions = performPut(API_POSTS_URL_PREFIX + "/{id}", id, request);

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


    @Test
    void givenNotExistingPost_whenRead_thenNotFound() throws Exception {
        // given
        Long id = 1000L;

        // when
        ResultActions resultActions = performGet(API_POSTS_URL_PREFIX + "/{id}", id);

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().string(is(emptyString())))
        ;
    }

    @Test
    void givenExistingPost_whenRead_thenReturnResponse() throws Exception {
        // given
        Post post = postCreator.createPost();
        Long postId = post.getId();
        Comment comment1 = commentCreator.createComment(post, 1);
        Comment comment2 = commentCreator.createComment(post, 2);
        Comment comment3 = commentCreator.createComment(post, 3);

        List<Comment> commentList = List.of(comment3, comment2, comment1);

        // when
        ResultActions resultActions = performGet(API_POSTS_URL_PREFIX + "/{id}", postId);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(post.getVersion()))
                .andExpect(jsonPath("$.text").value(post.getText()))
                .andExpect(jsonPath("$.scope").value(post.getScope().name()))
                .andExpect(jsonPath("$.author").value(post.getAuthor()))
                .andExpect(jsonPath("$.publicationDate", is(nullValue())))
                .andExpect(jsonPath("$.status").value(post.getStatus().name()))
                //.andExpect(jsonPath("$.createdDateTime").value(post.getCreatedDateTime().truncatedTo(ChronoUnit.MICROS).toString()))
                .andExpect(jsonPath("$.comments[*]", hasSize(commentList.size())))
                .andExpect(jsonPath("$.comments[*].id", contains(comment3.getId().intValue(), comment2.getId().intValue(), comment1.getId().intValue())));

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        LocalDateTime postCreatedDateTime = parseDateTime(contentAsString, "$.createdDateTime");
        assertThat(postCreatedDateTime).isEqualToIgnoringNanos(post.getCreatedDateTime());

        // tests for comments
        int i =0;
        for (Comment comment : commentList) {
            resultActions.andExpect(jsonPath("$.comments[" + i + "].text").value(comment.getText()))
                    //.andExpect(jsonPath("$.comments[" + i + "].createdDateTime").value(comment.getCreatedDateTime().truncatedTo(ChronoUnit.MICROS).toString()))
                    .andExpect(jsonPath("$.comments[" + i + "].author").value(comment.getAuthor()));

            LocalDateTime commentCreatedDateTime = parseDateTime(contentAsString, "$.comments[" + i + "].createdDateTime");
            assertThat(commentCreatedDateTime).isEqualToIgnoringNanos(comment.getCreatedDateTime());
            i++;
        }
    }

    @Test
    void givenNoPostsInDb_whenGetFind_thenEmptyList() throws Exception {
        // given

        // when
        ResultActions resultActions = performGet(API_POSTS_URL_PREFIX,
                Map.of(
                        "q", "text",
                        "page", "0",
                        "size", "3"
                )
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]", is(empty())))
        ;
    }

    @Test
    void givenPosts_whenGetFind_thenCorrectResponse() throws Exception {
        // given

        // PLEASE DO NOT CHANGE ORDER OF CREATED POSTS
        Post publishedAndActive1 = postCreator.createPost();
        Post publishedAndActive2 = postCreator.createPost(post -> post.setPublicationDate(null));

        //not matching by text
        postCreator.createPost(post -> post.setText("nie pasuje"));

        Post publishedAndActive3 = postCreator.createPost();

        // not matching - deleted
        postCreator.createPost(post -> post.setStatus(PostStatus.DELETED));

        Post publishedAndActive4 = postCreator.createPost();

        //not matching - not published
        Post notPublished = postCreator.createPost(post -> post.setPublicationDate(LocalDateTime.now().plusDays(1)));

        Post publishedAndActive5 = postCreator.createPost();

        Thread.sleep(1100);
        // when
        ResultActions resultActions = performGet(API_POSTS_URL_PREFIX,
                Map.of(
                        "q", "ex",
                        "page", "0",
                        "size", "3"
                )
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.content[*]", hasSize(3)))
                .andExpect(jsonPath("$.content[*].id", contains(
                        publishedAndActive5.getId().intValue(),
                        publishedAndActive4.getId().intValue(),
                        publishedAndActive3.getId().intValue()))
                );
    }

}