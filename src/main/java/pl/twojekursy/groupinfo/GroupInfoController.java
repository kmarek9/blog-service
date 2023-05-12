package pl.twojekursy.groupinfo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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
