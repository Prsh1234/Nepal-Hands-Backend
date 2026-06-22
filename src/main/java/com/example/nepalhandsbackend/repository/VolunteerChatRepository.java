package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.VolunteerChat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerChatRepository extends JpaRepository<VolunteerChat, Long> {

    Page<VolunteerChat> findByOpportunityIdOrderBySentAtDesc(
            Long opportunityId,
            Pageable pageable
    );
}