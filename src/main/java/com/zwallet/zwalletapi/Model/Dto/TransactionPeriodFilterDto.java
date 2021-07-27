package com.zwallet.zwalletapi.Model.Dto;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionPeriodFilterDto {
    private List<TransactionItemDto> weekly;
    private List<TransactionItemDto> monthly;
    private List<TransactionItemDto> weeklyFromMonday;
}
