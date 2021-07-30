package com.zwallet.zwalletapi.Repository;

import java.util.List;

import com.zwallet.zwalletapi.Model.Entity.PhoneNumberEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumberEntity, Integer> {

  List<PhoneNumberEntity> findByUser(UserDetailEntity userDetail);

  PhoneNumberEntity findByUserAndIsPrimary(UserDetailEntity userDetail, boolean primary);
}