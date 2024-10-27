package com.goalglo.controllers;

import com.goalglo.dto.PaymentDTO;
import com.goalglo.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

   private final PaymentService paymentService;

   @Autowired
   public PaymentController(PaymentService paymentService) {
      this.paymentService = paymentService;
   }

   /**
    * Endpoint to initiate a payment.
    *
    * @param paymentDTO The payment details.
    * @param userId     The ID of the user making the payment.
    * @return ResponseEntity  The response containing the created payment details.
    */
   @PostMapping("/initiate")
   public ResponseEntity<PaymentDTO> initiatePayment(@RequestBody PaymentDTO paymentDTO, @RequestParam UUID userId) {
      try {
         PaymentDTO createdPayment = paymentService.initiatePayment(paymentDTO, userId);
         return new ResponseEntity<>(createdPayment, HttpStatus.CREATED);
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

   /**
    * Endpoint to confirm a payment.
    *
    * @param paymentId The ID of the payment to confirm.
    * @return ResponseEntity  The response containing the confirmed payment details.
    */
   @PostMapping("/confirm/{paymentId}")
   public ResponseEntity<PaymentDTO> confirmPayment(@PathVariable UUID paymentId) {
      try {
         PaymentDTO confirmedPayment = paymentService.confirmPayment(paymentId);
         return new ResponseEntity<>(confirmedPayment, HttpStatus.OK);
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }

   /**
    * Endpoint to handle Stripe webhooks.
    *
    * @param request The HTTP request containing the webhook payload.
    * @return ResponseEntity  The response confirming receipt of the webhook event.
    */
   @PostMapping("/webhook")
   public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) {
      String payload;
      try (Scanner scanner = new Scanner(request.getInputStream(), StandardCharsets.UTF_8)) {
         payload = scanner.useDelimiter("\\A").next();
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      String sigHeader = request.getHeader("Stripe-Signature");

      String response = paymentService.handleStripeWebhook(payload, sigHeader);
      return new ResponseEntity<>(response, HttpStatus.OK);
   }
}