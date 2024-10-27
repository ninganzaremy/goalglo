package com.goalglo.controllers;

import com.goalglo.dto.BlogPostDTO;
import com.goalglo.services.BlogPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * REST controller for managing blog posts.
 */
@RestController
@RequestMapping("/api/blog-posts")
public class BlogPostController {

   private final BlogPostService blogPostService;
   Logger log = LoggerFactory.getLogger(BlogPostController.class);

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
    * @return a ResponseEntity containing the created blog post DTO and HTTP status
    *         CREATED
    */
   @PostMapping
   public ResponseEntity<BlogPostDTO> createBlogPost(
      BlogPostDTO blogPostDTO,
      Authentication authentication,
      @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

      BlogPostDTO createdBlogPostDTO = blogPostService.createBlogPost(blogPostDTO, authentication, image);
      return new ResponseEntity<>(createdBlogPostDTO, HttpStatus.CREATED);
   }

   /**
    * Retrieves all published blog posts.
    *
    * @return a ResponseEntity containing a list of all published blog post DTOs
    * and HTTP status OK
    */
   @GetMapping("/published")
   public ResponseEntity<List<BlogPostDTO>> getAllPublishedBlogPosts() {
      List<BlogPostDTO> blogPosts = blogPostService.findAllPublishedBlogPosts();
      return ResponseEntity.ok(blogPosts);
   }

   /**
    * Retrieves the latest blog posts.
    *
    * @return a ResponseEntity containing a list of the latest blog post DTOs
    * and HTTP status OK
    */
   @GetMapping("/latest")
   public ResponseEntity<List<BlogPostDTO>> getLatestBlogPosts() {
      List<BlogPostDTO> latestPosts = blogPostService.findLatestBlogPosts();
      return ResponseEntity.ok(latestPosts);
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
    * @return a ResponseEntity containing a list of all blog post DTOs and HTTP
    *         status OK
    */
   @GetMapping
   public ResponseEntity<List<BlogPostDTO>> getAllBlogPosts() {
      List<BlogPostDTO> blogPosts = blogPostService.findAllBlogPosts();
      return new ResponseEntity<>(blogPosts, HttpStatus.OK);
   }


   /**
    * Updates a blog post.
    *
    * @param id the UUID of the blog post to update
    * @param title the new title of the blog post
    * @param content the new content of the blog post
    * @param published the new published status of the blog post
    * @param image the new image of the blog post
    * @return a ResponseEntity containing the updated blog post DTO and HTTP status
    *         OK
    */
   @PutMapping("/{id}")
   public ResponseEntity<BlogPostDTO> updateBlogPost(
      @PathVariable UUID id,
      @ModelAttribute BlogPostDTO blogPostDTO,
      @RequestPart(value = "image", required = false) MultipartFile image,
      Authentication authentication) throws IOException {

      BlogPostDTO updatedBlogPost = blogPostService.updateBlogPost(id, blogPostDTO, image, authentication);
      return ResponseEntity.ok(updatedBlogPost);
   }
   /**
    * Deletes a blog post by its ID.
    *
    * @param id the UUID of the blog post to delete
    * @return a ResponseEntity with HTTP status NO_CONTENT if the blog post was
    *         deleted,
    *         or HTTP status NOT FOUND if the blog post does not exist
    */
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteBlogPost(@PathVariable UUID id) {
      boolean deleted = blogPostService.deleteBlogPost(id);
      return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

}