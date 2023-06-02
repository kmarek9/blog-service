package pl.twojekursy.test.helper;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostScope;
import pl.twojekursy.post.PostStatus;
import pl.twojekursy.user.User;
import pl.twojekursy.user.UserRole;

import java.util.function.Consumer;

@Component
public class UserCreator {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser() {
        return createUser("user", UserRole.USER);
    }

    @Transactional
    public User createAdmin() {
        return createUser("admin", UserRole.ADMIN);
    }

    @Transactional
    public User createUser(String login, UserRole role) {
        User user = User.builder()
                .login(login)
                .role(role)
                .password(passwordEncoder.encode("haselko"))
                .build();
        entityManager.persist(user);
        return user;
    }

}
