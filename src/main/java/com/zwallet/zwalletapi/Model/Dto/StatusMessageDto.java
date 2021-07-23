package com.zwallet.zwalletapi.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusMessageDto<T> {
    private String status;
    private String message;
    private T data;
}