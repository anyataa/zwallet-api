package com.zwallet.zwalletapi.Model.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberDto {
  private String phoneNumber;
  private boolean isPrimary;
  private Integer userId;
  private String phoneNumberId;
}
