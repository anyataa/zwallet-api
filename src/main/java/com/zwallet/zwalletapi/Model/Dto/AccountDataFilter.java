package com.zwallet.zwalletapi.Model.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDataFilter {
    private Double senderBalance;
    private String sender;
    private String receiver;

}
