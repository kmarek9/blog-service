package pl.twojekursy.groupinfo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.twojekursy.comment.CommentService;
import pl.twojekursy.comment.CreateCommentRequest;
import pl.twojekursy.comment.ReadCommentResponse;
import pl.twojekursy.comment.UpdateCommentRequest;


@RestController
@RequestMapping("/api/groups-info")
@RequiredArgsConstructor
@Slf4j
public class GroupInfoController {
    private final GroupInfoService groupInfoService;

    @PostMapping
    public void create(@Valid @RequestBody CreateGroupInfoRequest groupInfoRequest){
        groupInfoService.create(groupInfoRequest);
    }
}
