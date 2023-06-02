package pl.twojekursy.test.helper;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.comment.Comment;
import pl.twojekursy.post.Post;

@Component
public class CommentCreator {
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public Comment createComment(Post post) {
        Comment comment = Comment.builder()
                .author("author")
                .text("text")
                .post(post)
                .user(post.getUser())
                .build();
        entityManager.persist(comment);
        return comment;
    }

    @Transactional
    public Comment createComment(Post post, int number) {
        Comment comment = Comment.builder()
                .author("author")
                .text("text"+number)
                .post(post)
                .user(post.getUser())
                .build();
        entityManager.persist(comment);
        return comment;
    }
}
