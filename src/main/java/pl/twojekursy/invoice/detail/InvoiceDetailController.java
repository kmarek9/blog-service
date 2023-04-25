package pl.twojekursy.invoice.detail;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.twojekursy.comment.CreateCommentRequest;
import pl.twojekursy.comment.ReadCommentResponse;

@RestController
@RequestMapping("/api/invoice-details")
public class InvoiceDetailController {
    private final InvoiceDetailService invoiceDetailService;

    public InvoiceDetailController(InvoiceDetailService invoiceDetailService) {
        this.invoiceDetailService = invoiceDetailService;
    }

    @PostMapping
    public void create(@Valid @RequestBody CreateInvoiceDetailRequest invoiceDetailRequest){
        invoiceDetailService.create(invoiceDetailRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadInvoiceDetailResponse> read(@PathVariable("id") Long id){
        ReadInvoiceDetailResponse invoiceDetailResponse = invoiceDetailService.findById(id);
        return ResponseEntity.ok(invoiceDetailResponse);
    }
}
