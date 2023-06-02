package pl.twojekursy.post;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.twojekursy.BaseUnitTest;
import pl.twojekursy.security.LoggedUserProvider;
import pl.twojekursy.user.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PostServiceTest extends BaseUnitTest {
    @InjectMocks
    private PostService underTest;

    @Mock
    private PostRepository postRepository;

    @Mock
    private LoggedUserProvider loggedUserProvider;

    @Captor
    private ArgumentCaptor<Post> postCaptor;

    @Test
    void givenCorrectRequest_whenCreate_thenCreatePost() {
        // given
        String text = "text";
        PostScope scope = PostScope.PUBLIC;
        LocalDateTime publicationDate = LocalDateTime.now().plusDays(1).truncatedTo(ChronoUnit.SECONDS);

        CreatePostRequest request = new CreatePostRequest(
                text,
                scope,
                publicationDate
        );

        String login = "loginek";
        User user = User.builder()
                .id(23L)
                .login(login)
                .build();
        when(loggedUserProvider.provideLoggedUser()).thenReturn(user);

        // when
        underTest.create(request);

        // then
        verify(postRepository).save(postCaptor.capture());

        Post post = postCaptor.getValue();
        assertThat(post).isNotNull();
        assertThat(post.getId()).isNull();
        assertThat(post.getVersion()).isNull();
        assertThat(post.getCreatedDateTime()).isNull();
        assertThat(post.getLastModifiedDateTime()).isNull();
        assertThat(post.getText()).isEqualTo(text);
        assertThat(post.getPublicationDate()).isEqualToIgnoringNanos(publicationDate);
        assertThat(post.getScope()).isEqualTo(scope);
        assertThat(post.getStatus()).isEqualTo(PostStatus.ACTIVE);
        assertThat(post.getAuthor()).isEqualTo(login);
        assertThat(post.getUser()).isEqualTo(user);

    }

    @Test
    void givenNoResults_whenFindAll_thenReturnEmptyPage() {
        //given
        FindPostRequest request = new FindPostRequest(null,
                null,
                null,
                null,
                null);

        int expectedPageSize = 10;
        Pageable pageable = Pageable.ofSize(expectedPageSize);
        when(postRepository.findAll(any(), eq(pageable))).thenReturn(Page.empty(pageable));

        // when
        Page<FindPostResponse> response = underTest.find(request, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEmpty();
        assertThat(response.getSize()).isEqualTo(expectedPageSize);
    }

    @Test
    void givenTwoResults_whenFindAll_thenReturnResponseInCorrectOrder() {
        //given
        FindPostRequest request = new FindPostRequest(null,
                null,
                null,
                null,
                null);

        long post1Id = 1L;
        long post2Id = 2L;

        int expectedPageSize = 10;
        Pageable pageable = Pageable.ofSize(expectedPageSize);
        Post post1 = Post.builder()
                .id(post1Id)
                .comments(Set.of())
                .build();
        Post post2 = Post.builder()
                .id(post2Id)
                .comments(Set.of())
                .build();

        List<Post> postList = List.of(post1, post2);
        when(postRepository.findAll(any(), eq(pageable))).thenReturn(new PageImpl<>(
                postList, pageable, postList.size()
        ));

        // when
        Page<FindPostResponse> response = underTest.find(request, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getContent())
                .extracting(FindPostResponse::id)
                .containsExactly(post1Id, post2Id);
        assertThat(response.getSize()).isEqualTo(expectedPageSize);
    }
}