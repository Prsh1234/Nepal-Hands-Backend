package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.CampaignTransparencyExpenses;
import com.example.nepalhandsbackend.model.CampaignTransparencyImpact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignTransparencyImpactRepository extends JpaRepository<CampaignTransparencyImpact,Long> {
    Page<CampaignTransparencyImpact> findByCampaign_User_Id(
            Integer userId,
            Pageable pageable
    );

    Page<CampaignTransparencyImpact> findByCampaign_IdAndCampaign_User_Id(
            Long campaignId,
            Integer userId,
            Pageable pageable
    );

    List<CampaignTransparencyImpact> findByCampaign_Id(
            Long campaignId
    );
}
