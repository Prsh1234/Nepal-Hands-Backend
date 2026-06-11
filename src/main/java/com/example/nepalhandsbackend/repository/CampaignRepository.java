package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.states.CampaignStatus;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    @Query("""
        SELECT c FROM Campaign c
        WHERE c.status = :status
      
        """)
    Page<Campaign> campaignRequest(
            @Param("status") CampaignStatus status,
            Pageable pageable
    );

    List<Campaign> findByUserId(Integer userId);

}