package com.example.nepalhandsbackend.service;

import com.example.nepalhandsbackend.dto.request.KycRequest;
import com.example.nepalhandsbackend.dto.response.KycResponse;
import com.example.nepalhandsbackend.model.Kyc;
import com.example.nepalhandsbackend.repository.KycRepository;
import com.example.nepalhandsbackend.utils.FileTextUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KycService {
    @Autowired
    private FileTextUtils fileTextUtils;
    private final KycRepository kycRepository;

    public Kyc postKyc(KycRequest request){
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
                .build();

        return kycRepository.save(kyc);
    }

    @Transactional(readOnly = true)
    public KycResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }


    private Kyc findOrThrow(Long id) {
        return kycRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Kyc not found: " + id));
    }
    private KycResponse toResponse(Kyc e) {
        return KycResponse.builder()
                .id(e.getId())
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
                .build();

    }
}
