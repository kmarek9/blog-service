package pl.twojekursy.comment;

import org.springframework.stereotype.Service;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostService;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    public CommentService(CommentRepository commentRepository, PostService postService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
    }

    public void create(CreateCommentRequest createCommentRequest){
        Post post = postService.findPostById(createCommentRequest.getPostId());

        Comment comment = new Comment(
                createCommentRequest.getText(),
                createCommentRequest.getAuthor(),
                post
        );
        commentRepository.save(comment);
    }
}
