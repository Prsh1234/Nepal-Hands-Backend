package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.CampaignUpdate;
import com.example.nepalhandsbackend.model.VolunteerOpportunityUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerOpportunityUpdateRepository extends JpaRepository<VolunteerOpportunityUpdate, Long> {


    Page<VolunteerOpportunityUpdate> findByOpportunity_User_Id(
            Integer userId,
            Pageable pageable
    );

    Page<VolunteerOpportunityUpdate> findByOpportunity_IdAndOpportunity_User_Id(
            Long opportunityId,
            Integer userId,
            Pageable pageable
    );
}
