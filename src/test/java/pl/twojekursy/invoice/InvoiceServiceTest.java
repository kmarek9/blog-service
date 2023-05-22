package pl.twojekursy.invoice;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.twojekursy.BaseUnitTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class InvoiceServiceTest extends BaseUnitTest {

    @InjectMocks
    private InvoiceService underTest;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Test
    void givenNoResults_whenFindAll_thenReturnEmptyPage() {
        //given
        FindInvoiceRequest request = new FindInvoiceRequest(null,
                null,
                null,
                null);

        int expectedPageSize = 10;
        Pageable pageable = Pageable.ofSize(expectedPageSize);
        when(invoiceRepository.findAll(any(), eq(pageable))).thenReturn(Page.empty(pageable));

        // when
        Page<FindInvoiceResponse> response = underTest.find(request, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEmpty();
        assertThat(response.getSize()).isEqualTo(expectedPageSize);
    }

    @Test
    void givenTwoResults_whenFindAll_thenReturnResponseInCorrectOrder() {
        //given
        FindInvoiceRequest request = new FindInvoiceRequest(LocalDate.of(2023, 5,22),
                LocalDate.of(2023, 5,26),
                "selller",
                Set.of(InvoiceStatus.ACTIVE));

        long invoice1Id = 1L;
        long invoice2Id = 2L;

        int expectedPageSize = 10;
        Pageable pageable = Pageable.ofSize(expectedPageSize);
        Invoice invoice1 = Invoice.builder()
                .id(invoice1Id)
                .invoiceDetails(Set.of())
                .build();

        Invoice invoice2 = Invoice.builder()
                .id(invoice2Id)
                .invoiceDetails(Set.of())
                .build();

        List<Invoice> invoices = List.of(invoice1, invoice2);
        when(invoiceRepository.findAll(any(), eq(pageable))).thenReturn(new PageImpl<>(
                invoices, pageable, invoices.size()
        ));

        // when
        Page<FindInvoiceResponse> response = underTest.find(request, pageable);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(2);
        assertThat(response.getContent())
                .extracting(FindInvoiceResponse::id)
                .containsExactly(invoice1Id, invoice2Id);
        assertThat(response.getSize()).isEqualTo(expectedPageSize);
    }
}