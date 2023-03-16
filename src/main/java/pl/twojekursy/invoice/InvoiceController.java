package pl.twojekursy.invoice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    private final InvoiceRepository invoiceRepository;

    public InvoiceController(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }


    @PostMapping
    public void create(){
        for (int i = 0; i < 20; i++) {
            invoiceRepository.save(new Invoice(
                 null,
                 LocalDateTime.now(),
                 LocalDate.of(2023, 4,1),
                    "Firma1",
                    "Firma2",
                    (i<10) ? InvoiceStatus.ACTIVE : InvoiceStatus.DELETED
            ));
        }
    }

    @GetMapping
    public void read(){
        //R
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(6L);
        Invoice invoice = optionalInvoice.get();
        System.out.println(invoice);

        //D
        invoiceRepository.deleteById(5L);
        invoiceRepository.delete(invoice);
        invoiceRepository.deleteAllById(Set.of(10L, 11L));

        //find - list
        invoiceRepository.findAll().forEach(System.out::println);
        invoiceRepository.findAllById(List.of(19L, 20L)).forEach(System.out::println);

        //U
        Optional<Invoice> optionalInvoice1 = invoiceRepository.findById(1L);
        Invoice invoice1 = optionalInvoice1.get();
        invoice1.setStatus(InvoiceStatus.DELETED);
        invoiceRepository.save(invoice1);

        System.out.println(invoiceRepository.count());
        System.out.println(invoiceRepository.existsById(10L));
        System.out.println(invoiceRepository.existsById(19L));
    }
}
