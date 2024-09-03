package com.goalglo.backend.services;

import com.goalglo.backend.config.SecretConfig;
import com.goalglo.backend.dto.BlogPostDTO;
import com.goalglo.backend.entities.BlogPost;
import com.goalglo.backend.entities.User;
import com.goalglo.backend.repositories.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


   /**
    * Constructs a new BlogPostService with the specified BlogPostRepository.
    *
    * @param blogPostRepository the repository for blog posts
    */
   @Autowired
   public BlogPostService(BlogPostRepository blogPostRepository, SecretConfig secretConfig) {
      this.blogPostRepository = blogPostRepository;
      this.secretConfig = secretConfig;
   }


   /**
    * Creates a new blog post.
    *
    * @param blogPostDTO the blog post to create
    * @param currentUser the current user creating the blog post
    * @return the created blog post
    */
   public BlogPostDTO createBlogPost(BlogPostDTO blogPostDTO, User currentUser) {

      blogPostDTO.setAuthorId(currentUser.getId());

      boolean isAuthorized = currentUser.getRoles().stream().anyMatch(role -> secretConfig.getSecuredRole().equals(role.getName()));

      blogPostDTO.setPublished(isAuthorized);

      BlogPost blogPost = new BlogPost(blogPostDTO);
      BlogPost createdBlogPost = blogPostRepository.save(blogPost);
      return new BlogPostDTO(createdBlogPost);
   }


   /**
    * Finds a blog post by its ID.
    *
    * @param id the UUID of the blog post
    * @return an Optional containing the blog post if found, or an empty Optional if not
    */
   public Optional<BlogPostDTO> findBlogPostById(UUID id) {
      return blogPostRepository.findById(id).map(BlogPostDTO::new);  // Directly convert to DTO using constructor
   }


   /**
    * Updates an existing blog post.
    *
    * @param id          the UUID of the blog post to update
    * @param blogPost the updated blog post
    * @return the updated blog post, or an empty Optional if the blog post was not found
    */
   public Optional<BlogPostDTO> updateBlogPost(UUID id, BlogPost blogPost) {
      return blogPostRepository.findById(id).map(existingBlogPost -> {
         existingBlogPost.setTitle(blogPost.getTitle());
         existingBlogPost.setContent(blogPost.getContent());
         existingBlogPost.setPublished(blogPost.isPublished());
         BlogPost updatedBlogPost = blogPostRepository.save(existingBlogPost);
         return new BlogPostDTO(updatedBlogPost);
      });
   }

   /**
    * Deletes a blog post by its ID.
    *
    * @param id the UUID of the blog post to delete
    * @return true if the blog post was deleted, false if the blog post was not found
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

}