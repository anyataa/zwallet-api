package com.zwallet.zwalletapi.Model.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionReturnDto {
    private Double senderBalance;
    private Double receiverBalance;
    private String sender;
    private String receiver;
    private Double amount;

}
