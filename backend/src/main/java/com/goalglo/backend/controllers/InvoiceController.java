package com.goalglo.backend.controllers;

import com.goalglo.backend.dto.InvoiceDTO;
import com.goalglo.backend.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

   private final InvoiceService invoiceService;

   @Autowired
   public InvoiceController(InvoiceService invoiceService) {
      this.invoiceService = invoiceService;
   }

   @PostMapping("/create")
   public ResponseEntity<InvoiceDTO> createInvoice(@RequestParam UUID paymentId, @RequestParam UUID userId) {
      try {
         InvoiceDTO invoiceDTO = invoiceService.createAndSendInvoice(paymentId, userId);
         return new ResponseEntity<>(invoiceDTO, HttpStatus.OK);
      } catch (Exception e) {
         return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
      }
   }
}