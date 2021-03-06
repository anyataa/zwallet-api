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
    private String senderImage;
    private String receiver;
    private String receiverImage;
    private Double amount;
    private Timestamp date;
    private Integer transactionType;
    private Integer transactionDetails;
    private String transactionNotes;

    public TransactionItemDto(String sender, String senderImage, String receiver, String receiverImage, Double amount,
            Timestamp date, Integer transactionType, Integer transactionDetails, String transactionNotes) {
        this.sender = sender;
        this.senderImage = senderImage;
        this.receiver = receiver;
        this.receiverImage = receiverImage;
        this.amount = amount;
        this.date = date;
        this.transactionType = transactionType;
        this.transactionDetails = transactionDetails;
        this.transactionNotes = transactionNotes;
    }

}
