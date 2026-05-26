package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.Kyc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KycRepository extends JpaRepository<Kyc, Long> {
}