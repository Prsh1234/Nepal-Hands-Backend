package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.response.DonationDashboardResponse;
import com.example.nepalhandsbackend.dto.response.DonorListResponse;
import com.example.nepalhandsbackend.dto.response.DonorResponse;
import com.example.nepalhandsbackend.model.CampaignPayment;
import com.example.nepalhandsbackend.repository.CampaignPaymentRepository;
import com.example.nepalhandsbackend.states.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationService {

    @Autowired
    private CampaignPaymentRepository campaignPaymentRepository;
    public List<DonorResponse> getRecentDonations(Long campaignId){
        List<CampaignPayment> donations =
                campaignPaymentRepository
                        .findTop5ByCampaignIdAndStatusOrderByCreatedAtDesc(
                                campaignId,
                                PaymentStatus.SUCCESS
                        );

        return donations.stream()
                .map(payment -> DonorResponse.builder()
                        .donorName(
                                payment.isAnonymous()
                                        ? "Anonymous Donor"
                                        : payment.getUser().getFirstName() + " " + payment.getUser().getLastName()
                        )
                        .amount(payment.getAmount())
                        .donatedAt(payment.getCreatedAt())
                        .donorId(
                                payment.isAnonymous()
                                        ?null
                                        :payment.getUser().getId())
                        .build())
                .toList();
    }
    public List<DonorResponse> getRecentDonationsForOrganizer( Long userId){
        Long organizerId = userId.longValue();
        List<CampaignPayment> donations =
                campaignPaymentRepository
                        .findTop5ByCampaign_User_IdAndStatusOrderByCreatedAtDesc(
                                organizerId,
                                PaymentStatus.SUCCESS
                        );

        return donations.stream()
                .map(payment -> DonorResponse.builder()
                        .id(payment.getId())
                        .donorName(
                                payment.isAnonymous()
                                        ? "Anonymous Donor"
                                        : payment.getUser().getFirstName() + " " + payment.getUser().getLastName()
                        )
                        .amount(payment.getAmount())
                        .donatedAt(payment.getCreatedAt())
                        .donorId(
                                payment.isAnonymous()
                                        ?null
                                        :payment.getUser().getId())
                        .campaignId(payment.getCampaign().getId())
                        .campaignTitle(payment.getCampaign().getTitle())
                        .build())
                .toList();
    }

    public Page<DonorListResponse> getDonations(
            Long campaignId,
            Integer userId,
            int page,
            int size,
            String direction){
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();
        Long organizerId = userId != null ? userId.longValue() : null;
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CampaignPayment> donations;
        if (campaignId != null) {
            donations =
                    campaignPaymentRepository
                            .findByCampaign_IdAndCampaign_User_IdAndStatus(
                                    campaignId,
                                    organizerId,
                                    PaymentStatus.SUCCESS,
                                    pageable
                            );

        }else {
            donations =
                    campaignPaymentRepository
                            .findByCampaign_User_IdAndStatus(
                                    organizerId,
                                    PaymentStatus.SUCCESS,
                                    pageable
                            );
        }
        return donations.map(donation ->
                DonorListResponse.builder()
                        .Id(donation.getId())
                        .donorName(
                                donation.isAnonymous()
                                        ? "Anonymous Donor"
                                        : donation.getUser().getFirstName() + " " + donation.getUser().getLastName()
                        )
                        .amount(donation.getAmount())
                        .donatedAt(donation.getCreatedAt())
                        .donorId(
                                donation.isAnonymous()
                                        ?null
                                        :donation.getUser().getId())
                        .campaignId(donation.getCampaign().getId())
                        .campaignTitle(donation.getCampaign().getTitle())
                        .anonymous(donation.isAnonymous())
                        .build());
    }
    public DonationDashboardResponse getDonationDashboard(
            Long campaignId,
            Integer userId
    ) {
        Long organizerId = userId.longValue();

        if (campaignId != null) {
            return DonationDashboardResponse.builder()
                    .totalDonors(
                            campaignPaymentRepository.countUniqueDonorsByCampaign(
                                    campaignId,
                                    organizerId
                            )
                    )
                    .totalRaised(
                            campaignPaymentRepository.getTotalRaisedByCampaign(
                                    campaignId,
                                    organizerId
                            )
                    )
                    .averageDonation(
                            campaignPaymentRepository.getAverageDonationByCampaign(
                                    campaignId,
                                    organizerId
                            )
                    )
                    .build();
        }

        return DonationDashboardResponse.builder()
                .totalDonors(
                        campaignPaymentRepository.countUniqueDonors(
                                organizerId
                        )
                )
                .totalRaised(
                        campaignPaymentRepository.getTotalRaised(
                                organizerId
                        )
                )
                .averageDonation(
                        campaignPaymentRepository.getAverageDonation(
                                organizerId
                        )
                )
                .build();
    }

    public List<CampaignPayment> getAllDonationsForExport(
            Long campaignId,
            Integer userId
    ) {
        Long organizerId = userId.longValue();

        if (campaignId != null) {
            return campaignPaymentRepository
                    .findByCampaign_IdAndCampaign_User_IdAndStatusOrderByCreatedAtDesc(
                            campaignId,
                            organizerId,
                            PaymentStatus.SUCCESS
                    );
        }

        return campaignPaymentRepository
                .findByCampaign_User_IdAndStatusOrderByCreatedAtDesc(
                        organizerId,
                        PaymentStatus.SUCCESS
                );
    }
}
