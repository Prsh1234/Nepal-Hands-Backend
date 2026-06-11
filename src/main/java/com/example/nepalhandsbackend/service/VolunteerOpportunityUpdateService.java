package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.CreateUpdateRequest;
import com.example.nepalhandsbackend.dto.response.VolunteerOpportunityUpdateResponse;
import com.example.nepalhandsbackend.model.VolunteerOpportunity;
import com.example.nepalhandsbackend.model.VolunteerOpportunityUpdate;
import com.example.nepalhandsbackend.repository.VolunteerOpportunityRepository;
import com.example.nepalhandsbackend.repository.VolunteerOpportunityUpdateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VolunteerOpportunityUpdateService {

    private final VolunteerOpportunityUpdateRepository volunteerOpportunityUpdateRepository;
    private final VolunteerOpportunityRepository volunteerOpportunityRepository;

    public void createVolunteerUpdate(Integer userId, CreateUpdateRequest req) {

        VolunteerOpportunity volunteerOpportunity = volunteerOpportunityRepository.findById(req.getId())
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        // 🔒 Security check: only owner can post updates
        if (volunteerOpportunity.getUser().getId() != userId) {
            throw new RuntimeException("You are not allowed to post updates for this Volunteer Opportunity");
        }

        VolunteerOpportunityUpdate update = VolunteerOpportunityUpdate.builder()
                .opportunity(volunteerOpportunity)
                .title(req.getTitle())
                .body(req.getBody())
                .build();

        volunteerOpportunityUpdateRepository.save(update);
    }
    public Page<VolunteerOpportunityUpdateResponse> getUpdates(
            Integer userId,
            Long opportunityId,
            int page,
            int size,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VolunteerOpportunityUpdate> updates;

        if (opportunityId != null) {
            updates = volunteerOpportunityUpdateRepository
                    .findByOpportunity_IdAndOpportunity_User_Id(
                            opportunityId,
                            userId,
                            pageable
                    );
        } else {
            updates = volunteerOpportunityUpdateRepository
                    .findByOpportunity_User_Id(
                            userId,
                            pageable
                    );
        }

        return updates.map(u ->
                VolunteerOpportunityUpdateResponse.builder()
                        .id(u.getId())
                        .opportunityId(u.getOpportunity().getId())
                        .opportunityTitle(u.getOpportunity().getTitle())
                        .title(u.getTitle())
                        .body(u.getBody())
                        .date(u.getCreatedAt())
                        .build()
        );
    }
}
