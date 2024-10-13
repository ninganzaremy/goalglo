package com.goalglo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

   @GetMapping("/")
   public String home() {
      return "Backend API is running";
   }
}