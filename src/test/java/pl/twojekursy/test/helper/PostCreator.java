package pl.twojekursy.test.helper;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostScope;
import pl.twojekursy.post.PostStatus;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class PostCreator {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CommentCreator commentCreator;


    @Transactional
    public Post createPost() {
        LocalDateTime publicationDate = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);
        Post post = Post.builder()
                .author("author")
                .text("text")
                .scope(PostScope.PUBLIC)
                .publicationDate(publicationDate)
                .status(PostStatus.ACTIVE)
                .build();
        entityManager.persist(post);
        return post;
    }

    @Transactional
    public Post createPostWithOneComment() {
        Post post = createPost();
        commentCreator.createComment(post);

        return post;
    }

    @Transactional
    public Post createPostWithComments(int commentsNumber) {
        Post post = createPost();
        for (int i = 0; i < commentsNumber; i++) {
            commentCreator.createComment(post, i);
        }

        return post;
    }
}
