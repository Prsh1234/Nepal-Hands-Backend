package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.response.*;
import com.example.nepalhandsbackend.model.Campaign;
import com.example.nepalhandsbackend.model.CampaignUpdate;
import com.example.nepalhandsbackend.model.VolunteerApplication;
import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.repository.CampaignPaymentRepository;
import com.example.nepalhandsbackend.repository.CampaignRepository;
import com.example.nepalhandsbackend.repository.VolunteerApplicationRepository;
import com.example.nepalhandsbackend.repository.VolunteerOpportunityRepository;
import com.example.nepalhandsbackend.states.ApplicationStatus;
import com.example.nepalhandsbackend.states.CampaignStatus;
import com.example.nepalhandsbackend.states.OpportunityStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizerDashboardService {

    private final CampaignRepository campaignRepository;
    private final VolunteerOpportunityRepository volunteerOpportunityRepository;
    private final VolunteerApplicationRepository volunteerApplicationRepository;
    private final CampaignPaymentRepository campaignPaymentRepository;
    public DashboardStatsResponse getDashboardStats(Long organizerId) {

        return DashboardStatsResponse.builder()
                .totalRaised(
                        campaignPaymentRepository.getTotalRaised(organizerId)
                )
                .totalDonors(
                        campaignPaymentRepository.countUniqueDonors(organizerId)
                )
                .totalApplicants(
                        volunteerApplicationRepository.getTotalApplicants(organizerId)
                )
                .build();
    }
    public Page<OrganizerCampaignResponse> getMyCampaigns(
            Integer userId,
            int page,
            int size,
            String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Campaign> campaigns;
        campaigns = campaignRepository.findByUserId(userId, pageable);

        return campaigns.map(c -> OrganizerCampaignResponse.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .category(c.getCategory())
                        .status(mapVolunteerStatus(c.getStatus()))
                        .goal(c.getGoal())
                        .raised(campaignPaymentRepository.getTotalRaisedByCampaignId(c.getId()))
                        .donors(campaignPaymentRepository.countUniqueDonorsByCampaign(c.getId(), (long)c.getUser().getId()))  // replace with donor count
                        .daysLeft(getDaysLeft(c))
                        .createdDate(c.getCreatedAt())
                        .build());
    }


    public List<OrganizerVolunteerResponse> getMyOpportunities(Integer userId) {

        List<VolunteerOpportunity> opportunity = volunteerOpportunityRepository.findByUserId(userId);
        return opportunity.stream()
                .map(o -> OrganizerVolunteerResponse.builder()
                        .id(o.getId())
                        .title(o.getTitle())
                        .category(o.getCategory())
                        .status(mapOpportunityStatus(o.getStatus()))
                        .capacity(o.getVolunteerSpots())
                        .accepted(0)     // TODO replace with real applications
                        .applicants(0)   // TODO replace with real applications
                        .location(o.getLocation())
                        .startDate(o.getStartDate())
                        .build()
                ).toList();
    }

    public List<CampaignSelectResponse> getMyCampaignDropdown(Integer userId) {

        return campaignRepository.findByUserId(userId)
                .stream()
                .map(c -> CampaignSelectResponse.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .build())
                .toList();
    }
    public List<VolunteerOpportunitySelectResponse> getMyVolunteerOpportunityDropdown(Integer userId) {

        return volunteerOpportunityRepository.findByUserId(userId)
                .stream()
                .map(c -> VolunteerOpportunitySelectResponse.builder()
                        .id(c.getId())
                        .title(c.getTitle())
                        .build())
                .toList();
    }

    public Page<VolunteerApplyResponse> getApplicationResponses(
            Integer organizerId,
            Long opportunityId,
            ApplicationStatus status,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "appliedAt")
        );

        Page<VolunteerApplication> applications =
                volunteerApplicationRepository.findApplications(
                        organizerId,
                        opportunityId,
                        status,
                        pageable
                );

        return applications.map(v ->
                VolunteerApplyResponse.builder()
                        .id(v.getId())
                        .volunteerId(v.getVolunteer().getId())
                        .opportunityId(v.getOpportunity().getId())
                        .opportunityTitle(v.getOpportunity().getTitle())
                        .fullName(v.getFullName())
                        .email(v.getEmail())
                        .phone(v.getPhone())
                        .motivation(v.getMotivation())
                        .status(v.getStatus())
                        .appliedAt(v.getAppliedAt())
                        .updatedAt(v.getUpdatedAt())
                        .skills(v.getVolunteer().getSkills())
                        .build()
        );
    }

    public void updateApplicationStatus(
            Long applicationId,
            ApplicationStatus status) {

        VolunteerApplication application =
                volunteerApplicationRepository.findById(applicationId)
                        .orElseThrow(() ->
                                new RuntimeException("Application not found"));

        application.setStatus(status);

        volunteerApplicationRepository.save(application);
    }
    private Long getDaysLeft(Campaign campaign) {
        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            return 0L;
        }

        long days = ChronoUnit.DAYS.between(
                LocalDate.now(),
                campaign.getEndDate()
        );

        return Math.max(days, 0);
    }

    private String mapVolunteerStatus(CampaignStatus status) {
        return switch (status) {
            case ACTIVE -> "active";
            case CLOSED -> "completed";
            case PENDING_REVIEW -> "pending";
            case REJECTED -> "rejected";
        };
    }
    private String mapOpportunityStatus(OpportunityStatus status) {
        return switch (status) {
            case ACTIVE -> "active";
            case CLOSED -> "completed";
            case PENDING_REVIEW -> "pending";
            case REJECTED -> "rejected";
        };
    }

}
