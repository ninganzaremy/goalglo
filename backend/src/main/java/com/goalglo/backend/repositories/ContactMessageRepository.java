package com.goalglo.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.goalglo.backend.entities.ContactMessage;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
}