package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.CampaignPayment;
import com.example.nepalhandsbackend.states.PaymentStatus;
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
}
