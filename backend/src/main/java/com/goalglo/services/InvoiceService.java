package com.goalglo.services;

import com.goalglo.common.ResourceNotFoundException;
import com.goalglo.dto.InvoiceDTO;
import com.goalglo.entities.InvoiceEntity;
import com.goalglo.entities.Payment;
import com.goalglo.entities.User;
import com.goalglo.repositories.InvoiceRepository;
import com.goalglo.repositories.PaymentRepository;
import com.goalglo.repositories.UserRepository;
import com.goalglo.util.SecretConfig;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceItem;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.InvoiceCreateParams;
import com.stripe.param.InvoiceItemCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class InvoiceService {

   private final InvoiceRepository invoiceRepository;
   private final PaymentRepository paymentRepository;
   private final UserRepository userRepository;
   private final SecretConfig secretConfig;

   @Autowired
   public InvoiceService(InvoiceRepository invoiceRepository, PaymentRepository paymentRepository, UserRepository userRepository, SecretConfig secretConfig) {
      this.invoiceRepository = invoiceRepository;
      this.paymentRepository = paymentRepository;
      this.userRepository = userRepository;
      this.secretConfig = secretConfig;
   }

   /**
    * Creates and sends an invoice to a customer based on a payment.
    *
    * @param paymentId The ID of the payment associated with the invoice.
    * @param userId    The ID of the user to whom the invoice will be sent.
    * @return InvoiceDTO The created invoice details.
    * @throws StripeException If an error occurs while interacting with Stripe API.
    */
   public InvoiceDTO createAndSendInvoice(UUID paymentId, UUID userId) throws StripeException {
      Stripe.apiKey = secretConfig.getStripeApiKey();
      Payment payment = paymentRepository.findById(paymentId)
         .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
      User user = userRepository.findById(userId)
         .orElseThrow(() -> new ResourceNotFoundException("User not found"));

      // Create Stripe Customer if not already created
      String stripeCustomerId = user.getStripeCustomerId();
      if (stripeCustomerId == null || stripeCustomerId.isEmpty()) {
         stripeCustomerId = createStripeCustomer(user);
         user.setStripeCustomerId(stripeCustomerId);
         userRepository.save(user);
      }

      // Create Invoice Item in Stripe
      createStripeInvoiceItem(payment, stripeCustomerId);

      // Create and Finalize Invoice in Stripe
      String stripeInvoiceId = createAndFinalizeStripeInvoice(stripeCustomerId);
      // Save Invoice to Database
      InvoiceEntity invoiceEntity = new InvoiceEntity();
      invoiceEntity.setPayment(payment);
      invoiceEntity.setUser(user);
      invoiceEntity.setInvoiceNumber(stripeInvoiceId);
      invoiceEntity.setIssueDate(LocalDate.now());
      invoiceEntity.setDueDate(LocalDate.now().plusDays(30));
      invoiceEntity.setAmountDue(BigDecimal.valueOf(payment.getAmount()));
      invoiceEntity.setStatus("PENDING");

      invoiceEntity = invoiceRepository.save(invoiceEntity);

      return new InvoiceDTO(invoiceEntity);
   }

   /**
    * Creates a new Stripe customer for the given user.
    *
    * @param user The user for whom the Stripe customer is created.
    * @return String The Stripe customer ID.
    * @throws StripeException If an error occurs while creating the Stripe customer.
    */
   private String createStripeCustomer(User user) throws StripeException {
      CustomerCreateParams customerParams = CustomerCreateParams.builder()
         .setEmail(user.getEmail())
         .setName(user.getFirstName() + " " + user.getLastName())
         .build();
      Customer customer = Customer.create(customerParams);
      return customer.getId();
   }

   /**
    * Creates an invoice item in Stripe for the given payment and customer.
    *
    * @param payment    The payment associated with the invoice item.
    * @param customerId The Stripe customer ID to associate the invoice item with.
    * @throws StripeException If an error occurs while creating the invoice item in Stripe.
    */
   private void createStripeInvoiceItem(Payment payment, String customerId) throws StripeException {
      InvoiceItemCreateParams itemParams = InvoiceItemCreateParams.builder()
         .setCustomer(customerId)
         .setAmount(payment.getAmount())
         .setCurrency(payment.getCurrency())
         .setDescription("Service: " + payment.getService().getName())
         .build();
      InvoiceItem.create(itemParams);
   }

   /**
    * Creates and finalizes an invoice in Stripe for the given customer.
    *
    * @param customerId The Stripe customer ID to create the invoice for.
    * @return String The finalized Stripe invoice ID.
    * @throws StripeException If an error occurs while creating and finalizing the invoice in Stripe.
    */
   private String createAndFinalizeStripeInvoice(String customerId) throws StripeException {
      InvoiceCreateParams invoiceParams = InvoiceCreateParams.builder()
         .setCustomer(customerId)
         .setAutoAdvance(true)
         .build();
      Invoice invoice = com.stripe.model.Invoice.create(invoiceParams);
      Invoice finalizedInvoice = invoice.finalizeInvoice();
      return finalizedInvoice.getId();
   }
}