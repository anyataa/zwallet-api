package com.zwallet.zwalletapi.Model.Dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionItemDto {
    private String sender;
    private String receiver;
    private Double amount;
    private Timestamp date;
    private Integer transactionType;
    private Integer transactionDetails;
    private String transactionNotes;

    public TransactionItemDto(String sender, String receiver, Double amount, Timestamp date, Integer transactionType,
            Integer transactionDetails, String transactionNotes) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.date = date;
        this.transactionType = transactionType;
        this.transactionDetails = transactionDetails;
        this.transactionNotes = transactionNotes;
    }

}
