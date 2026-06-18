package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.CampaignTransparencyExpenses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignTransparencyExpensesRepository extends JpaRepository<CampaignTransparencyExpenses,Long> {
    Page<CampaignTransparencyExpenses> findByCampaign_User_Id(
            Integer userId,
            Pageable pageable
    );

    Page<CampaignTransparencyExpenses> findByCampaign_IdAndCampaign_User_Id(
            Long campaignId,
            Integer userId,
            Pageable pageable
    );

    List<CampaignTransparencyExpenses> findByCampaign_Id(
            Long campaignId
    );
}
