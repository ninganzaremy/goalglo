package com.goalglo.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import com.goalglo.backend.entities.BlogPost;
import com.goalglo.backend.repositories.BlogPostRepository;

@RestController
@RequestMapping("/api/blogs")
public class BlogPostController {

   private final BlogPostRepository blogPostRepository;

   @Autowired
   public BlogPostController(BlogPostRepository blogPostRepository) {
      this.blogPostRepository = blogPostRepository;
   }

   @GetMapping
   public List<BlogPost> getAllBlogPosts() {
      return blogPostRepository.findAll();
   }

   @GetMapping("/{id}")
   public BlogPost getBlogPostById(@PathVariable Long id) {
      return blogPostRepository.findById(id).orElse(null);
   }

   @PostMapping
   public BlogPost createBlogPost(@RequestBody BlogPost blogPost) {
      blogPost.setCreatedAt(LocalDateTime.now());
      return blogPostRepository.save(blogPost);
   }

   @PutMapping("/{id}")
   public BlogPost updateBlogPost(@PathVariable Long id, @RequestBody BlogPost blogPost) {
      BlogPost existingPost = blogPostRepository.findById(id).orElse(null);
      if (existingPost != null) {
         existingPost.setTitle(blogPost.getTitle());
         existingPost.setContent(blogPost.getContent());
         return blogPostRepository.save(existingPost);
      }
      return null;
   }

   @DeleteMapping("/{id}")
   public void deleteBlogPost(@PathVariable Long id) {
      blogPostRepository.deleteById(id);
   }
}