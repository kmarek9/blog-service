package pl.twojekursy.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.twojekursy.groupinfo.CreateGroupInfoRequest;
import pl.twojekursy.groupinfo.GroupInfoService;


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
}
