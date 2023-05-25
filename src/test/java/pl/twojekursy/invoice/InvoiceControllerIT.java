package pl.twojekursy.invoice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import pl.twojekursy.BaseIT;
import pl.twojekursy.comment.Comment;
import pl.twojekursy.invoice.detail.InvoiceDetail;
import pl.twojekursy.post.Post;
import pl.twojekursy.post.PostScope;
import pl.twojekursy.post.UpdatePostRequest;
import pl.twojekursy.test.helper.InvoiceCreator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class InvoiceControllerIT extends BaseIT {
    private static final String API_INVOICES_PREFIX_URL = "/api/invoices";
    @Autowired
    private InvoiceCreator invoiceCreator;

    @Test
    void givenWrongRequest_whenCreate_thenBadRequest() throws Exception {
        // given
        CreateInvoiceRequest request = new CreateInvoiceRequest(
                null, null, null
        );

        // when
        ResultActions resultActions = performPost(API_INVOICES_PREFIX_URL, request);

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
        ResultActions resultActions = performPost(API_INVOICES_PREFIX_URL, request);

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


    @Test
    void givenWrongRequest_whenUpdate_thenBadRequest() throws Exception {
        // given
        UpdateInvoiceRequest request = new UpdateInvoiceRequest(null, null, null, null);
        Long id = 1000L;

        // when
        ResultActions resultActions = performPut(API_INVOICES_PREFIX_URL + "/{id}", id, request);

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.*", hasSize(4)))
                .andExpect(jsonPath("$.paymentDate").value("must not be null"))
                .andExpect(jsonPath("$.buyer").value("must not be blank"))
                .andExpect(jsonPath("$.seller").value("must not be blank"))
                .andExpect(jsonPath("$.version").value("must not be null"));
    }

    @Test
    void givenNotExistingInvoice_whenUpdate_thenNotFound() throws Exception {
        // given
        UpdateInvoiceRequest request = new UpdateInvoiceRequest(0, LocalDate.now(), "buyer", "seller");
        Long id = 1000L;

        // when
        ResultActions resultActions = performPut(API_INVOICES_PREFIX_URL + "/{id}", id, request);

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().string(is(emptyString())))
        ;
    }

    @Test
    void givenCorrectRequest_whenUpdate_thenUpdateInvoice() throws Exception {
        // given
        Invoice invoice = invoiceCreator.createInvoiceWithOneInvoiceDetail();
        Long id = invoice.getId();

        LocalDate newPaymentDate = LocalDate.now().plusDays(3);
        String newBuyer = "buyer";
        String newSeller = "seller";
        UpdateInvoiceRequest request = new UpdateInvoiceRequest(0, newPaymentDate, newBuyer, newSeller);

        // when
        ResultActions resultActions = performPut(API_INVOICES_PREFIX_URL + "/{id}", id, request);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(is(emptyString())));

        Invoice updatedInvoice = entityManager.createQuery(
                        "select i from Invoice i left join fetch i.invoiceDetails where i.id=:id",
                        Invoice.class)
                .setParameter("id", id)
                .getSingleResult();

        assertThat(updatedInvoice.getLastModifiedDate()).isAfter(invoice.getCreatedDate());
        assertThat(updatedInvoice.getCreatedDate()).isEqualToIgnoringNanos(invoice.getCreatedDate());

        assertThat(updatedInvoice)
                .extracting(
                        Invoice::getVersion,
                        Invoice::getBuyer,
                        Invoice::getSeller,
                        Invoice::getPaymentDate,
                        Invoice::getStatus
                ).containsExactly(
                        invoice.getVersion() + 1,
                        newBuyer,
                        newSeller,
                        newPaymentDate,
                        invoice.getStatus()
                );

        assertThat(updatedInvoice.getInvoiceDetails()).hasSize(1);
        InvoiceDetail invoiceDetail = updatedInvoice.getInvoiceDetails().iterator().next();
        assertThat(invoiceDetail.getLastModifiedDate()).isEqualToIgnoringNanos(invoiceDetail.getCreatedDate());
        assertThat(invoiceDetail.getVersion()).isEqualTo(invoiceDetail.getVersion());
    }

    @Test
    void givenWrongVersion_whenUpdate_thenConflict() throws Exception {
        // given
        Invoice invoice = invoiceCreator.createInvoiceWithOneInvoiceDetail();
        Long id = invoice.getId();
        int wrongVersion = invoice.getVersion() + 1;

        LocalDate newPaymentDate = LocalDate.now().plusDays(3);
        UpdateInvoiceRequest request = new UpdateInvoiceRequest(wrongVersion, newPaymentDate, "newBuyer", "newSeller");

        // when
        ResultActions resultActions = performPut(API_INVOICES_PREFIX_URL + "/{id}", id, request);

        // then
        resultActions.andExpect(status().isConflict())
                .andExpect(content().string(is(emptyString())));

        Invoice shouldntBeUpdated = entityManager.find(Invoice.class, id);

        assertThat(shouldntBeUpdated.getLastModifiedDate()).isEqualToIgnoringNanos(invoice.getCreatedDate());
        assertThat(shouldntBeUpdated.getCreatedDate()).isEqualToIgnoringNanos(invoice.getCreatedDate());

        assertThat(shouldntBeUpdated)
                .extracting(
                        Invoice::getVersion,
                        Invoice::getBuyer,
                        Invoice::getSeller,
                        Invoice::getPaymentDate,
                        Invoice::getStatus
                ).containsExactly(
                        invoice.getVersion(),
                        invoice.getBuyer(),
                        invoice.getSeller(),
                        invoice.getPaymentDate(),
                        invoice.getStatus()
                );
    }

    @Test
    void givenNotExistingInvoice_whenRead_thenNotFound() throws Exception {
        // given
        Long id = 1000L;

        // when
        ResultActions resultActions = performGet(API_INVOICES_PREFIX_URL + "/{id}", id);

        // then
        resultActions.andExpect(status().isNotFound())
                .andExpect(content().string(is(emptyString())))
        ;
    }

    @Test
    void givenExistingInvoice_whenRead_thenReturnResponse() throws Exception {
        // given
        Invoice invoice = invoiceCreator.createInvoiceWithOneInvoiceDetail();
        Long invoiceId = invoice.getId();

        // when
        ResultActions resultActions = performGet(API_INVOICES_PREFIX_URL + "/{id}", invoiceId);

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(invoice.getId().intValue()))
                .andExpect(jsonPath("$.version").value(invoice.getVersion()))
                .andExpect(jsonPath("$.paymentDate").value(invoice.getPaymentDate().toString()))
                .andExpect(jsonPath("$.buyer").value(invoice.getBuyer()))
                .andExpect(jsonPath("$.seller").value(invoice.getSeller()))
                .andExpect(jsonPath("$.status").value(invoice.getStatus().name()));

        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();

        LocalDateTime createdDateTime = parseDateTime(contentAsString, "$.createdDate");
        assertThat(createdDateTime).isEqualToIgnoringNanos(invoice.getCreatedDate());
    }
}