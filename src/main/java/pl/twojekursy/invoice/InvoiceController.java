package pl.twojekursy.invoice;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<ReadInvoiceResponse> read(@PathVariable("id") Long id){
        return ResponseEntity.ok(invoiceService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UpdateInvoiceRequest updateInvoiceRequest){
        invoiceService.update(id, updateInvoiceRequest);
        return ResponseEntity.ok().build();
    }
}
