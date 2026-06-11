package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VolunteerOpportunityRepository extends JpaRepository<VolunteerOpportunity, Long> {

    Page<VolunteerOpportunity> findByStatus(OpportunityStatus status, Pageable pageable);

    Page<VolunteerOpportunity> findByCategoryAndStatus(
            String category, OpportunityStatus status, Pageable pageable
    );



    @Query("""
        SELECT v FROM VolunteerOpportunity v
        WHERE v.status = :status
          AND (:category IS NULL OR v.category = :category)
          AND (:location IS NULL OR LOWER(v.location) LIKE LOWER(CONCAT('%', :location, '%')))
        """)
    Page<VolunteerOpportunity> search(
            @Param("status")   OpportunityStatus status,
            @Param("category") String category,
            @Param("location") String location,
            Pageable pageable
    );

    @Query("""
        SELECT v FROM VolunteerOpportunity v
        WHERE v.status = :status
      
        """)
    Page<VolunteerOpportunity> opportunityRequest(
            @Param("status")   OpportunityStatus status,
            Pageable pageable
    );

    List<VolunteerOpportunity> findByUserId(Integer userId);
}