package com.zwallet.zwalletapi.Repository;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Entity.FriendshipEntity;
import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, Integer> {
  List<FriendshipEntity> findByUser(UserDetailEntity user);

  @Query(value = "select friendship_id, user_detail_table.user_id, username, user_image, phone_number_table.phone_number from friendship_table join user_detail_table on friendship_table.friend_id = user_detail_table.user_id join phone_number_table on friendship_table.friend_id = phone_number_table.user_id where friendship_table.user_id = ?", nativeQuery = true)
  List<Object> findFriends(Integer id);

  @Query(value = "SELECT * FROM friendship_table WHERE user_id = ?1 AND friend_id = ?2", nativeQuery = true)
  List<FriendshipEntity> findFriendshipStatus(UserDetailEntity user, UserDetailEntity friend);

}
