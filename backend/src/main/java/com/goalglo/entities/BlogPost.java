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

   @Column(nullable = false, columnDefinition = "TEXT")
   private String content;

   @ManyToOne
   @JoinColumn(name = "author_id")
   private User author;

   @Column(nullable = false)
   private boolean published;

   @CreationTimestamp
   private LocalDateTime createdAt;

   @UpdateTimestamp
   private LocalDateTime updatedAt;

   public BlogPost(BlogPostDTO blogPostDTO) {
      this.title = blogPostDTO.getTitle();
      this.content = blogPostDTO.getContent();
      this.published = blogPostDTO.isPublished();
   }
   public BlogPost() {
   }
}