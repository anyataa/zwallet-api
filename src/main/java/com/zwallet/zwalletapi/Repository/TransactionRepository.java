package com.zwallet.zwalletapi.Repository;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    public Optional<List<TransactionEntity>> findByFromAccountId(Integer sender);

    public Optional<List<TransactionEntity>> findByToAccountId(Integer receiver);

}
