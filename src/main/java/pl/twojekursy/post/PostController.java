package pl.twojekursy.post;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public void read(){
        Optional<Post> optionalPost = postRepository.findById(5L);
        Post post = optionalPost.get();
        System.out.println(post);

        //--------

        postRepository.delete(post);

        postRepository.deleteById(1L);

        postRepository.deleteAllById(Set.of(8L, 19L));

        //---------
        System.out.println("-----  findAll ------");
        postRepository.findAll().forEach(System.out::println);

        System.out.println("-----  findAll ------");
        postRepository.findAllById(List.of(8L, 2L, 4L, 4566L)).forEach(System.out::println);

        //Optional<Post> optionalPost4 = postRepository.findById(4L);
        Post post4 = new Post();
        post4.setId(4L);
        post4.setAuthor("Zmieniony Marek");
        postRepository.save(post4);

        Optional<Post> optionalPost3 = postRepository.findById(3L);
        Post post3 = optionalPost3.get();
        post3.setAuthor("Zmieniony Marek3");
        post3.setScope(null);
        postRepository.save(post4);
    }

    @PostMapping
    public void create(){
        for (int i = 0; i < 20; i++) {
            postRepository.save(new Post(
                  null,
                    "Przykladowy tekst" +i,
                    LocalDateTime.now(),
                    (i%2==0)? PostScope.PRIVATE : PostScope.PUBLIC,
                    "Marek"+i,
                    null,
                    (i<10)? PostStatus.ACTIVE : PostStatus.DELETED
            ));
        }
    }
}
