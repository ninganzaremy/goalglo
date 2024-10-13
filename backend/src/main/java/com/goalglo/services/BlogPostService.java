package com.goalglo.services;

import com.goalglo.aws.AwsS3Service;
import com.goalglo.config.SecretConfig;
import com.goalglo.dto.BlogPostDTO;
import com.goalglo.entities.BlogPost;
import com.goalglo.entities.User;
import com.goalglo.repositories.BlogPostRepository;
import com.goalglo.tokens.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing blog posts.
 */
@Service
public class BlogPostService {

   private final BlogPostRepository blogPostRepository;
   private final SecretConfig secretConfig;
   private final AwsS3Service awsS3Service;
   private final JwtUtils jwtUtils;

   /**
    * Constructs a new BlogPostService with the specified BlogPostRepository.
    *
    * @param blogPostRepository the repository for blog posts
    */
   @Autowired
   public BlogPostService(BlogPostRepository blogPostRepository, SecretConfig secretConfig, AwsS3Service awsS3Service,
                          JwtUtils jwtUtils) {
      this.blogPostRepository = blogPostRepository;
      this.secretConfig = secretConfig;
      this.awsS3Service = awsS3Service;
      this.jwtUtils = jwtUtils;

   }


   /**
    * Creates a new blog post.
    *
    * @param blogPostDTO the blog post to create
    * @param authentication the authentication object containing the current user
    * @param image the image file to upload
    * @return the created blog post
    * @throws IOException if the image file cannot be read
    */
   public BlogPostDTO createBlogPost(BlogPostDTO blogPostDTO, Authentication authentication, MultipartFile image) throws IOException {

      BlogPost blogPost = new BlogPost(blogPostDTO);

      blogPost.setAuthor(getCurrentUser(authentication));

      blogPost.setPublished(getCurrentUser(authentication).getRoles().stream()
         .anyMatch(role -> secretConfig.getRoles().getSecuredRole().equals(role.getName())));

      if (image != null && !image.isEmpty()) {
         blogPost.setImageUrl(awsS3Service.uploadFile(image));
      }

      return new BlogPostDTO(blogPostRepository.save(blogPost));
   }


   /**
    * Finds a blog post by its ID.
    *
    * @param id the UUID of the blog post
    * @return an Optional containing the blog post if found, or an empty Optional
    *         if not
    */
   public Optional<BlogPostDTO> findBlogPostById(UUID id) {
      return blogPostRepository.findById(id).map(BlogPostDTO::new); // Directly convert to DTO using constructor
   }

   /**
    * Updates a blog post.
    *
    * @param id the UUID of the blog post to update
    * @param blogPostDTO the updated blog post data
    * @param image the new image file to upload
    * @param authentication the authentication object containing the current user
    * @return the updated blog post
    * @throws IOException if the image file cannot be read
    */
   public BlogPostDTO updateBlogPost(UUID id, BlogPostDTO blogPostDTO, MultipartFile image, Authentication authentication) throws IOException {
      BlogPost existingBlogPost = blogPostRepository.findById(id)
         .orElseThrow(() -> new RuntimeException("Blog post not found"));

      User currentUser = getCurrentUser(authentication);

      // Check if the current user is the author or has admin rights
      if (!existingBlogPost.getAuthor().getId().equals(currentUser.getId()) &&
         currentUser.getRoles().stream().noneMatch(role -> secretConfig.getRoles().getSecuredRole().equals(role.getName()))) {
         throw new RuntimeException("You don't have permission to edit this blog post");
      }

      existingBlogPost.setTitle(blogPostDTO.getTitle());
      existingBlogPost.setContent(blogPostDTO.getContent());
      existingBlogPost.setPublished(blogPostDTO.isPublished());

      if (image != null && !image.isEmpty()) {
         // Delete old image if it exists
         if (existingBlogPost.getImageUrl() != null) {
            awsS3Service.deleteS3Object(existingBlogPost.getImageUrl());
         }
         // Upload new image
         String newImageUrl = awsS3Service.uploadFile(image);
         existingBlogPost.setImageUrl(newImageUrl);
      }

      BlogPost updatedBlogPost = blogPostRepository.save(existingBlogPost);
      return new BlogPostDTO(updatedBlogPost);
   }

   /**
    * Deletes a blog post by its ID.
    *
    * @param id the UUID of the blog post to delete
    * @return true if the blog post was deleted, false if the blog post was not
    *         found
    */
   public boolean deleteBlogPost(UUID id) {
      if (blogPostRepository.existsById(id)) {
         blogPostRepository.deleteById(id);
         return true;
      }
      return false;
   }

   /**
    * Retrieves all blog posts.
    *
    * @return a list of all blog posts
    */
   public List<BlogPostDTO> findAllBlogPosts() {
      return blogPostRepository.findByPublished(true).stream()
         .map(BlogPostDTO::new)
         .collect(Collectors.toList());
   }

   /**
    * Retrieves all published blog posts.
    *
    * @return a list of all published blog posts
    */
   public List<BlogPostDTO> findAllPublishedBlogPosts() {
      return blogPostRepository.findByPublishedTrue().stream()
         .map(BlogPostDTO::new)
         .collect(Collectors.toList());
   }


   /**
    * Retrieves the latest blog posts.
    *
    * @return a list of the latest blog post DTOs
    */
   public List<BlogPostDTO> findLatestBlogPosts() {
      return blogPostRepository.findTop10ByPublishedTrueOrderByCreatedAtDesc().stream()
         .map(BlogPostDTO::new)
         .collect(Collectors.toList());
   }

   /**
    * Retrieves the current user based on the authentication object.
    *
    * @param authentication The authentication object containing the current user's information.
    * @return The User object representing the current user.
    * @throws RuntimeException If the user is not found.
    */
   private User getCurrentUser(Authentication authentication) {
      return jwtUtils.getCurrentUser(authentication)
         .orElseThrow(() -> new RuntimeException("User not found"));
   }
}