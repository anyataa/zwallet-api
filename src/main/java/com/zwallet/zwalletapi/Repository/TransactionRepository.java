package com.zwallet.zwalletapi.Repository;

import java.util.List;
import java.util.Optional;

import com.zwallet.zwalletapi.Model.Dto.TransactionBalanceHistoryDto;
import com.zwallet.zwalletapi.Model.Entity.AccountEntity;
import com.zwallet.zwalletapi.Model.Entity.TransactionEntity;

import org.apache.catalina.connector.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Integer> {

    List<TransactionEntity> findByFromAccountId(AccountEntity accountId);

    List<TransactionEntity> findByToAccountId(AccountEntity accountId);

    @Query(value = "SELECT * FROM transaction_table WHERE to_account_id = ?1 OR from_account_id = ?1   ORDER BY transaction_id DESC LIMIT 10", nativeQuery = true)
    List<TransactionEntity> findAllTransactionByAccountId(AccountEntity accountId);

    @Query(value = "SELECT * FROM transaction_table  WHERE  to_account_id = ?1 AND transaction_timestamp BETWEEN curdate()-7 AND curdate()  OR from_account_id = ?1 AND transaction_timestamp BETWEEN curdate()-7 AND curdate()  ORDER BY transaction_timestamp ", nativeQuery = true)
    List<TransactionEntity> findAllTransactionLastWeek(AccountEntity accountId);

    @Query(value = "SELECT * FROM transaction_table WHERE  to_account_id = ?1 AND transaction_timestamp BETWEEN curdate()-8 AND curdate()-30  OR from_account_id = ?1 AND transaction_timestamp BETWEEN curdate()-8 AND curdate()-30 ORDER BY transaction_timestamp", nativeQuery = true)
    List<TransactionEntity> findAllTransactionMonth(AccountEntity accountId);

    @Query(value = "SELECT if(from_account_id = ?1 , from_account_balance , to_account_balance), transaction_timestamp FROM transaction_table WHERE transaction_timestamp BETWEEN curdate()-7 AND curdate()  AND  from_account_id = ?1 OR transaction_timestamp BETWEEN curdate()-7 AND curdate()  AND to_account_id = ?1 ", nativeQuery = true)
    List<?> findTransactionBalanceHistory(AccountEntity accountId);

    @Query(value = "SELECT * FROM transaction_table WHERE from_account_id = ?1 AND transaction_timestamp = current_date() OR to_account_id = ?1 AND transaction_timestamp = current_date() ORDER BY transaction_id DESC LIMIT 5", nativeQuery = true)
    List<TransactionEntity> findToday2Transaction(AccountEntity accountId);

    @Query(value = "SELECT * FROM transaction_table WHERE from_account_id = ?1 AND transaction_timestamp BETWEEN curdate()-7 AND curdate() -1 OR to_account_id = ?1 AND transaction_timestamp BETWEEN curdate()-7 AND curdate() -1 ORDER BY transaction_id DESC LIMIT 5", nativeQuery = true)
    List<TransactionEntity> findWeek2Transaction(AccountEntity accountId);

    @Query(value = "SELECT * FROM transaction_table WHERE from_account_id = ?1 AND transaction_timestamp BETWEEN curdate()-30 AND curdate() -8 OR to_account_id = ?1 AND transaction_timestamp BETWEEN curdate()-7 AND curdate() -1 ORDER BY transaction_id DESC LIMIT 5", nativeQuery = true)
    List<TransactionEntity> findMonth2Transaction(AccountEntity accountId);

    @Query(value = "SELECT IF(from_account_id = ?1 , from_account_balance , to_account_balance) FROM transaction_table WHERE from_account_id = ?1 AND transaction_timestamp =curdate()- ?2 OR to_account_id = ?1 AND transaction_timestamp = curdate()- ?2 ORDER BY transaction_id DESC LIMIT 1", nativeQuery = true)
    Optional<Double> findPerDay(AccountEntity accountId, Integer dayMinus);

}
