package pl.twojekursy.post;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import pl.twojekursy.user.User;

@Component
public class PostAuthorizationChecker {
    public void checkPermissions(User user, Post post) {
        if (!user.isAdmin() && !post.isAuthor(user.getId())) {
            throw new AccessDeniedException("Cant update comment");
        }
    }
}
