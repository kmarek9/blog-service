package pl.twojekursy.post;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.springframework.security.access.AccessDeniedException;
import pl.twojekursy.BaseUnitTest;
import pl.twojekursy.user.User;
import pl.twojekursy.user.UserRole;

import static org.junit.jupiter.api.Assertions.*;

class PostAuthorizationCheckerTest extends BaseUnitTest {

    @InjectMocks
    private PostAuthorizationChecker underTest;

    @ParameterizedTest
    @CsvSource(
            {
                    "10, ADMIN",
                    "10, USER",
                    "234, ADMIN"
            }
    )
    void givenAdminOrAuthor_whenCheckPermissions_thenNotThrowAccessDenied(Long id, UserRole role) {
        // given
        long authorId = 10L;

        User loggedUser = User.builder()
                .id(id)
                .role(role)
                .build();

        User author = User.builder()
                .id(authorId)
                .build();

        Post post = Post.builder()
                .id(12L)
                .user(author)
                .build();

        // when
        Executable ex = () -> underTest.checkPermissions(loggedUser, post);

        // then
        assertDoesNotThrow(ex);
    }

    @Test
    void givenNotAdminAndNotAuthor_whenCheckPermissions_thenThrowAccessDenied() {
        // given
        long authorId = 10L;

        User loggedUser = User.builder()
                .id(authorId+12)
                .role(UserRole.USER)
                .build();

        User author = User.builder()
                .id(authorId)
                .build();

        Post post = Post.builder()
                .id(12L)
                .user(author)
                .build();

        // when
        Executable ex = () -> underTest.checkPermissions(loggedUser, post);

        // then
        assertThrows(AccessDeniedException.class, ex);
    }
}