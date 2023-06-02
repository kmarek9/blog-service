package pl.twojekursy.post;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import pl.twojekursy.BaseServiceIT;
import pl.twojekursy.test.helper.PostCreator;
import pl.twojekursy.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryIT extends BaseServiceIT {
    @Autowired
    private PostRepository underTest;

    @Autowired
    private PostCreator postCreator;

    @Test
    void givenPosts_whenGetFind_thenCorrectResponse() throws Exception {
        // given
        String textContaining = "ex";
        int page = 1;
        int size = 3;

        // PLEASE DO NOT CHANGE ORDER OF CREATED POSTS
        User user = userCreator.createUser();
        Post publishedAndActive1 = postCreator.createPost(user);
        Post publishedAndActive2 = postCreator.createPost(user, post -> post.setPublicationDate(LocalDateTime.now().plusSeconds(1)));

        //not matching by text
        postCreator.createPost(user,post -> post.setText("nie pasuje"));

        Post publishedAndActive3 = postCreator.createPost(user);

        // not matching - deleted
        postCreator.createPost(user,post -> post.setStatus(PostStatus.DELETED));

        Post publishedAndActive4 = postCreator.createPost(user);

        //not matching - not published
        Post notPublished = postCreator.createPost(user,post -> post.setPublicationDate(LocalDateTime.now().plusDays(1)));

        Post publishedAndActive5 = postCreator.createPost(user);

        // when
        Page<Post> resultPage = underTest.findActiveAndPublished(textContaining,
                LocalDateTime.now().plusSeconds(10),
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("createdDateTime"))));

        // then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getTotalElements()).isEqualTo(5);
        assertThat(resultPage.getContent())
                .hasSize(2)
                .extracting(Post::getId)
                .containsExactly(
                        publishedAndActive4.getId(),
                        publishedAndActive5.getId()
                )
        ;
    }
}