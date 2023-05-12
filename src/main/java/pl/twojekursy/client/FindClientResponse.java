package pl.twojekursy.client;

import lombok.Value;
import pl.twojekursy.groupinfo.GroupInfo;

@Value
public class FindClientResponse {
    Long id;
    String name;

    public static FindClientResponse from(Client client){
        return new FindClientResponse(client.getId(), client.getName());
    }
}
