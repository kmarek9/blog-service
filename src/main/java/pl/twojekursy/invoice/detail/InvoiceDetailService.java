package pl.twojekursy.invoice.detail;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.invoice.Invoice;
import pl.twojekursy.invoice.InvoiceService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceDetailService {
    private final InvoiceDetailRepository invoiceDetailRepository;

    private final InvoiceService invoiceService;

    @Transactional
    public void create(CreateInvoiceDetailRequest invoiceDetailRequest){
        Invoice invoice = invoiceService.findInvoiceById(invoiceDetailRequest.getInvoiceId());

        InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                .invoice(invoice)
                .price(invoiceDetailRequest.getPrice())
                .productName(invoiceDetailRequest.getProductName())
                .build();
        //wypisac invoiceDetail poziom debug
        log.debug("invoiceDetail: {}", invoiceDetail);

        //wypisać wynik 'save' na poziom info
        log.info("wynik save: {}", invoiceDetailRepository.save(invoiceDetail));
    }

    public ReadInvoiceDetailResponse findById(Long id) {
        Optional<InvoiceDetail> maybeInvoiceDetail = invoiceDetailRepository.findByIdFetchInvoice(id);
        Optional<ReadInvoiceDetailResponse> readInvoiceDetailResponse = maybeInvoiceDetail.map(ReadInvoiceDetailResponse::from);
        return readInvoiceDetailResponse.orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void update(Long id, UpdateInvoiceDetailRequest updateInvoiceDetailRequest) {
        InvoiceDetail invoiceDetail = invoiceDetailRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        InvoiceDetail newInvoiceDetail = invoiceDetail.toBuilder()
                .version(updateInvoiceDetailRequest.getVersion())
                .productName(updateInvoiceDetailRequest.getProductName())
                .price(updateInvoiceDetailRequest.getPrice())
                .build();

/*        InvoiceDetail newInvoiceDetail = new InvoiceDetail(invoiceDetail.getId(),
                updateInvoiceDetailRequest.getVersion(),
                invoiceDetail.getCreatedDate(),
                invoiceDetail.getLastModifiedDate(),
                updateInvoiceDetailRequest.getProductName(),
                updateInvoiceDetailRequest.getPrice(),
                invoiceDetail.getInvoice());*/

        invoiceDetailRepository.save(newInvoiceDetail);
    }
}
