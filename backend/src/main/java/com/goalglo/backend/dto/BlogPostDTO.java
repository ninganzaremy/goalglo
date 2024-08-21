package com.goalglo.backend.dto;

import com.goalglo.backend.entities.BlogPost;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) for BlogPost.
 */
@Getter
@Setter
public class BlogPostDTO {

   private UUID id;
   private String title;
   private String content;
   private UUID authorId;
   private boolean published;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   public BlogPostDTO(BlogPost blogPost) {
      this.id = blogPost.getId();
      this.title = blogPost.getTitle();
      this.content = blogPost.getContent();
      this.authorId = blogPost.getAuthor().getId();
      this.published = blogPost.isPublished();
      this.createdAt = blogPost.getCreatedAt();
      this.updatedAt = blogPost.getUpdatedAt();
   }
   public BlogPostDTO() {}
}