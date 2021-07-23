package com.zwallet.zwalletapi.Repository;

import com.zwallet.zwalletapi.Model.Entity.AccountEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<AccountEntity, Integer> {

}
