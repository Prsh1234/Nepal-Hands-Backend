package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.CampaignPayment;
import com.example.nepalhandsbackend.states.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignPaymentRepository extends JpaRepository<CampaignPayment, Long> {
    Optional<CampaignPayment> findByTransactionUuid(String transactionUuid);
    List<CampaignPayment> findTop5ByCampaignIdAndStatusOrderByCreatedAtDesc(
            Long campaignId,
            PaymentStatus status
    );
    List<CampaignPayment> findTop5ByCampaign_User_IdAndStatusOrderByCreatedAtDesc(
            Long userId,
            PaymentStatus status
    );
    Page<CampaignPayment> findByCampaign_IdAndCampaign_User_IdAndStatus(
            Long campaignId,
            Long userId,
            PaymentStatus status,
            Pageable pageable
    );

    Page<CampaignPayment> findByCampaign_User_IdAndStatus(
            Long campaignId,
            PaymentStatus status,
            Pageable pageable
    );
    List<CampaignPayment> findByCampaign_IdAndCampaign_User_IdAndStatusOrderByCreatedAtDesc(
            Long campaignId,
            Long userId,
            PaymentStatus status
    );

    List<CampaignPayment> findByCampaign_User_IdAndStatusOrderByCreatedAtDesc(
            Long campaignId,
            PaymentStatus status
    );
    long countByCampaignIdAndStatus(
            Long campaignId,
            PaymentStatus status
    );
    @Query("""
        SELECT COALESCE(SUM(cp.amount), 0)
        FROM CampaignPayment cp
        WHERE cp.campaign.id = :campaignId
        AND cp.status = 'SUCCESS'
    """)
    Double getTotalRaisedByCampaignId(@Param("campaignId") Long campaignId);


    @Query("""
    SELECT COALESCE(SUM(cp.amount), 0)
    FROM CampaignPayment cp
    WHERE cp.campaign.user.id = :userId
    AND cp.status = 'SUCCESS'
""")
    Double getTotalRaised(@Param("userId") Long userId);

    @Query("""
    SELECT COUNT(DISTINCT cp.user.id)
    FROM CampaignPayment cp
    WHERE cp.campaign.user.id = :userId
    AND cp.status = 'SUCCESS'
""")
    Long countUniqueDonors(@Param("userId") Long userId);

    @Query("""
    SELECT COALESCE(AVG(cp.amount),0)
    FROM CampaignPayment cp
    WHERE cp.campaign.user.id = :userId
    AND cp.status = 'SUCCESS'
""")
    Double getAverageDonation(@Param("userId") Long userId);


    @Query("""
    SELECT COALESCE(SUM(cp.amount), 0)
    FROM CampaignPayment cp
    WHERE cp.campaign.id = :campaignId
    AND cp.campaign.user.id = :userId
    AND cp.status = 'SUCCESS'
""")
    Double getTotalRaisedByCampaign(
            @Param("campaignId") Long campaignId,
            @Param("userId") Long userId
    );

    @Query("""
    SELECT COUNT(DISTINCT cp.user.id)
    FROM CampaignPayment cp
    WHERE cp.campaign.id = :campaignId
    AND cp.campaign.user.id = :userId
    AND cp.status = 'SUCCESS'
""")
    Long countUniqueDonorsByCampaign(
            @Param("campaignId") Long campaignId,
            @Param("userId") Long userId
    );

    @Query("""
    SELECT COALESCE(AVG(cp.amount),0)
    FROM CampaignPayment cp
    WHERE cp.campaign.id = :campaignId
    AND cp.campaign.user.id = :userId
    AND cp.status = 'SUCCESS'
""")
    Double getAverageDonationByCampaign(
            @Param("campaignId") Long campaignId,
            @Param("userId") Long userId
    );
}
