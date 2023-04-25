package pl.twojekursy.comment;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.twojekursy.post.CreatePostRequest;
import pl.twojekursy.post.ReadPostResponse;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public void create(@Valid @RequestBody CreateCommentRequest createCommentRequest){
        commentService.create(createCommentRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadCommentResponse> read(@PathVariable("id") Long id){
        ReadCommentResponse comment = commentService.findById(id);
        return ResponseEntity.ok(comment);
    }
}
