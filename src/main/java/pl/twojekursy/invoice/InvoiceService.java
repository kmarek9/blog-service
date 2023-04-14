package pl.twojekursy.invoice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostService;
import pl.twojekursy.util.LogUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

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

        log(() -> invoiceRepository.findByPaymentDateLessThanEqualOrderByPaymentDateDesc(
                LocalDate.of(2023,3,28)
            ) ,
                "findByPaymentDateLessThenEqualOrderByPaymentDateDesc"
        );

        log(() -> invoiceRepository.findAndOrderByPaymentDateDesc(
                        LocalDate.of(2023,3,28)
                ) ,
                "findAndOrderByPaymentDateDesc"
        );

        log(() -> invoiceRepository.findByPaymentDateLessThanEqual(
                        LocalDate.of(2023,3,28),
                        Sort.by(Sort.Order.desc("paymentDate"),
                                Sort.Order.asc("id")
                                )
                ) ,
                "findByPaymentDateLessThenEqualOrderByPaymentDateDesc"
        );

        log(() -> invoiceRepository.findByAndSort(
                        LocalDate.of(2023,3,28),
                        Sort.by(Sort.Order.desc("paymentDate"),
                                Sort.Order.asc("id")
                        )
                ) ,
                "findByAndSort"
        );


        log(()-> invoiceRepository.findAllByPaymentDateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn(
                LocalDate.of(2023,3,28),
                LocalDate.of(2023,3,29),
                "Sel",
                Set.of(InvoiceStatus.ACTIVE, InvoiceStatus.DRAFT)
                ),
                "findAllByPaymentDateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn"
        );


        log(()-> invoiceRepository.findBy(
                        LocalDate.of(2023,3,28),
                        LocalDate.of(2023,3,29),
                        "Sel%",
                        Set.of(InvoiceStatus.ACTIVE, InvoiceStatus.DRAFT)
                ),
                "findBy"
        );

        //zad dom
        //skopiowac poniższą metodą i dodać stronicowanie i zwracać Page
        //w serwisie wywołac 4x
        // - dla daty 2023-03-28 ,
        // - okno o wielkosci 3
        // - posortować po seller malejaco
        // - numer strony 0, 1,2, 3 i obejrzec wyniki i zapytania
        LogUtil.logPage(() -> invoiceRepository.findByAndSort(
                LocalDate.of(2023,3,28),
                PageRequest.of(0,3,Sort.by(Sort.Order.desc("seller")))
        ), "findByAndSort");

        LogUtil.logPage(() -> invoiceRepository.findByAndSort(
                LocalDate.of(2023,3,28),
                PageRequest.of(1,3,Sort.by(Sort.Order.desc("seller")))
        ), "findByAndSort");

        LogUtil.logPage(() -> invoiceRepository.findByAndSort(
                LocalDate.of(2023,3,28),
                PageRequest.of(2,3,Sort.by(Sort.Order.desc("seller")))
        ), "findByAndSort");

        LogUtil.logPage(() -> invoiceRepository.findByAndSort(
                LocalDate.of(2023,3,28),
                PageRequest.of(3,3,Sort.by(Sort.Order.desc("seller")))
        ), "findByAndSort");

    }

    private void log(Supplier<List<Invoice>> listSupplier, String methodName){
        System.out.println("---------------" + methodName + " ---------------------");

        listSupplier.get().forEach(System.out::println);
    }
}
