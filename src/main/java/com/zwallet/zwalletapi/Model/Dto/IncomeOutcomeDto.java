package com.zwallet.zwalletapi.Model.Dto;

import java.util.List;

import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IncomeOutcomeDto {
    private Double sumIncome;
    private Double sumOutcome;
    private List<TransactionEntity> listIncome;
    private List<TransactionEntity> listOutcome;
    private List<TransactionEntity> listTransaction;

    public IncomeOutcomeDto(Double sumIncome, Double sumOutcome, List<TransactionEntity> listIncome,
            List<TransactionEntity> listOutcome, List<TransactionEntity> listTransaction) {
        this.sumIncome = sumIncome;
        this.sumOutcome = sumOutcome;
        this.listIncome = listIncome;
        this.listOutcome = listOutcome;
        this.listTransaction = listTransaction;
    }

}
