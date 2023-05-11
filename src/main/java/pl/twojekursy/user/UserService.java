package pl.twojekursy.user;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.groupinfo.GroupInfo;
import pl.twojekursy.groupinfo.GroupInfoService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final GroupInfoService groupInfoService;

    @Transactional
    public void create(CreateUserRequest userRequest){
        User user = User.builder()
                .login(userRequest.getLogin())
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void joinToGroup(JoinToGroupRequest joinToGroupRequest) {
        User user = userRepository.findById(joinToGroupRequest.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        GroupInfo groupInfo = groupInfoService.findById(joinToGroupRequest.getGroupId());

        user.getGroupsInfo().add(groupInfo);
    }
}

