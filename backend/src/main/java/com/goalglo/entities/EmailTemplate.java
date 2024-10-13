package com.goalglo.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "email_templates")
public class EmailTemplate {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private UUID id;

   @Column(nullable = false, unique = true)
   private String templateName;

   @Column(nullable = false)
   private String subject;

   @Column(nullable = false, columnDefinition = "TEXT")
   private String body;

   @Column(nullable = false)
   private boolean isHtml;

}