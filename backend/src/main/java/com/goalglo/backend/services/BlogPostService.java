package com.goalglo.backend.services;

import com.goalglo.backend.dto.BlogPostDTO;
import com.goalglo.backend.entities.BlogPost;
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

   /**
    * Constructs a new BlogPostService with the specified BlogPostRepository.
    *
    * @param blogPostRepository the repository for blog posts
    */
   @Autowired
   public BlogPostService(BlogPostRepository blogPostRepository) {
      this.blogPostRepository = blogPostRepository;
   }

   /**
    * Creates a new blog post.
    *
    * @param blogPost the blog post to be created
    * @return the created blog post
    */
   public BlogPostDTO createBlogPost(BlogPost blogPost) {
      BlogPost createdBlogPost = blogPostRepository.save(blogPost);
      return new BlogPostDTO(createdBlogPost);  // Using the constructor to create DTO
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
      return blogPostRepository.findAll().stream()
         .map(BlogPostDTO::new)  // Convert each BlogPost to BlogPostDTO using the constructor
         .collect(Collectors.toList());
   }
}