package com.goalglo.services;

import com.goalglo.aws.AwsS3Service;
import com.goalglo.dto.BlogPostDTO;
import com.goalglo.entities.BlogPost;
import com.goalglo.entities.User;
import com.goalglo.repositories.BlogPostRepository;
import com.goalglo.repositories.UserRepository;
import com.goalglo.util.SecretConfig;
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
   private final UserRepository userRepository;

   /**
    * Constructs a new BlogPostService with the specified BlogPostRepository.
    *
    * @param blogPostRepository the repository for blog posts
    */
   @Autowired
   public BlogPostService(BlogPostRepository blogPostRepository, SecretConfig secretConfig, AwsS3Service awsS3Service,
                          UserRepository userRepository) {
      this.blogPostRepository = blogPostRepository;
      this.secretConfig = secretConfig;
      this.awsS3Service = awsS3Service;
      this.userRepository = userRepository;

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
      User currentUser = userRepository.findByUsername(authentication.getName())
         .orElseThrow(() -> new RuntimeException("User not found"));

      BlogPost blogPost = new BlogPost(blogPostDTO);
      blogPost.setAuthor(currentUser);
      blogPost.setPublished(currentUser.getRoles().stream()
         .anyMatch(role -> secretConfig.getSecuredRole().equals(role.getName())));

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
    * Updates an existing blog post.
    *
    * @param id       the UUID of the blog post to update
    * @param blogPost the updated blog post
    * @return the updated blog post, or an empty Optional if the blog post was not
    *         found
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

}