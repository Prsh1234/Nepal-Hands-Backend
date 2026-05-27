package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.KycRequest;
import com.example.nepalhandsbackend.dto.response.KycResponse;
import com.example.nepalhandsbackend.model.Kyc;
import com.example.nepalhandsbackend.model.User;
import com.example.nepalhandsbackend.repository.KycRepository;
import com.example.nepalhandsbackend.repository.UserRepository;
import com.example.nepalhandsbackend.states.KycStatus;
import com.example.nepalhandsbackend.states.Role;
import com.example.nepalhandsbackend.utils.FileTextUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KycService {
    @Autowired
    private FileTextUtils fileTextUtils;
    private final KycRepository kycRepository;
    @Autowired
    private UserRepository userRepository;
    public Kyc postKyc(KycRequest request,Integer userId){
        User user = userRepository.findById(userId)
                .orElseThrow();
        Kyc kyc = Kyc.builder()
                .fullName(request.getFullName())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .citizenshipNumber(request.getCitizenshipNumber())
                .citizenshipIssuedDistrict(request.getCitizenshipIssuedDistrict())
                .citizenshipIssuedDate(request.getCitizenshipIssuedDate())
                .panNumber(request.getPanNumber())
                .phoneNumber(request.getPhoneNumber())
                .email(request.getEmail())
                .province(request.getProvince())
                .district(request.getDistrict())
                .municipality(request.getMunicipality())
                .wardNumber(request.getWardNumber())
                .tole(request.getTole())
                .permanentAddress(request.getPermanentAddress())
                .temporaryAddress(request.getTemporaryAddress())
                .occupation(request.getOccupation())
                .sourceOfFunds(request.getSourceOfFunds())
                .citizenshipFront(fileTextUtils.toBytes(request.getCitizenshipFront()))
                .citizenshipBack(fileTextUtils.toBytes(request.getCitizenshipBack()))
                .panDocument(fileTextUtils.toBytes(request.getPanDocument()))
                .user(user)
                .build();

        return kycRepository.save(kyc);
    }

    @Transactional(readOnly = true)
    public KycResponse getById(Long id) {

        return toResponse(kycRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Kyc not found: " + id)));
    }


    @Transactional(readOnly = true)
    public Page<KycResponse> findByStatus(KycStatus status, Pageable pageable) {
        return kycRepository.findByStatus(status, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public KycResponse updateStatus(Long id, KycStatus status, String reason) {

        Kyc kyc = kycRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("KYC not found: " + id));

        kyc.setStatus(status);

        User user = kyc.getUser();

        if (status == KycStatus.REJECTED) {
            kyc.setRejectionReason(reason);

            // Optional:
            // remove organizer role if rejected
            // user.removeRole(Role.ROLE_ORGANIZER);

        } else {
            kyc.setRejectionReason(null);

            // Add organizer role on approval
            if (status == KycStatus.APPROVED) {
                user.addRole(Role.ROLE_ORGANIZER);
            }
        }

        userRepository.save(user);

        return toResponse(kycRepository.save(kyc));
    }
    private KycResponse toResponse(Kyc e) {
        return KycResponse.builder()
                .id(e.getId())
                .userId(e.getUser().getId())
                .status(e.getStatus())
                .fullName(e.getFullName())
                .dateOfBirth(e.getDateOfBirth())
                .gender(e.getGender())
                .citizenshipNumber(e.getCitizenshipNumber())
                .citizenshipIssuedDistrict(e.getCitizenshipIssuedDistrict())
                .citizenshipIssuedDate(e.getCitizenshipIssuedDate())
                .panNumber(e.getPanNumber())
                .phoneNumber(e.getPhoneNumber())
                .email(e.getEmail())
                .province(e.getProvince())
                .district(e.getDistrict())
                .municipality(e.getMunicipality())
                .wardNumber(e.getWardNumber())
                .tole(e.getTole())
                .permanentAddress(e.getPermanentAddress())
                .temporaryAddress(e.getTemporaryAddress())
                .occupation(e.getOccupation())
                .sourceOfFunds(e.getSourceOfFunds())
                .citizenshipFront(e.getCitizenshipFront())
                .citizenshipBack(e.getCitizenshipBack())
                .panDocument(e.getPanDocument())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .build();

    }
}
