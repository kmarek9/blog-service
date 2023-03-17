package pl.twojekursy.post;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping
    public void read(){

    }

    @PostMapping
    public void create(@RequestBody CreatePostRequest postRequest){
        postService.create(postRequest);
    }
}
