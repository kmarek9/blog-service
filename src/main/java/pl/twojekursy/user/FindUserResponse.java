package pl.twojekursy.user;

import lombok.Value;

@Value
public class FindUserResponse {
    String login;

    public static FindUserResponse from(User user){
        return new FindUserResponse(user.getLogin());
    }
}
