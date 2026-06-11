package com.example.nepalhandsbackend.service.VolunteersAndDonors;


import com.example.nepalhandsbackend.dto.request.CreateUpdateRequest;
import com.example.nepalhandsbackend.dto.request.VolunteerApplyRequest;
import com.example.nepalhandsbackend.model.*;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.repository.VolunteerApplicationRepository;
import com.example.nepalhandsbackend.repository.VolunteerOpportunityRepository;
import com.example.nepalhandsbackend.states.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class VolunteersService {

    private final UserRepository userRepository;
    private final VolunteerOpportunityRepository volunteerOpportunityRepository;
    private final VolunteerApplicationRepository volunteerApplicationRepository;

    public boolean applicationExists( Integer userId, Long volunteerOpportunityId){
        return  volunteerApplicationRepository
                        .existsByVolunteerIdAndOpportunityId(
                                userId,
                                volunteerOpportunityId
                        );
    }
    public ApplicationStatus getStatus(Long volunteerOpportunityId, Integer userId) {
        return volunteerApplicationRepository
                .findByVolunteerIdAndOpportunityId(userId, volunteerOpportunityId)
                .map(VolunteerApplication::getStatus)
                .orElse(ApplicationStatus.NOT_APPLIED);
    }
    public void apply(Integer userId, VolunteerApplyRequest req, Long volunteerOpportunityId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));

        VolunteerOpportunity opportunity = volunteerOpportunityRepository.findById(volunteerOpportunityId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));


        if (applicationExists(userId,volunteerOpportunityId)) {
            throw new RuntimeException(
                    "You have already applied for this opportunity"
            );
        }
        VolunteerApplication application = VolunteerApplication.builder()
                .volunteer(user)
                .opportunity(opportunity)
                .motivation(req.getMotivation())
                .email(user.getEmail())
                .fullName(user.getFirstName()+ " " +user.getLastName())
                .phone(req.getPhone())
                .build();

        volunteerApplicationRepository.save(application);
    }
}
