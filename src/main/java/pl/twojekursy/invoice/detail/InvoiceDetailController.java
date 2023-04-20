package pl.twojekursy.invoice.detail;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.twojekursy.comment.CreateCommentRequest;

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
}
