package pl.twojekursy.comment;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    @Transactional
    public void create(CreateCommentRequest createCommentRequest){
        Post post = postService.findPostById(createCommentRequest.getPostId());

        Comment comment = Comment.builder()
                .text(createCommentRequest.getText())
                .author(createCommentRequest.getAuthor())
                .post(post)
                .build();

        commentRepository.save(comment);
    }


    //@Transactional(readOnly = true)
    public ReadCommentResponse findById(Long id) {
        Optional<Comment> maybeComment = commentRepository.findByIdFetchPost(id);
        log.debug("maybeComment: {}" , maybeComment);
        Optional<ReadCommentResponse> readCommentResponse = maybeComment.map(ReadCommentResponse::from);
        log.debug("readCommentResponse: {}" , readCommentResponse);
        ReadCommentResponse comment = readCommentResponse.orElseThrow(EntityNotFoundException::new);
        log.debug("comment: {}" , comment);
        return comment;
    }

    @Transactional
    public void update(Long id, UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Comment newComment = comment.toBuilder()
                .author(updateCommentRequest.getAuthor())
                .text(updateCommentRequest.getText())
                .build();

        commentRepository.save(newComment);
    }
}
