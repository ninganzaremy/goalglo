package com.goalglo.dto;

import com.goalglo.entities.BlogPost;
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
   private String slug;
   private LocalDateTime publishedAt;
   private String author;

   private String imageUrl;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   public BlogPostDTO(BlogPost blogPost) {
      this.id = blogPost.getId();
      this.title = blogPost.getTitle();
      this.content = blogPost.getContent();
      this.authorId = blogPost.getAuthor() != null ? blogPost.getAuthor().getId() : null;
      this.author = blogPost.getAuthor() != null ? blogPost.getAuthor().getFirstName() : null;
      this.publishedAt = blogPost.getPublishedAt();
      this.published = blogPost.isPublished();
      this.slug = blogPost.getSlug();
      this.imageUrl = blogPost.getImageUrl();
      this.createdAt = blogPost.getCreatedAt();
      this.updatedAt = blogPost.getUpdatedAt();
   }

   public BlogPostDTO() {
   }

}