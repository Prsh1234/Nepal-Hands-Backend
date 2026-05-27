package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.Kyc;
import com.example.nepalhandsbackend.states.KycStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KycRepository extends JpaRepository<Kyc, Long> {

    Page<Kyc> findByStatus(KycStatus status, Pageable pageable);
}