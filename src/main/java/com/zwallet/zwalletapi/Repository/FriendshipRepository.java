package com.zwallet.zwalletapi.Repository;

import java.util.List;

import com.zwallet.zwalletapi.Model.Entity.FriendshipEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Integer> {
  List<FriendshipEntity> findByUser(UserDetailEntity user);
}
