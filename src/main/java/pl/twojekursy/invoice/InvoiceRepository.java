package pl.twojekursy.invoice;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
    /*    zad dom
    dodac usługe find w controllerze
    dodac metodę find w serwisie
    dodać metode w InvoiceRepository która umożliwi filtrowanie po:
    - paymentDate between
    - i Sellername zaczyna sie od (ignorujac wielkosc liter )
    - i kilku statusach IN

    */

    List<Invoice> findAllByPaymentDateBetweenAndSellerStartingWithIgnoreCaseAndStatusIn(LocalDate paymentStartDate,
                                                                                        LocalDate paymentEndDate,
                                                                                        String seller,
                                                                                        Set<InvoiceStatus> statuses);
}
