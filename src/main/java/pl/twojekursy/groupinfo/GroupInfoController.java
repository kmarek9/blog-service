package pl.twojekursy.groupinfo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.twojekursy.user.FindUserResponse;


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

    @GetMapping
    public ResponseEntity<Page<FindGroupInfoResponse>> find(@RequestParam(value = "u_id") Long userId,
                                                       Pageable pageable){
        Page<FindGroupInfoResponse> body = groupInfoService.find(userId, pageable);
        return ResponseEntity.ok(body);
    }
}
