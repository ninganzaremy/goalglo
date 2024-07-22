package com.goalglo.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.goalglo.backend.entities.BlogPost;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
}