package com.example.nepalhandsbackend.dto.response;

import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EsewaSignatureResponse {
    private String signature;
    private String signed_field_names;
    private String transaction_uuid;


}
