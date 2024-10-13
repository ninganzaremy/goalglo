package com.goalglo.entities;

import com.goalglo.dto.BlogPostDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a blog post.
 */
@Getter
@Setter
@Entity
@Table(name = "blogs")
public class BlogPost {

   @Id
   @GeneratedValue
   private UUID id;

   @Column(nullable = false)
   private String title;

   @Column(nullable = false, unique = true)
   private String slug;

   @Column(nullable = false, columnDefinition = "TEXT")
   private String content;

   @Column(name = "image_url")
   private String imageUrl;

   @ManyToOne
   @JoinColumn(name = "author_id")
   private User author;

   @Column(nullable = false)
   private boolean published;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @Column(name = "published_at")
   private LocalDateTime publishedAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;

   public BlogPost(BlogPostDTO blogPostDTO) {
      this.title = blogPostDTO.getTitle();
      this.slug = blogPostDTO.getSlug();
      this.content = blogPostDTO.getContent();
      this.imageUrl = blogPostDTO.getImageUrl();
      this.published = blogPostDTO.isPublished();
   }

   public void setPublished(boolean published) {
      this.published = published;
      if (published && this.publishedAt == null) {
         this.publishedAt = LocalDateTime.now();
      }
   }
   public BlogPost() {
   }
}