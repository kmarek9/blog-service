package pl.twojekursy.post;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public void create(@Valid @RequestBody CreatePostRequest postRequest){
        postService.create(postRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadPostResponse> read(@PathVariable("id") Long id){
        return ResponseEntity.ok(postService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UpdatePostRequest updatePostRequest){
        postService.update(id, updatePostRequest);
        return ResponseEntity.ok().build();
    }
}
