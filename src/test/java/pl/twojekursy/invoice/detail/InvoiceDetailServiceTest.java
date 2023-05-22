package pl.twojekursy.invoice.detail;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.twojekursy.BaseUnitTest;
import pl.twojekursy.invoice.Invoice;
import pl.twojekursy.invoice.InvoiceService;
import pl.twojekursy.invoice.InvoiceStatus;
import pl.twojekursy.invoice.ReadInvoiceResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

class InvoiceDetailServiceTest extends BaseUnitTest {
    @InjectMocks
    private InvoiceDetailService underTest;

    @Mock
    private InvoiceDetailRepository invoiceDetailRepository;

    @Mock
    private InvoiceService invoiceService;

    @Test
    void givenInvoiceDetailIdNotExist_whenFindById_thenEntityNotFoundException() {
        // given
        Long expectedInvoiceDetailId = 3445L;

        when(invoiceDetailRepository.findByIdFetchInvoice(expectedInvoiceDetailId)).thenReturn(Optional.empty());

        // when
        Executable executable = () -> underTest.findById(expectedInvoiceDetailId);

        // then
        Assertions.assertThrows(EntityNotFoundException.class, executable);
    }

    @Test
    void givenInvoiceDetailIdExists_whenFindById_thenReturnResponse() {
        // given
        Long expectedInvoiceDetailId = 3445L;
        int expectedInvDetVersion = 0;
        LocalDateTime expectedInvDetCreatedDate = LocalDateTime.now();
        String expectedInvDetProductName = "prName";
        BigDecimal expectedInvDetPrice = BigDecimal.TEN;

        long expectedInvoiceId = 345346L;
        int expectedInvoiceVersion = 2;
        LocalDateTime expectedInvoiceCreatedDate = LocalDateTime.now();
        LocalDate expectedInvoicePaymentDate = LocalDate.now();
        String expectedInvoiceBuyer = "buyer";
        String expectedInvoiceSeller = "seller";
        InvoiceStatus expectedInvoiceStatus = InvoiceStatus.DELETED;

        Invoice invoice = Invoice.builder()
                .id(expectedInvoiceId)
                .version(expectedInvoiceVersion)
                .createdDate(expectedInvoiceCreatedDate)
                .paymentDate(expectedInvoicePaymentDate)
                .buyer(expectedInvoiceBuyer)
                .seller(expectedInvoiceSeller)
                .status(expectedInvoiceStatus)
                .build();

        InvoiceDetail invoiceDetail = InvoiceDetail.builder()
                .id(expectedInvoiceDetailId)
                .version(expectedInvDetVersion)
                .createdDate(expectedInvDetCreatedDate)
                .productName(expectedInvDetProductName)
                .price(expectedInvDetPrice)
                .invoice(invoice)
                .build();

        when(invoiceDetailRepository.findByIdFetchInvoice(expectedInvoiceDetailId)).thenReturn(Optional.of(invoiceDetail));

        // when
        ReadInvoiceDetailResponse response = underTest.findById(expectedInvoiceDetailId);

        // then
        assertThat(response).isNotNull();
        assertThat(response)
                .extracting(ReadInvoiceDetailResponse::getId,
                        ReadInvoiceDetailResponse::getVersion,
                        ReadInvoiceDetailResponse::getCreatedDate,
                        ReadInvoiceDetailResponse::getProductName,
                        ReadInvoiceDetailResponse::getPrice
                )
                .containsExactly(
                        expectedInvoiceDetailId,
                        expectedInvDetVersion,
                        expectedInvDetCreatedDate,
                        expectedInvDetProductName,
                        expectedInvDetPrice
                );

        ReadInvoiceResponse responseInvoice = response.getInvoice();
        assertThat(responseInvoice).isNotNull();
        assertThat(responseInvoice)
                .extracting(
                        ReadInvoiceResponse::id,
                        ReadInvoiceResponse::version,
                        ReadInvoiceResponse::createdDate,
                        ReadInvoiceResponse::paymentDate,
                        ReadInvoiceResponse::buyer,
                        ReadInvoiceResponse::seller,
                        ReadInvoiceResponse::status
                )
                .containsExactly(
                        expectedInvoiceId,
                        expectedInvoiceVersion,
                        expectedInvoiceCreatedDate,
                        expectedInvoicePaymentDate,
                        expectedInvoiceBuyer,
                        expectedInvoiceSeller,
                        expectedInvoiceStatus
                );

    }
}