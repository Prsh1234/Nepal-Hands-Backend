package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.VolunteerApplication;
import com.example.nepalhandsbackend.states.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VolunteerApplicationRepository extends JpaRepository<VolunteerApplication, Long> {
    boolean existsByVolunteerIdAndOpportunityId(
            Integer volunteerId,
            Long opportunityId
    );
    Optional<VolunteerApplication> findByVolunteerIdAndOpportunityId(
            Integer volunteerId,
            Long opportunityId
    );
    List<VolunteerApplication> findByOpportunityId(
            Long opportunityId
    );


    Page<VolunteerApplication> findByOpportunityUserId(
            Integer organizerId,
            Pageable pageable
    );

    Page<VolunteerApplication> findByOpportunityUserIdAndStatus(
            Integer organizerId,
            ApplicationStatus status,
            Pageable pageable
    );

    Page<VolunteerApplication> findByOpportunityIdAndOpportunityUserId(
            Long opportunityId,
            Integer organizerId,
            Pageable pageable
    );

    Page<VolunteerApplication> findByOpportunityIdAndOpportunityUserIdAndStatus(
            Long opportunityId,
            Integer organizerId,
            ApplicationStatus status,
            Pageable pageable
    );
}
