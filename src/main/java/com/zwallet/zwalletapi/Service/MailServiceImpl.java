package com.zwallet.zwalletapi.Service;

import com.zwallet.zwalletapi.Model.Dto.MailDto;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {
  private final JavaMailSender javaMailSender;

  @Override
  public void sendMail(MailDto mailDto) {
    SimpleMailMessage message = new SimpleMailMessage();

    message.setTo(mailDto.getRecipient());
    message.setSubject(mailDto.getSubject());
    message.setText(mailDto.getMessage());

    javaMailSender.send(message);
  }

}
