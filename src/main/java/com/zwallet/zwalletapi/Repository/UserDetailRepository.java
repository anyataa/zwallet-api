package com.zwallet.zwalletapi.Repository;

import java.util.List;

import com.zwallet.zwalletapi.Model.Entity.UserDetailEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetailEntity, Integer> {
    UserDetailEntity findByUsername(String username);

    UserDetailEntity findByEmail(String email);

    @Query(value = "select * from user_detail_table where is_deleted = 0", nativeQuery = true)
    List<UserDetailEntity> findAllActive();

    List<UserDetailEntity> findByIsDeleted(boolean status);
}
