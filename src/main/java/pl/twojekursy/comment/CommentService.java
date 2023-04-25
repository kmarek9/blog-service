package pl.twojekursy.comment;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostService;

import java.util.Optional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    @Transactional
    public void create(CreateCommentRequest createCommentRequest){
        Post post = postService.findPostById(createCommentRequest.getPostId());

        Comment comment = new Comment(
                createCommentRequest.getText(),
                createCommentRequest.getAuthor(),
                post
        );
        commentRepository.save(comment);
    }


    //@Transactional(readOnly = true)
    public ReadCommentResponse findById(Long id) {
        Optional<Comment> maybeComment = commentRepository.findByIdFetchPost(id);
        Optional<ReadCommentResponse> readCommentResponse = maybeComment.map(ReadCommentResponse::from);
        ReadCommentResponse comment = readCommentResponse.orElseThrow(EntityNotFoundException::new);
        return comment;
    }

    @Transactional
    public void update(Long id, UpdateCommentRequest updateCommentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Comment newComment = new Comment(comment);
        newComment.setAuthor(updateCommentRequest.getAuthor());
        newComment.setText(updateCommentRequest.getText());

        Comment c = commentRepository.save(newComment);
    }
}
