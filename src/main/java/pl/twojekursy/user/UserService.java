package pl.twojekursy.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.groupinfo.CreateGroupInfoRequest;
import pl.twojekursy.groupinfo.GroupInfo;
import pl.twojekursy.groupinfo.GroupInfoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public void create(CreateUserRequest userRequest){
        User user = User.builder()
                .login(userRequest.getLogin())
                .build();

        userRepository.save(user);
    }
}

