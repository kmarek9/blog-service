package pl.twojekursy.invoice.detail;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoice-details")
@RequiredArgsConstructor
public class InvoiceDetailController {
    private final InvoiceDetailService invoiceDetailService;

    @PostMapping
    public void create(@Valid @RequestBody CreateInvoiceDetailRequest invoiceDetailRequest){
        invoiceDetailService.create(invoiceDetailRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadInvoiceDetailResponse> read(@PathVariable("id") Long id){
        ReadInvoiceDetailResponse invoiceDetailResponse = invoiceDetailService.findById(id);
        return ResponseEntity.ok(invoiceDetailResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") Long id, @Valid @RequestBody UpdateInvoiceDetailRequest updateInvoiceDetailRequest){
        invoiceDetailService.update(id, updateInvoiceDetailRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page<ReadInvoiceDetailResponse>> find(@RequestParam(value = "inid") Long invoiceId,
                                                          Pageable pageable){
        return ResponseEntity.ok(invoiceDetailService.find(invoiceId, pageable));
    }
}
