package com.zwallet.zwalletapi.Controller;

import com.zwallet.zwalletapi.Model.Dto.MailDto;
import com.zwallet.zwalletapi.Service.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailController {
  @Autowired
  private MailService mailService;

  @PostMapping("/send")
  public ResponseEntity<?> sendMail(@RequestBody MailDto mailDto) {
    mailService.sendMail(mailDto);
    return ResponseEntity.ok().body("Email sent successfully!");
  }
}
