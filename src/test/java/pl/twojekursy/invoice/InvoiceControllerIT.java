package pl.twojekursy.invoice;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import pl.twojekursy.BaseIT;
import pl.twojekursy.invoice.detail.InvoiceDetail;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InvoiceControllerIT extends BaseIT {

    @Test
    void givenWrongRequest_whenCreate_thenBadRequest() throws Exception {
        // given
        CreateInvoiceRequest request = new CreateInvoiceRequest(
                null,null,null
        );

        // when
        ResultActions resultActions = performPost("/api/invoices", request);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.seller").value("must not be blank"))
                .andExpect(jsonPath("$.buyer").value("must not be blank"))
                .andExpect(jsonPath("$.paymentDate").value("must not be null"));
    }

    @Test
    void givenCorrectRequest_whenCreate_thenCreateInvoice() throws Exception {
        // given
        LocalDate paymentDate = LocalDate.now().plusDays(1);
        String buyer = "buyer";
        String seller = "seller";

        CreateInvoiceRequest request = new CreateInvoiceRequest(
                paymentDate,
                buyer,
                seller
        );

        // when
        ResultActions resultActions = performPost("/api/invoices", request);

        // then
        resultActions.andExpect(status().isOk());

        List<Invoice> invoices = entityManager.createQuery("select i from Invoice i left join fetch i.invoiceDetails").getResultList();
        assertThat(invoices).hasSize(1);
        Invoice invoice = invoices.get(0);
        assertThat(invoice)
                .extracting(
                        Invoice::getId,
                        Invoice::getCreatedDate,
                        Invoice::getLastModifiedDate
                ).isNotNull();

        assertThat(invoice)
                .extracting(
                        Invoice::getVersion,
                        Invoice::getBuyer,
                        Invoice::getSeller,
                        Invoice::getPaymentDate,
                        Invoice::getStatus
                ).containsExactly(
                        0,
                        buyer,
                        seller,
                        paymentDate,
                        InvoiceStatus.ACTIVE
                );

        assertThat(invoice.getInvoiceDetails()).hasSize(2);
        assertThat(invoice.getInvoiceDetails())
                .extracting(InvoiceDetail::getProductName)
                .containsExactlyInAnyOrder("product1", "product2");
    }
}