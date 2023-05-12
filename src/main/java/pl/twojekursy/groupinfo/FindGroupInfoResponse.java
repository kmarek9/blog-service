package pl.twojekursy.groupinfo;

import lombok.Value;
import pl.twojekursy.user.User;

@Value
public class FindGroupInfoResponse {
    Long id;
    String name;

    public static FindGroupInfoResponse from(GroupInfo groupInfo){
        return new FindGroupInfoResponse(groupInfo.getId(), groupInfo.getName());
    }
}
