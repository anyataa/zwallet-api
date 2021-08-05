package com.zwallet.zwalletapi.Service;

import com.zwallet.zwalletapi.Model.Dto.MailDto;

public interface MailService {
    void sendMail(MailDto mailDto);
  }