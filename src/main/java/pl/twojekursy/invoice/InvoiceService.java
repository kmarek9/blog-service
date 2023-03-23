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
}
