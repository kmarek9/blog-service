package pl.twojekursy.invoice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public void create(CreateInvoiceRequest invoiceRequest){
        Invoice invoice = new Invoice(
              invoiceRequest.paymentDate(),
              invoiceRequest.buyer(),
              invoiceRequest.seller()
        ) ;

        invoiceRepository.save(invoice);
    }

    public ReadInvoiceResponse findById(Long id) {
        return invoiceRepository.findById(id)
                .map(ReadInvoiceResponse::from)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void update(Long id, UpdateInvoiceRequest updateInvoiceRequest) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Invoice newInvoice = new Invoice(invoice);

        newInvoice.setPaymentDate(updateInvoiceRequest.paymentDate());
        newInvoice.setSeller(updateInvoiceRequest.seller());
        newInvoice.setBuyer(updateInvoiceRequest.buyer());
        newInvoice.setVersion(updateInvoiceRequest.version());

        invoiceRepository.save(newInvoice);
    }

    public void delete(Long id) {
        invoiceRepository.deleteById(id);
    }

    public void archive(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        Invoice newInvoice = new Invoice(invoice);

        newInvoice.setStatus(InvoiceStatus.DELETED);
        invoiceRepository.save(newInvoice);
    }

    public void find() {
        List<Invoice> invoices = invoiceRepository.findAllByPaymentDateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn(
                LocalDate.of(2023,3,28),
                LocalDate.of(2023,3,29),
                "Sel",
                Set.of(InvoiceStatus.ACTIVE, InvoiceStatus.DRAFT)
                );
        invoices.forEach(System.out::println);
    }
}
