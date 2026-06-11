package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.CampaignUpdate;
import com.example.nepalhandsbackend.model.VolunteerOpportunityUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignUpdateRepository extends JpaRepository<CampaignUpdate, Long> {

    Page<CampaignUpdate> findByCampaign_User_Id(
            Integer userId,
            Pageable pageable
    );

    Page<CampaignUpdate> findByCampaign_IdAndCampaign_User_Id(
            Long campaignId,
            Integer userId,
            Pageable pageable
    );
}