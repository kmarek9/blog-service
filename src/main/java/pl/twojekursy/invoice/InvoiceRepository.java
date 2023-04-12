package pl.twojekursy.invoice;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
    /*    zad dom
        1. dodac metodę która umożliwia filtrowac po dacie płatności w taki sposób,
        że 'paymentData<=wskazana data' oraz sortować po dacie płatności malejacym

        2. dodac metodę która umożliwia filtrowac po dacie płatności w taki sposób,
        że 'paymentData<=wskazana data' oraz sortować po dowolnych polach encji
     */

    List<Invoice> findByPaymentDateLessThanEqualOrderByPaymentDateDesc(LocalDate maxPaymentDate);

    List<Invoice> findByPaymentDateLessThanEqual(LocalDate maxPaymentDate, Sort sort);

    List<Invoice> findAllByPaymentDateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn(LocalDate paymentStartDate,
                                                                                        LocalDate paymentEndDate,
                                                                                        String seller,
                                                                                        Set<InvoiceStatus> statuses);
}
