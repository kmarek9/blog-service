package pl.twojekursy.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.groupinfo.GroupInfo;
import pl.twojekursy.groupinfo.GroupInfoService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private final GroupInfoService groupInfoService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void create(CreateUserRequest userRequest){
        User user = User.builder()
                .login(userRequest.getLogin())
                .password(passwordEncoder.encode(userRequest.getPassword()))
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

    @Transactional
    public void leaveToGroup(LeaveGroupRequest leaveGroupRequest) {
        User user = userRepository.findById(leaveGroupRequest.getUserId())
                .orElseThrow(EntityNotFoundException::new);

        GroupInfo groupInfo = groupInfoService.findById(leaveGroupRequest.getGroupId());

        user.getGroupsInfo().remove(groupInfo);
    }

    public User findById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<FindUserResponse> find(Long groupId, int page, int size) {
        Page<User> users=  userRepository.find(groupId,
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("login"))));
        return users.map(FindUserResponse::from);
    }

    public Page<FindUserResponse> find(FindUserRequest findUserRequest, Pageable pageable) {
        Page<User> users=  userRepository.findAll(prepareSpec(findUserRequest), pageable);
        return users.map(FindUserResponse::from);
    }

    private Specification<User> prepareSpec(FindUserRequest findUserRequest) {
        return (root, query, criteriaBuilder) -> {
            Join<User, GroupInfo> joinGroupsInfo = root.join("groupsInfo");

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(joinGroupsInfo.get("id"), findUserRequest.groupId()));

            if(findUserRequest.login()!=null ) {
                predicates.add(criteriaBuilder.like(root.get("login"), "%" + findUserRequest.login() + "%"));
            }

            return criteriaBuilder.and( predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

