package pl.twojekursy.client;

import lombok.Value;

@Value
public class FindClientResponse {
    Long id;
    String name;

    public static FindClientResponse from(Client client){
        return new FindClientResponse(client.getId(), client.getName());
    }
}
