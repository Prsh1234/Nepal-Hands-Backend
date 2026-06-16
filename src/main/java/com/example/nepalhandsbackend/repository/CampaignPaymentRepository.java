package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.CampaignPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampaignPaymentRepository extends JpaRepository<CampaignPayment, Long> {
    Optional<CampaignPayment> findByTransactionUuid(String transactionUuid);
}
