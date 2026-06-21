package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.CampaignRequest;
import com.example.nepalhandsbackend.dto.response.*;
import com.example.nepalhandsbackend.model.*;
import com.example.nepalhandsbackend.repository.CampaignPaymentRepository;
import com.example.nepalhandsbackend.repository.CampaignRepository;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.states.CampaignCategory;
import com.example.nepalhandsbackend.states.CampaignStatus;
import com.example.nepalhandsbackend.states.PaymentStatus;
import com.example.nepalhandsbackend.utils.FileTextUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {
    @Autowired
    private FileTextUtils fileTextUtils;
    private final CampaignRepository campaignRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampaignPaymentRepository campaignPaymentRepository;

    @Autowired
    private DonationService donationService;
    @Transactional
    public Campaign createCampaign(CampaignRequest request,Integer userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow();

        Campaign campaign = Campaign.builder()
                .title(request.getTitle())
                .category(request.getCategory())
                .location(request.getLocation())
                .description(request.getDescription())
                .longDescription(request.getLongDescription())
                .projectScope(request.getProjectScope())
                .goal(request.getGoal())
                .duration(request.getDuration())
                .organizer(request.getOrganizer())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .contactName(request.getContactName())
                .contactEmail(request.getContactEmail())
                .contactPhone(request.getContactPhone())
                .coverImage(fileTextUtils.toBytes(request.getCoverImage()))
                .images(fileTextUtils.toBytesList(request.getImages()))
                .user(user)
                .build();
        CampaignVerification verification = CampaignVerification.builder()
                .campaign(campaign)
                .orgLegalName(request.getOrgLegalName())
                .orgType(request.getOrgType())
                .regNumber(request.getRegNumber())
                .regAuthority(request.getRegAuthority())
                .regDate(request.getRegDate())
                .panNumber(request.getPanNumber())
                .website(request.getWebsite())
                .authorizedSignatory(request.getAuthorizedSignatory())
                .signatoryRole(request.getSignatoryRole())
                .bankName(request.getBankName())
                .bankAccountHolderName(request.getBankAccountHolderName())
                .bankAccountNumber(request.getBankAccountNumber())
                .build();
        verification.setCampaign(campaign);
        campaign.setVerification(verification);

        List<CampaignVerificationDocument> docs = new ArrayList<>();

        List<MultipartFile> files = request.getDocuments();
        List<String> types = request.getDocumentTypes();

        if (files != null && !files.isEmpty()) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);

                CampaignVerificationDocument doc = CampaignVerificationDocument.builder()
                        .file(file.getBytes())
                        .fileName(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .documentType(types != null && i < types.size()
                                ? types.get(i)
                                : "UNKNOWN")
                        .build();
                doc.setVerification(verification);
                docs.add(doc);
            }
        }
        verification.setDocuments(docs);


        return campaignRepository.save(campaign);
    }
    @Transactional(readOnly = true)
    public CampaignResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }



    private Campaign findOrThrow(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Campaign not found: " + id));
    }


    @Transactional(readOnly = true)
    public PageResponse<CampaignResponse> campaignRequests(
            Pageable pageable) {

        Page<CampaignResponse> page =
                campaignRepository.campaignRequest(CampaignStatus.PENDING_REVIEW, pageable)
                        .map(this::toResponse);

        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
    public CampaignResponse updateStatus(Long id, CampaignStatus status) {
        Campaign entity = campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        entity.setStatus(status);
        campaignRepository.save(entity);

        return toResponse(entity);
    }
    public Page<CampaignCardDTO> getCampaigns(
            String search,
            CampaignCategory category,
            String sort,
            int page,
            int size
    ) {

        Sort sorting;

        switch (sort) {
            case "newest":
                sorting = Sort.by("createdAt").descending();
                break;

            case "ending-soon":
                sorting = Sort.by("endDate").ascending();
                break;

            default:
                sorting = Sort.by("createdAt").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sorting);

        return campaignRepository
                .findCampaigns(search, category, pageable)
                .map(c -> CampaignCardDTO.builder()
                        .description(c.getDescription())
                        .id(c.getId())
                        .organizer(c.getOrganizer())
                        .category(c.getCategory())
                        .title(c.getTitle())
                        .donors(campaignPaymentRepository.countByCampaignIdAndStatus(c.getId(),PaymentStatus.SUCCESS))
                        .raised(campaignPaymentRepository.getTotalRaisedByCampaignId(c.getId()))
                        .goal(c.getGoal())
                        .daysLeft(getDaysLeft(c))
                        .progress(getProgress(c))
                        .postedAt(c.getCreatedAt())
                        .build());
    }

    private CampaignVerificationResponse mapVerification(
            CampaignVerification c
    ) {
        if (c == null) return null;

        return CampaignVerificationResponse.builder()
                .orgLegalName(c.getOrgLegalName())
                .orgType(c.getOrgType())
                .regNumber(c.getRegNumber())
                .regAuthority(c.getRegAuthority())
                .regDate(c.getRegDate())
                .panNumber(c.getPanNumber())
                .website(c.getWebsite())
                .bankName(c.getBankName())
                .bankAccountHolderName(c.getBankAccountHolderName())
                .bankAccountNumber(c.getBankAccountNumber())
                .authorizedSignatory(c.getAuthorizedSignatory())
                .signatoryRole(c.getSignatoryRole())

                // ✅ FIX: map documents
                .documents(
                        c.getDocuments() == null ? List.of() :
                                c.getDocuments().stream().map(d ->
                                        CampaignVerificationDocumentResponse.builder()
                                                .id(d.getId())
                                                .documentType(d.getDocumentType())
                                                .fileName(d.getFileName())
                                                .build()
                                ).toList()
                )
                .build();
    }
    private CampaignResponse toResponse(Campaign e) {
        return CampaignResponse.builder()
                .id(e.getId())
                .title(e.getTitle())
                .category(e.getCategory())
                .location(e.getLocation())
                .description(e.getDescription())
                .longDescription(e.getLongDescription())
                .projectScope(fileTextUtils.splitLines(e.getProjectScope()))
                .goal(e.getGoal())
                .totalDonors(campaignPaymentRepository.countByCampaignIdAndStatus(e.getId(),PaymentStatus.SUCCESS))
                .raised(campaignPaymentRepository.getTotalRaisedByCampaignId(e.getId()))
                .duration(e.getDuration())
                .organizer(e.getOrganizer())
                .startDate(e.getStartDate())
                .endDate(e.getEndDate())
                .contactName(e.getContactName())
                .contactEmail(e.getContactEmail())
                .contactPhone(e.getContactPhone())
                .coverImage(e.getCoverImage())
                .images(e.getImages())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .verification(mapVerification(e.getVerification()))
                .postedById(
                        e.getUser() != null ? e.getUser().getId() : null
                )
                .postedByName(
                        e.getUser() != null
                                ? e.getUser().getFirstName() + " " + e.getUser().getLastName()
                                : null
                )
                .updates(
                        e.getUpdates() == null ? List.of() :
                                e.getUpdates().stream().map(u ->
                                        CampaignUpdateResponse.builder()
                                                .id(u.getId())
                                                .title(u.getTitle())
                                                .body(u.getBody())
                                                .date(u.getCreatedAt())
                                                .build()
                                ).toList()
                )
                .recentDonors(
                        donationService.getRecentDonations(e.getId())
                )
                .build();
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
    private double getProgress(Campaign campaign) {
        if (campaign.getStatus() != CampaignStatus.ACTIVE) {
            return 0L;
        }
        if(campaign.getGoal()>0){
            double progress = Math.min(
                    ((campaignPaymentRepository.getTotalRaisedByCampaignId(campaign.getId()))/campaign.getGoal())*100,100);
            return progress;
        }else {
            return 0;
        }
    }
}

