package pl.twojekursy.comment;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import pl.twojekursy.post.Post;
import pl.twojekursy.user.User;

@Component
public class CommentAuthorizationChecker {
    public void checkPermissions(User user, Comment comment) {
        if (!user.isAdmin() && !comment.isAuthor(user.getId())) {
            throw new AccessDeniedException("Cant update comment");
        }
    }
}
