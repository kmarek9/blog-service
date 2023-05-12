package pl.twojekursy.accountant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/attach-client")
    public void attachClient(@Valid @RequestBody AttachClientRequest attachClientRequest){
        accountantService.attachClient(attachClientRequest);
    }

    @PostMapping("/detach-client")
    public void detachClient(@Valid @RequestBody DetachClientRequest detachClientRequest){
        accountantService.detachClient(detachClientRequest);
    }

    @GetMapping
    public ResponseEntity<Page<FindAccountantResponse>> find(@RequestParam(value = "c_id") Long clientId,
                                                            Pageable pageable){
        Page<FindAccountantResponse> body = accountantService.find(clientId, pageable);
        return ResponseEntity.ok(body);
    }
}
