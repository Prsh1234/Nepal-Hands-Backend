package com.example.nepalhandsbackend.repository;

import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.states.CampaignCategory;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import com.example.nepalhandsbackend.states.VolunteerCategory;
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

    @Query("""
        SELECT c
        FROM VolunteerOpportunity c
        WHERE c.status = com.example.nepalhandsbackend.states.OpportunityStatus.ACTIVE
        AND (
            :search IS NULL OR
            LOWER(c.title) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')) OR
            LOWER(c.organizer) LIKE LOWER(CONCAT('%', :search, '%')) OR 
            LOWER(c.longDescription) LIKE LOWER(CONCAT('%', :search, '%')) 
        )
        AND (
            :category IS NULL OR
            c.category = :category
        )
    """)
    Page<VolunteerOpportunity> findOpportunities(
            @Param("search") String search,
            @Param("category") VolunteerCategory category,
            Pageable pageable
    );
    @Query("""
    SELECT v
    FROM VolunteerOpportunity v
    LEFT JOIN VolunteerApplication a
        ON a.opportunity.id = v.id
        AND a.status = com.example.nepalhandsbackend.states.ApplicationStatus.APPROVED
    WHERE (:search IS NULL OR LOWER(v.title) LIKE LOWER(CONCAT('%', :search, '%')))
      AND (:category IS NULL OR v.category = :category)
    GROUP BY v
    ORDER BY (v.volunteerSpots - COUNT(a)) DESC
""")
    Page<VolunteerOpportunity> findOpportunitiesBySpotsLeft(
            @Param("search") String search,
            @Param("category") VolunteerCategory category,
            Pageable pageable
    );
    @Query("""
SELECT COUNT(a)
FROM VolunteerApplication a
WHERE a.opportunity.id = :opportunityId
AND a.status = 'APPROVED'
""")
    long countApproved(Long opportunityId);
}