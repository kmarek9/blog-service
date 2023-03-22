package pl.twojekursy.invoice;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public void create(@Valid @RequestBody CreateInvoiceRequest invoiceRequest){
        invoiceService.create(invoiceRequest);
    }

    @GetMapping
    public void read(){

    }
}
