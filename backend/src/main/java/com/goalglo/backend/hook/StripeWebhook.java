package com.goalglo.backend.hook;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import com.stripe.param.PaymentIntentCancelParams;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Component;

@Component
public class StripeWebhook {

   /**
    * Captures the Stripe event from the webhook payload.
    *
    * @param payload              The JSON payload received from Stripe.
    * @param sigHeader            The Stripe signature header used to verify the authenticity of the event.
    * @param stripeEndpointSecret The endpoint secret to verify the signature.
    * @return Event             The Stripe Event object parsed from the payload.
    * @throws SignatureVerificationException If the signature verification fails.
    */
   public Event captureStripeEvent(String payload, String sigHeader, String stripeEndpointSecret) throws SignatureVerificationException {

      return Webhook.constructEvent(payload, sigHeader, stripeEndpointSecret);
   }
   /**
    * Creates a payment intent with Stripe for a specified amount and currency.
    *
    * @param amount   The amount to be charged, in the smallest currency unit (e.g., cents for USD).
    * @param currency The three-letter ISO currency code (e.g., "USD").
    * @return String  The unique identifier for the created Stripe payment intent.
    * @throws StripeException If an error occurs while communicating with Stripe.
    */
   public String createPaymentIntent(Long amount, String currency) throws StripeException {
      PaymentIntentCreateParams params =
         PaymentIntentCreateParams.builder()
            .setAmount(amount)
            .setCurrency(currency)
            .setAutomaticPaymentMethods(
               PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                  .setEnabled(true)
                  .build()
            )
            .build();
      PaymentIntent paymentIntent = PaymentIntent.create(params);
      return paymentIntent.getId();
   }

   /**
    * Confirms an existing payment intent with Stripe.
    *
    * @param paymentIntentId The unique identifier of the payment intent to confirm.
    * @throws StripeException If an error occurs while confirming the payment intent with Stripe.
    */
   public void confirmPaymentIntent(String paymentIntentId, String paymentMethodId, String returnUrl) throws StripeException {
      PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
      PaymentIntentConfirmParams confirmParams =
         PaymentIntentConfirmParams.builder()
            .setPaymentMethod(paymentMethodId)
            .setReturnUrl(returnUrl)
            .build();
      paymentIntent.confirm(confirmParams);
   }


   /**
    * Cancels an existing payment intent with Stripe.
    *
    * @param paymentIntentId The unique identifier of the payment intent to cancel.
    * @throws StripeException If an error occurs while canceling the payment intent with Stripe.
    */
   public void cancelPaymentIntent(String paymentIntentId) throws StripeException {
      PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
      PaymentIntentCancelParams cancelParams = PaymentIntentCancelParams.builder().build();
      paymentIntent.cancel(cancelParams);
   }
}