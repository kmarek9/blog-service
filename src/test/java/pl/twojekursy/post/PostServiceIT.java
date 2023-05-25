package pl.twojekursy.post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.ResultActions;
import pl.twojekursy.BaseIT;
import pl.twojekursy.BaseServiceIT;
import pl.twojekursy.comment.Comment;
import pl.twojekursy.test.helper.CommentCreator;
import pl.twojekursy.test.helper.PostCreator;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PostServiceIT extends BaseServiceIT {
    @Autowired
    private PostService underTest;

    @Autowired
    private PostCreator postCreator;

    @Test
    void givenPosts_whenGetFind_thenCorrectResponse() throws Exception {
        // given
        String textContaining = "ex";
        int page = 0;
        int size = 3;

        // PLEASE DO NOT CHANGE ORDER OF CREATED POSTS
        Post publishedAndActive1 = postCreator.createPost();
        Post publishedAndActive2 = postCreator.createPost(post -> post.setPublicationDate(LocalDateTime.now().plusSeconds(1)));

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
        Page<FindPostByGetMethodResponse> resultPage = underTest.find(textContaining, page, size);

        // then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getTotalElements()).isEqualTo(5);
        assertThat(resultPage.getContent())
                .hasSize(3)
                .extracting(FindPostByGetMethodResponse::id)
                .containsExactly(
                        publishedAndActive5.getId(),
                        publishedAndActive4.getId(),
                        publishedAndActive3.getId()
                )
        ;
    }
}