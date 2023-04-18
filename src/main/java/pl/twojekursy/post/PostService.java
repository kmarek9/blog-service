package pl.twojekursy.post;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import pl.twojekursy.util.LogUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void create(CreatePostRequest postRequest){
        Post post = new Post(
                postRequest.getText(),
                postRequest.getScope(),
                postRequest.getAuthor(),
                postRequest.getPublicationDate()
        );

        postRepository.save(post);
    }

    public ReadPostResponse findById(Long id) {
        return postRepository.findById(id)
                .map(ReadPostResponse::from)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void update(Long id, UpdatePostRequest updatePostRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Post newPost = new Post(post);
        newPost.setText(updatePostRequest.getText());
        newPost.setScope(updatePostRequest.getScope());
        newPost.setVersion(updatePostRequest.getVersion());

        postRepository.save(newPost);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

    public void archive(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Post newPost = new Post(post);
        newPost.setStatus(PostStatus.DELETED);
        postRepository.save(newPost);
    }

    public Page<FindPostResponse> find(FindPostRequest findPostRequest, Pageable pageable) {
        Specification<Post> specification = preparePostSpecificationUsingPredicates(findPostRequest);
        return postRepository.findAll(specification, pageable)
                .map(FindPostResponse::from);
    }

    private static Specification<Post> preparePostSpecificationUsingPredicates(FindPostRequest findPostRequest) {
        return (root, query, criteriaBuilder) ->
        {
            List<Predicate> predicates = new ArrayList<>();

            if(findPostRequest.postStatuses()!=null ) {
                predicates.add(root.get("status").in(findPostRequest.postStatuses()));
            }

            if(findPostRequest.text()!=null) {
                predicates.add(criteriaBuilder.like(root.get("text"), "%" + findPostRequest.text() + "%"));
            }

            if(findPostRequest.publicationDate()!=null) {
                Predicate publicationDateIsNullPred = criteriaBuilder.isNull(root.get("publicationDate"));
                Predicate publicationDateLEPred = criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"), findPostRequest.publicationDate());
                predicates.add(criteriaBuilder.or(publicationDateLEPred, publicationDateIsNullPred));
            }

            if(findPostRequest.createdDateTimeMax()!=null && findPostRequest.createdDateTimeMin()!=null) {
                predicates.add(criteriaBuilder.between(root.get("createdDateTime"),
                        findPostRequest.createdDateTimeMin(),
                        findPostRequest.createdDateTimeMax())
                );
            }

            return criteriaBuilder.and( predicates.toArray(new Predicate[0]));
        };
    }

    private static Specification<Post> preparePostSpecification() {
        Specification<Post> statusInSpec = (root, query, criteriaBuilder) ->
                root.get("status").in(Set.of(PostStatus.DELETED, PostStatus.ACTIVE)) ;

        Specification<Post> textLikeSpec = (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("text"), "%"+ "POST" +"%") ;

        Specification<Post> publicationDateSpec = (root, query, criteriaBuilder) ->
        {
            Predicate publicationDateIsNullPred = criteriaBuilder.isNull(root.get("publicationDate"));
            Predicate publicationDateLEPred = criteriaBuilder.lessThanOrEqualTo(root.get("publicationDate"), LocalDateTime.now());
            return criteriaBuilder.or(publicationDateLEPred, publicationDateIsNullPred);
        };

        Specification<Post> createDateTimeBetween = (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("createdDateTime"),
                        LocalDateTime.now().minusDays(1),
                        LocalDateTime.now().plusDays(1)) ;

        Specification<Post> specification = statusInSpec
                .and(textLikeSpec)
                .and(publicationDateSpec)
                .and(createDateTimeBetween);
        return specification;
    }

    public Page<FindPostResponse> find(String textContaining,
                     int page,
                     int size) {

        return postRepository.findActiveAndPublished(textContaining,
                        LocalDateTime.now(),
                        PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdDateTime"))))
                .map(FindPostResponse::from);
    }

    public void find2() {

        log(postRepository.findByStatusOrderByCreatedDateTimeDesc(PostStatus.ACTIVE), "findByStatusOrderByCreatedDateTimeDesc");

        log(postRepository.findByStatusOrderByCreatedDateTime(PostStatus.ACTIVE), "findByStatusOrderByCreatedDateTime");

        log(postRepository.findByStatus(PostStatus.ACTIVE,
                        Sort.by("createdDateTime")
                ), "findByStatus"
        );

        log(postRepository.findByStatus(PostStatus.ACTIVE,
                        Sort.by("createdDateTime", "author")
                ), "findByStatus"
        );

        log(postRepository.findByStatus(PostStatus.ACTIVE,
                        Sort.by(Sort.Order.asc("createdDateTime"), Sort.Order.desc("author"))
                ), "findByStatus"
        );

        System.out.println(postRepository.countByStatus(PostStatus.DELETED));
        System.out.println(postRepository.existsByStatus(PostStatus.ACTIVE));

        log(postRepository.findByStatusAndAuthor(PostStatus.ACTIVE, "Marek Koszałka"), "findByStatusAndAuthor");

        log(postRepository.findByStatusInAndAuthorLike(Set.of(PostStatus.ACTIVE), "Marek Koszałka"), "findByStatusInAndAuthorLike");
        log(postRepository.findByStatusInAndAuthorContaining(Set.of(PostStatus.ACTIVE), "Marek Koszałka"), "findByStatusInAndAuthorContaining");
        log(postRepository.findByStatusInAndAuthorStartingWith(Set.of(PostStatus.ACTIVE), "Marek Koszałka"), "findByStatusInAndAuthorStartingWith");

        log(postRepository.findByStatusInAndAuthorStartingWith(Set.of(), "Marek Koszałka"), "findByStatusInAndAuthorStartingWith");


        log(postRepository.findByStatusInAndCreatedDateTimeBetween(Set.of(PostStatus.DELETED),
                LocalDate.of(2023, 3, 24).atStartOfDay(),
                LocalDate.of(2023, 3, 26).atStartOfDay()
        ), "findByStatusInAndCreatedDateTimeBetween");

        log(postRepository::find, "find");


        log(()-> postRepository.findByStatusOrderByCreatedDateTimeDesc(PostStatus.DELETED), "findByStatusOrderByCreatedDateTimeDesc");
        log(()-> postRepository.findOrderByCreateDateDescending(PostStatus.DELETED), "findOrderByCreateDateDescending");

        log(()->postRepository.findByStatus(PostStatus.DELETED,
                        Sort.by(Sort.Order.desc("createdDateTime"), Sort.Order.desc("author"))
                ), "findByStatus"
        );

        log(()->postRepository.findAndSort(PostStatus.DELETED,
                        Sort.by(Sort.Order.desc("createdDateTime"), Sort.Order.desc("author"))
                ), "findByStatus"
        );

        log(()->postRepository.findByStatusInAndAuthorLike(Set.of(PostStatus.ACTIVE), "Marek Koszałka"), "findByStatusInAndAuthorLike");
        log(()->postRepository.find(Set.of(PostStatus.ACTIVE, PostStatus.DELETED), "Koszałka"), "find");

        System.out.println("=================================================================================");
        System.out.println("find");
        System.out.println(postRepository.find(1L));

        System.out.println("findOptional");
        System.out.println(postRepository.findOptional(4554L));

        System.out.println("count");
        System.out.println(postRepository.count());

        System.out.println("findIds");
        System.out.println(postRepository.findIds());

        System.out.println("findAuthors");
        System.out.println(postRepository.findAuthors());

        /*System.out.println("find po statusie");
        System.out.println(postRepository.find(PostStatus.ACTIVE));*/

        log(()->postRepository.findByStatus(PostStatus.ACTIVE, PageRequest.of(0, 2, Sort.by(Sort.Order.desc("id")))) ,"findByStatus");
        log(()->postRepository.findByStatus(PostStatus.ACTIVE, PageRequest.of(1, 2, Sort.by(Sort.Order.desc("id")))) ,"findByStatus");
        log(()->postRepository.findByStatus(PostStatus.ACTIVE, PageRequest.of(2, 2, Sort.by(Sort.Order.desc("id")))) ,"findByStatus");
        log(()->postRepository.findByStatus(PostStatus.ACTIVE, PageRequest.of(3, 2, Sort.by(Sort.Order.desc("id")))) ,"findByStatus");

        LogUtil.logPage(()->postRepository.findAllByStatus(PostStatus.ACTIVE, PageRequest.of(0, 2, Sort.by(Sort.Order.desc("id")))) ,"findAllByStatus 0");
        LogUtil.logPage(()->postRepository.findAllByStatus(PostStatus.ACTIVE, PageRequest.of(1, 2, Sort.by(Sort.Order.desc("id")))) ,"findAllByStatus 1");
        LogUtil.logPage(()->postRepository.findAllByStatus(PostStatus.ACTIVE, PageRequest.of(2, 2, Sort.by(Sort.Order.desc("id")))) ,"findAllByStatus 2");
        LogUtil.logPage(()->postRepository.findAllByStatus(PostStatus.ACTIVE, PageRequest.of(3, 2, Sort.by(Sort.Order.desc("id")))) ,"findAllByStatus 3");
    }

    private void log(List<Post> posts, String methodName){
        System.out.println("-------------------- "+ methodName +" ----------------------");
        posts.forEach(System.out::println);
    }

    private void log(Supplier<List<Post>> listSupplier, String methodName){
        System.out.println("---------------" + methodName + " ---------------------");

        listSupplier.get().forEach(System.out::println);
    }
}
