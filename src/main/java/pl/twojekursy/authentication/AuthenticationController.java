package pl.twojekursy.authentication;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.twojekursy.user.CreateUserRequest;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @PostMapping
    public void create(@Valid @RequestBody AuthenticateRequest authenticateRequest){
        authenticationService.authenticate(authenticateRequest);
    }
}
