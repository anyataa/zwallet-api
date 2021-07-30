package com.zwallet.zwalletapi.Model.Dto;

import java.util.List;

import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionPeriodDto {
    private List<TransactionEntity> weekly;
    private List<TransactionEntity> monthly;
    private List<TransactionEntity> dailyStartFromMonday;
}
