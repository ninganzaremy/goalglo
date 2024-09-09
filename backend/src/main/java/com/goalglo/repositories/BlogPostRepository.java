package com.goalglo.repositories;

import com.goalglo.entities.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for BlogPost entities.
 */
@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, UUID> {
   /**
    * Finds all blog posts by a specific author.
    *
    * @param published The published status of the blog post.
    * @return A list of blog posts with the specified published status.
    */
   List<BlogPost> findByPublished(boolean published);

   /**
    * Finds the top 3 blog posts by creation date in descending order.
    *
    * @return A list of the top 3 blog posts by creation date in descending order.
    */
   List<BlogPost> findTop10ByPublishedTrueOrderByCreatedAtDesc();


   /**
    * Finds all blog posts by a specific author.
    *
    * @param authorId The UUID of the author.
    * @return A list of blog posts written by the specified author.
    */
   List<BlogPost> findByAuthorId(UUID authorId);

   /**
    * Finds all blog posts with a specific title.
    *
    * @param title The title of the blog post.
    * @return A list of blog posts that have the specified title.
    */
   List<BlogPost> findByTitle(String title);

   /**
    * Finds all blog posts that are published.
    *
    * @return A list of blog posts that have been published.
    */
   List<BlogPost> findByPublishedTrue();

   /**
    * Finds all blog posts that are not yet published.
    *
    * @return A list of blog posts that have not been published.
    */
   List<BlogPost> findByPublishedFalse();

   /**
    * Deletes all blog posts by a specific author.
    *
    * @param authorId The UUID of the author.
    * @return The number of blog posts deleted.
    */
   int deleteByAuthorId(UUID authorId);

}