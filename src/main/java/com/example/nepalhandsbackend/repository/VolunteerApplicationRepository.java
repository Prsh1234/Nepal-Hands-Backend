package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.VolunteerApplication;
import com.example.nepalhandsbackend.states.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    @Query("""
    SELECT v FROM VolunteerApplication v
    WHERE v.opportunity.user.id = :organizerId
    AND (:opportunityId IS NULL OR v.opportunity.id = :opportunityId)
    AND (:status IS NULL OR v.status = :status)
""")
    Page<VolunteerApplication> findApplications(
            @Param("organizerId") Integer organizerId,
            @Param("opportunityId") Long opportunityId,
            @Param("status") ApplicationStatus status,
            Pageable pageable
    );
    @Query("""
    SELECT COUNT(v)
    FROM VolunteerApplication v
    WHERE v.opportunity.user.id = :organizerId
""")
    Long getTotalApplicants(@Param("organizerId") Long organizerId);
}
