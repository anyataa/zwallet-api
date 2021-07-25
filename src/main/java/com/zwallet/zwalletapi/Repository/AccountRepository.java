package com.zwallet.zwalletapi.Repository;

import java.util.Optional;

import com.zwallet.zwalletapi.Model.Entity.AccountEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    @Query(value = " SELECT * FROM account_table a LEFT JOIN user_detail_table u ON a.user_id = u.user_id WHERE u.user_id = ?1", nativeQuery = true)
    Optional<AccountEntity> getAccountUserByUserId(Integer userId);
}
