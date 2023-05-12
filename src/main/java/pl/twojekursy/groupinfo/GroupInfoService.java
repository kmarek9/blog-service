package pl.twojekursy.groupinfo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.user.FindUserResponse;
import pl.twojekursy.user.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroupInfoService {
    private final GroupInfoRepository groupInfoRepository;

    @Transactional
    public void create(CreateGroupInfoRequest groupInfoRequest){
        GroupInfo groupInfo = GroupInfo.builder()
                .name(groupInfoRequest.getName())
                .build();

        groupInfoRepository.save(groupInfo);
    }

    public GroupInfo findById(Long groupId) {
        return groupInfoRepository.findById(groupId)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<FindGroupInfoResponse> find(Long userId, Pageable pageable) {
        return groupInfoRepository.find(userId, pageable)
            .map(FindGroupInfoResponse::from);
    }
}

