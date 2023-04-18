package pl.twojekursy.post;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PutMapping("/{id}/archive")
    public ResponseEntity<Void> archive(@PathVariable("id") Long id){
        postService.archive(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //stworzyc usługę typu GET, która zwraca listę postów, które sa ACTIVE i opublikowane, posortowane od najnowszych
    //dostępna pod adresem /api/posts
    //parametry:
    //q - filtruje po polu text, usługa zwraca posty, ktore zawieraja wartosc w polu text, wartosc podaną w polu q
    //page - numer strony (numeracja od 0), wymagany
    //size - rozmiar strony, wymagany
    //
    //response:
    //id,  text skrócony do 50 znaków, datę utworzenie i autor
    @GetMapping
    public ResponseEntity<Page<FindPostResponse>> find(@RequestParam(value = "q", defaultValue = "") String textContaining,
                                           @RequestParam int page,
                                           @RequestParam int size){
        Page<FindPostResponse> body = postService.find(textContaining, page, size);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/find")
    public ResponseEntity<Page<FindPostResponse>> find(@RequestBody FindPostRequest findPostRequest, Pageable pageable){
        Page<FindPostResponse> body = postService.find(findPostRequest, pageable);
        return ResponseEntity.ok(body);
    }
}
