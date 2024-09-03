package com.goalglo.backend.repositories;

import com.goalglo.backend.entities.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, UUID> {
   List<TimeSlot> findByBookedFalseAndEndTimeAfterOrderByStartTimeAsc(LocalDateTime currentDateTime);

}