package pl.twojekursy.invoice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
}
