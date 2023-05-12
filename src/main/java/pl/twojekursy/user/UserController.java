package pl.twojekursy.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public void create(@Valid @RequestBody CreateUserRequest userRequest){
        userService.create(userRequest);
    }

    @PostMapping("/join-group")
    public void joinToGroup(@Valid @RequestBody JoinToGroupRequest joinToGroupRequest){
        userService.joinToGroup(joinToGroupRequest);
    }

    @PostMapping("/leave-group")
    public void leaveToGroup(@Valid @RequestBody LeaveGroupRequest leaveGroupRequest){
        userService.leaveToGroup(leaveGroupRequest);
    }

    @GetMapping
    public ResponseEntity<Page<FindUserResponse>> find(@RequestParam(value = "g_id") Long groupId,
                                                       @RequestParam int page,
                                                       @RequestParam int size){
        Page<FindUserResponse> body = userService.find(groupId, page, size);
        return ResponseEntity.ok(body);
    }
}
