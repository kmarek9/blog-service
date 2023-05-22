package pl.twojekursy.comment;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.twojekursy.BaseUnitTest;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostScope;
import pl.twojekursy.post.PostService;
import pl.twojekursy.post.PostStatus;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.twojekursy.comment.ReadCommentResponse.*;

class CommentServiceTest extends BaseUnitTest {
    @InjectMocks
    private CommentService underTest;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostService postService;

    @Test
    void givenCommentIdNotExist_whenFindById_thenEntityNotFoundException() {
        // given
        Long expectedCommentId = 3445L;

        when(commentRepository.findByIdFetchPost(expectedCommentId)).thenReturn(Optional.empty());

        // when
        Executable executable = () -> underTest.findById(expectedCommentId);

        // then
        Assertions.assertThrows(EntityNotFoundException.class, executable);
    }

    @Test
    void givenCommentExists_whenFindById_thenReturnResponse() {
        // given
        Long expectedCommentId = 3445L;
        String expectedCommentText = "tttttt";
        String expectedCommentAuthor = "aaaauthor";
        LocalDateTime expectedCommentCreatedDateTime = LocalDateTime.now();

        long expectedPostId = 123L;
        int expectedPostVersion = 0;
        String expectedPostText = "postText";
        LocalDateTime expectedPostCreatedDateTime = LocalDateTime.of(2023, 5, 22, 10, 15, 23);
        String expectedPostAuthor = "postAuthor";
        PostScope expectedPostScope = PostScope.PUBLIC;
        LocalDateTime expectedPublicationDate = LocalDateTime.now();
        PostStatus expectedPostStatus = PostStatus.ACTIVE;

        Post post = Post.builder()
                .id(expectedPostId)
                .version(expectedPostVersion)
                .text(expectedPostText)
                .createdDateTime(expectedPostCreatedDateTime)
                .scope(expectedPostScope)
                .author(expectedPostAuthor)
                .publicationDate(expectedPublicationDate)
                .status(expectedPostStatus)
                .build();

        Comment comment = Comment.builder()
                .id(expectedCommentId)
                .text(expectedCommentText)
                .createdDateTime(expectedCommentCreatedDateTime)
                .author(expectedCommentAuthor)
                .post(post)
                .build();

        when(commentRepository.findByIdFetchPost(expectedCommentId)).thenReturn(Optional.of(comment));

        // when
        ReadCommentResponse response = underTest.findById(expectedCommentId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(expectedCommentId);
        assertThat(response.getText()).isEqualTo(expectedCommentText);
        assertThat(response.getCreatedDateTime()).isEqualToIgnoringNanos(expectedCommentCreatedDateTime);
        assertThat(response.getAuthor()).isEqualTo(expectedCommentAuthor);

        assertThat(response)
                .extracting("id", "text")
                .containsExactly(expectedCommentId, expectedCommentText);

        assertThat(response)
                .extracting(ReadCommentResponse::getId, ReadCommentResponse::getText)
                .containsExactly(expectedCommentId, expectedCommentText);

        PostResponse responsePost = response.getPost();
        assertThat(responsePost).isNotNull();
        assertThat(responsePost.getId()).isEqualTo(expectedPostId);
        assertThat(responsePost.getText()).isEqualTo(expectedPostText);
        assertThat(responsePost.getCreatedDateTime()).isEqualToIgnoringNanos(expectedPostCreatedDateTime);

        assertThat(responsePost)
                .extracting(PostResponse::getId,
                        PostResponse::getVersion,
                        PostResponse::getText,
                        PostResponse::getCreatedDateTime,
                        PostResponse::getScope,
                        PostResponse::getAuthor,
                        PostResponse::getPublicationDate,
                        PostResponse::getStatus)
                .containsExactly(expectedPostId,
                        expectedPostVersion,
                        expectedPostText,
                        expectedPostCreatedDateTime,
                        expectedPostScope,
                        expectedPostAuthor,
                        expectedPublicationDate,
                        expectedPostStatus
                );
    }
}