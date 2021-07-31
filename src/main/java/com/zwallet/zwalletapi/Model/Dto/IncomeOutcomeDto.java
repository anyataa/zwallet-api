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
    private List<TransactionItemDto> listIncome;
    private List<TransactionItemDto> listOutcome;
    private List<TransactionItemDto> listTransaction;
    private List<?> listBalance;

    public IncomeOutcomeDto(Double sumIncome, Double sumOutcome, List<TransactionItemDto> listIncome,
            List<TransactionItemDto> listOutcome, List<TransactionItemDto> listTransaction, List<?> listBalance) {
        this.sumIncome = sumIncome;
        this.sumOutcome = sumOutcome;
        this.listIncome = listIncome;
        this.listOutcome = listOutcome;
        this.listTransaction = listTransaction;
        this.listBalance = listBalance;
    }

}
