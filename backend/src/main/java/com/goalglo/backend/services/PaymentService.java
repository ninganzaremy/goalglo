package com.goalglo.backend.services;

import com.goalglo.backend.common.ResourceNotFoundException;
import com.goalglo.backend.config.SecretConfig;
import com.goalglo.backend.dto.PaymentDTO;
import com.goalglo.backend.entities.Payment;
import com.goalglo.backend.entities.ServiceEntity;
import com.goalglo.backend.entities.Transaction;
import com.goalglo.backend.entities.User;
import com.goalglo.backend.hook.StripeWebhook;
import com.goalglo.backend.repositories.PaymentRepository;
import com.goalglo.backend.repositories.TransactionRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentService {

   private final PaymentRepository paymentRepository;
   private final StripeWebhook stripeWebhook;
   private final UserService userService;
   private final ServiceService serviceService;
   private final SecretConfig secretConfig;
   private final TransactionRepository transactionRepository;


   @Autowired
   public PaymentService(PaymentRepository paymentRepository, StripeWebhook stripeWebhook, UserService userService, ServiceService serviceService, SecretConfig secretConfig, TransactionRepository transactionRepository) {
      this.paymentRepository = paymentRepository;
      this.stripeWebhook = stripeWebhook;
      this.userService = userService;
      this.serviceService = serviceService;
      this.transactionRepository = transactionRepository;
      this.secretConfig = secretConfig;
      Stripe.apiKey = secretConfig.getStripeApiKey();
   }

   /**
    * Initiates a new payment for a given service.
    *
    * @param paymentDTO The data transfer object containing payment details.
    * @param userId     The ID of the user making the payment.
    * @return PaymentDTO     The created payment details.
    * @throws Exception Throws exception if payment fails.
    */
   public PaymentDTO initiatePayment(PaymentDTO paymentDTO, UUID userId) throws Exception {
      User user = userService.findById(userId)
         .orElseThrow(() -> new ResourceNotFoundException("User not found"));
      ServiceEntity service = serviceService.findById(paymentDTO.getServiceId())
         .orElseThrow(() -> new ResourceNotFoundException("Service not found"));
      String paymentIntentId = stripeWebhook.createPaymentIntent(paymentDTO.getAmount(), paymentDTO.getCurrency());

      Payment payment = new Payment();
      payment.setUser(user);
      payment.setService(service);
      payment.setAmount(paymentDTO.getAmount());
      payment.setCurrency(paymentDTO.getCurrency());
      payment.setPaymentMethod(paymentDTO.getPaymentMethod());
      payment.setStripePaymentId(paymentIntentId);
      payment.setStatus("INITIATED");

      payment = paymentRepository.save(payment);

      return new PaymentDTO(payment);
   }

   /**
    * Handles the payment confirmation after the payment is completed.
    *
    * @param paymentId The ID of the payment to confirm.
    * @return PaymentDTO     The confirmed payment details.
    * @throws Exception Throws exception if payment confirmation fails.
    */
   public PaymentDTO confirmPayment(UUID paymentId) throws Exception {
      Payment payment = paymentRepository.findById(paymentId)
         .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
      // Confirm payment with Stripe using dynamic paymentMethodId
      stripeWebhook.confirmPaymentIntent(payment.getStripePaymentId(), payment.getPaymentMethod(), secretConfig.getDomain() + "/paid/thanks");

      payment.setStatus("COMPLETED");
      payment = paymentRepository.save(payment);

      // Create a new transaction record
      Transaction transaction = new Transaction();
      transaction.setPayment(payment);
      transaction.setService(payment.getService());
      transaction.setAmount(BigDecimal.valueOf(payment.getAmount()));
      transactionRepository.save(transaction);

      return new PaymentDTO(payment);
   }

   /**
    * Handles Stripe webhook events to update payment status.
    *
    * @param payload   The webhook event payload received from Stripe.
    * @param sigHeader The Stripe signature header used for verifying the authenticity of the event.
    * @return String   A confirmation message indicating the result of the webhook handling process.
    */
   public String handleStripeWebhook(String payload, String sigHeader) {

      try {
         String stripeEndpointSecret = secretConfig.getStripeEndpointSecret();

         Event event = stripeWebhook.captureStripeEvent(payload, sigHeader, stripeEndpointSecret);

         // Handle different event types
         switch (event.getType()) {
            case "payment_intent.succeeded":
               handlePaymentEvent(event, "succeeded");
               break;

            case "payment_intent.payment_failed":
               handlePaymentEvent(event, "failed");
               break;

            default:
               handlePaymentEvent(event, "other");
               break;
         }
         return "Webhook event received";
      } catch (SignatureVerificationException e) {
         return "Signature verification failed";
      } catch (Exception e) {
         return "Error processing webhook";
      }
   }

   /**
    * Handles payment events from Stripe to update the payment status.
    *
    * @param event  The Stripe event received.
    * @param status The status to update the payment to (e.g., "succeeded", "failed").
    */
   public void handlePaymentEvent(Event event, String status) {
      PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
         .getObject()
         .orElseThrow(() -> new RuntimeException("Unable to deserialize payment intent"));

      // Fetch the payment record using the paymentIntent ID
      Payment payment = paymentRepository.findByStripePaymentId(paymentIntent.getId())
         .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

      payment.setStatus(status);
      paymentRepository.save(payment);
   }

}