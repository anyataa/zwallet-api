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
    private List<TransactionItemDto> list2Day;
    private List<TransactionItemDto> list2Week;
    private List<TransactionItemDto> list2Month;

    public IncomeOutcomeDto(Double sumIncome, Double sumOutcome, List<TransactionItemDto> listIncome,
            List<TransactionItemDto> listOutcome, List<TransactionItemDto> listTransaction, List<?> listBalance,
            List<TransactionItemDto> list2Day, List<TransactionItemDto> list2Week,
            List<TransactionItemDto> list2Month) {
        this.sumIncome = sumIncome;
        this.sumOutcome = sumOutcome;
        this.listIncome = listIncome;
        this.listOutcome = listOutcome;
        this.listTransaction = listTransaction;
        this.listBalance = listBalance;
        this.list2Day = list2Day;
        this.list2Week = list2Week;
        this.list2Month = list2Month;

    }

}
