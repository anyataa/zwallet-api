package com.zwallet.zwalletapi.Controller;

import com.zwallet.zwalletapi.Model.Dto.MailDto;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;
import com.zwallet.zwalletapi.Repository.UserDetailRepository;
import com.zwallet.zwalletapi.Service.MailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/mail")
@CrossOrigin(origins = "*")
public class MailController {
  @Autowired
  private MailService mailService;

  @Autowired
  private UserDetailRepository userDetailRepository;

  private JavaMailSender javaMailSender;

  @PostMapping("/send")
  public ResponseEntity<?> sendMail(@RequestBody MailDto mailDto) {
    mailService.sendMail(mailDto);
    return ResponseEntity.ok().body("Email sent successfully!");
  }
  
  @PostMapping("/sendresetpass/{id}")
  public ResponseEntity<?> mailResetPass(@RequestBody MailDto mailDto, @PathVariable Integer id) {
    SimpleMailMessage mailMessage = new SimpleMailMessage();
    UserDetailEntity userDetailEntity = userDetailRepository.findById(id).get();

    mailMessage.setTo(mailDto.getRecipient());
    mailMessage.setSubject("Zwallet - Reset Password");
    mailMessage.setText("Hello " + userDetailEntity.getUsername()
        + "," + "\nClick link attached below :" + "\nhttp://localhost:3000/createNewPassword/" + userDetailEntity.getUserId() + "\nThank you");

    javaMailSender.send(mailMessage);
    return ResponseEntity.ok().body("Email sent successfully!");
  }
}
