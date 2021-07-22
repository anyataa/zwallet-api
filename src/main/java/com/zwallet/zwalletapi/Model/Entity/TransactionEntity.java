package com.zwallet.zwalletapi.Model.Entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transaction_table")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transactionId;

    @Column(name = "transaction_amount", nullable = false)
    private Double transactionAmount;

    @Column(name = "transaction_notes", nullable = false, length = 100)
    private String transactionNotes;

    @Column(name = "transaction_timestamp", nullable = false)
    private Date transactionTimestamp;

    // 0 : Debit | 1 : Credit
    @Column(name = "transaction_type", nullable = false)
    private Integer transactionType;

    // 1 : Transfer | 2 : Subscription | 3 : Payment | 4 : Top Up | 5 : Retrieve
    @Column(name = "transaction_detail", nullable = false)
    private Integer transactionDetail;

    @Column(name = "is_success", nullable = false)
    private Integer isSuccess;

    // Sender | Type Transaction : 0 Debit
    @ManyToOne
    @JoinColumn(name = "from_account_id", nullable = false, updatable = true, insertable = true)
    private AccountEntity fromAccountId;

    // Receiver | Type Transaction : 1 Credit
    @ManyToOne
    @JoinColumn(name = "to_account_id", nullable = false, updatable = true, insertable = true)
    private AccountEntity toAccountId;

}
