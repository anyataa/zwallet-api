package com.zwallet.zwalletapi.Model.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionGraphDto {
    Double first;
    Double second;
    Double third;
    Double forth;
    Double fifth;
    Double sixth;
    Double seventh;

    public TransactionGraphDto(Double first, Double second, Double third, Double forth, Double fifth, Double sixth,
            Double seventh) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.forth = forth;
        this.fifth = fifth;
        this.sixth = sixth;
        this.seventh = seventh;
    }

}
