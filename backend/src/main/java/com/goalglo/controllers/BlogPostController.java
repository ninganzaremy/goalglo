package com.goalglo.controllers;

import com.goalglo.dto.BlogPostDTO;
import com.goalglo.entities.BlogPost;
import com.goalglo.entities.User;
import com.goalglo.services.BlogPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing blog posts.
 */
@RestController
@RequestMapping("/api/blog-posts")
public class BlogPostController {

   private final BlogPostService blogPostService;

   /**
    * Constructs a new BlogPostController with the specified BlogPostService.
    *
    * @param blogPostService the service for managing blog posts
    */
   @Autowired
   public BlogPostController(BlogPostService blogPostService) {
      this.blogPostService = blogPostService;
   }

   /**
    * Creates a new blog post.
    *
    * @param blogPostDTO the blog post data transfer object
    * @return a ResponseEntity containing the created blog post DTO and HTTP status CREATED
    */
   @PostMapping
   public ResponseEntity<BlogPostDTO> createBlogPost(@RequestBody BlogPostDTO blogPostDTO, @AuthenticationPrincipal User currentUser) {
      BlogPostDTO createdBlogPostDTO = blogPostService.createBlogPost(blogPostDTO, currentUser);
      return new ResponseEntity<>(createdBlogPostDTO, HttpStatus.CREATED);
   }

   /**
    * Retrieves a blog post by its ID.
    *
    * @param id the UUID of the blog post
    * @return a ResponseEntity containing the blog post DTO and HTTP status OK,
    *         or HTTP status NOT FOUND if the blog post does not exist
    */
   @GetMapping("/{id}")
   public ResponseEntity<BlogPostDTO> getBlogPostById(@PathVariable UUID id) {
      return blogPostService.findBlogPostById(id)
         .map(blogPostDTO -> new ResponseEntity<>(blogPostDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }

   /**
    * Retrieves all blog posts.
    *
    * @return a ResponseEntity containing a list of all blog post DTOs and HTTP status OK
    */
   @GetMapping
   public ResponseEntity<List<BlogPostDTO>> getAllBlogPosts() {
      List<BlogPostDTO> blogPosts = blogPostService.findAllBlogPosts();
      return new ResponseEntity<>(blogPosts, HttpStatus.OK);
   }

   /**
    * Updates an existing blog post.
    *
    * @param id the UUID of the blog post to update
    * @param blogPostDTO the updated blog post data transfer object
    * @return a ResponseEntity containing the updated blog post DTO and HTTP status OK,
    *         or HTTP status NOT FOUND if the blog post does not exist
    */
   @PutMapping("/{id}")
   public ResponseEntity<BlogPostDTO> updateBlogPost(@PathVariable UUID id, @RequestBody BlogPostDTO blogPostDTO) {
      BlogPost blogPost = new BlogPost(blogPostDTO);
      return blogPostService.updateBlogPost(id, blogPost)
         .map(updatedBlogPostDTO -> new ResponseEntity<>(updatedBlogPostDTO, HttpStatus.OK))
         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
   }
   /**
    * Deletes a blog post by its ID.
    *
    * @param id the UUID of the blog post to delete
    * @return a ResponseEntity with HTTP status NO_CONTENT if the blog post was deleted,
    *         or HTTP status NOT FOUND if the blog post does not exist
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteBlogPost(@PathVariable UUID id) {
      boolean deleted = blogPostService.deleteBlogPost(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

}