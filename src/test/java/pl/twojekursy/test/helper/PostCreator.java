package pl.twojekursy.test.helper;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostScope;
import pl.twojekursy.post.PostStatus;
import pl.twojekursy.user.User;

import java.util.function.Consumer;

@Component
public class PostCreator {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CommentCreator commentCreator;


    @Transactional
    public Post createPost(User user) {
        Post post = Post.builder()
                .author("author")
                .text("text")
                .scope(PostScope.PUBLIC)
                .publicationDate(null)
                .status(PostStatus.ACTIVE)
                .user(user)
                .build();
        entityManager.persist(post);
        return post;
    }

    @Transactional
    public Post createPost(User user, Consumer<Post> modifier) {
        Post post = createPost(user);

        modifier.accept(post);
        entityManager.persist(post);
        return post;
    }

    @Transactional
    public Post createPostWithOneComment(User user) {
        Post post = createPost(user);
        commentCreator.createComment(post);

        return post;
    }

    @Transactional
    public Post createPostWithComments(User user, int commentsNumber) {
        Post post = createPost(user);
        for (int i = 0; i < commentsNumber; i++) {
            commentCreator.createComment(post, i);
        }

        return post;
    }
}
