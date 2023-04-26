package pl.twojekursy.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public void create(@Valid @RequestBody CreateCommentRequest createCommentRequest){
        commentService.create(createCommentRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadCommentResponse> read(@PathVariable("id") Long id){
        ReadCommentResponse comment = commentService.findById(id);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UpdateCommentRequest updateCommentRequest){
        commentService.update(id, updateCommentRequest);
        return ResponseEntity.ok().build();
    }
}
