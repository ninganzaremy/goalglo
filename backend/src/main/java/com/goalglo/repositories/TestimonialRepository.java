package com.goalglo.repositories;

import com.goalglo.entities.Testimonial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TestimonialRepository extends JpaRepository<Testimonial, UUID> {
   List<Testimonial> findByStatus(Testimonial.TestimonialStatus status);

}