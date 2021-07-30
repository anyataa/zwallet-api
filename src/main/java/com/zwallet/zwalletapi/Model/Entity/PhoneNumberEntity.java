package com.zwallet.zwalletapi.Model.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phone_number_table")
@Data
@NoArgsConstructor
public class PhoneNumberEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY) 
  private Integer phoneNumberId;

  @Column(length = 50, nullable = false)
  private String phoneNumber;

  @Column()
  private boolean isDeleted;

  @Column(nullable = true)
  private boolean isPrimary;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserDetailEntity user;

  public PhoneNumberEntity(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
