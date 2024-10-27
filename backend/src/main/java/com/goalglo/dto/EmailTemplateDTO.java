package com.goalglo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailTemplateDTO {
   private String templateName;
   private String subject;
   private String body;
   private boolean isHtml;
}