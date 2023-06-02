package pl.twojekursy.comment;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostService;
import pl.twojekursy.security.LoggedUserProvider;
import pl.twojekursy.user.User;
import pl.twojekursy.util.SpecificationUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    private final LoggedUserProvider loggedUserProvider;

    @Transactional
    public void create(CreateCommentRequest createCommentRequest){
        User user = loggedUserProvider.provideLoggedUser();

        Post post = postService.findPostById(createCommentRequest.getPostId());

        Comment comment = Comment.builder()
                .text(createCommentRequest.getText())
                .author(user.getLogin())
                .post(post)
                .user(user)
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

    public Page<ReadCommentResponse> find(Long postId, Pageable pageable) {
        return commentRepository.findAll(prepareSpecification(postId), pageable)
                .map(ReadCommentResponse::from);
    }

    private Specification<Comment> prepareSpecification(Long postId) {
        return (root, query, criteriaBuilder) -> {
            if(!SpecificationUtil.isCountQuery(query)) {
                root.fetch("post");
            }

            return criteriaBuilder.equal(root.get("post").get("id"), postId);
        };
    }
}

