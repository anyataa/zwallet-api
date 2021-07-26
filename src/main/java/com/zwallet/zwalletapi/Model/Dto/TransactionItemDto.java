package com.zwallet.zwalletapi.Model.Dto;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionItemDto {
    private String sender;
    private String receiver;
    private Double amount;
    private Date date;

}
