package pl.twojekursy.accountant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/accountants")
@RequiredArgsConstructor
@Slf4j
public class AccountantController {
    private final AccountantService accountantService;

    @PostMapping
    public void create(@Valid @RequestBody CreateAccountantRequest accountantRequest){
        accountantService.create(accountantRequest);
    }
}
