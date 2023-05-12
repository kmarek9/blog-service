package pl.twojekursy.accountant;

import lombok.Value;
import pl.twojekursy.groupinfo.GroupInfo;

@Value
public class FindAccountantResponse {
    Long id;
    String name;

    public static FindAccountantResponse from(Accountant accountant){
        return new FindAccountantResponse(accountant.getId(), accountant.getName());
    }
}
