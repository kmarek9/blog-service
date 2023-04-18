package pl.twojekursy.invoice;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PutMapping("/{id}/archive")
    public ResponseEntity<Void> archive(@PathVariable("id") Long id){
        invoiceService.archive(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id){
        invoiceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //stworzyc usługę typu GET, która zwraca listę invoicow, które sa ACTIVE lub DRAFT ,
    // posortowane po dacie platnosci rosnaco
    //dostępna pod adresem /api/invoices
    //parametry:
    //s - filtruje po polu seller, usługa zwraca invoicy, ktore zawieraja w polu seller wartosc podaną w polu s
    //b - filtruje po polu buyer, usługa zwraca invoicy, ktore zawieraja w polu buyer wartosc podaną w polu b
    //page - numer strony (numeracja od 0), wymagany
    //size - rozmiar strony, wymagany
    //
    //response:
    //id,  seller, buyer, paymentDate
    @GetMapping
    public ResponseEntity<Page<FindInvoiceResponse>> find(@RequestParam(value = "s", defaultValue = "") String sellerContaining,
                                                          @RequestParam(value = "b", defaultValue = "") String buyerContaining,
                                                          @RequestParam int page,
                                                          @RequestParam int size){
        return ResponseEntity.ok(invoiceService.find(sellerContaining, buyerContaining,page, size));
    }

    //stworzyc usługę typu POST, która zwraca listę invoicow
    //dostępna pod adresem /api/invoices/find
    //parametry:
    //pageable
    //
    //response:
    //Page<FindInvoiceResponse>
    //przerób InvoiceRepository tak aby miał metody przyjmujące Specification i Pageable
    //Stwórz w InvoiceService metodę find(Pageable pageable)
    //zbuduj w niej specyfikację która wytworzy zapytanie  (bez lowerów)
    //"select i from Invoice i where i.paymentDate between :paymentStartDate and :paymentEndDate " +
    //        "and lower(i.seller) like lower(:seller) " +
    //        "and i.status in :statuses "
    @PostMapping("/find")
    public ResponseEntity<Page<FindInvoiceResponse>> find(Pageable pageable) {
        return ResponseEntity.ok(invoiceService.find(pageable));
    }
}
