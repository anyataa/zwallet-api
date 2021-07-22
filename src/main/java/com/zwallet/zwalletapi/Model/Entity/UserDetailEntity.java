package com.zwallet.zwalletapi.Model.Entity;

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

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_detail_table")
public class UserDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(length = 50, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 6, nullable = false)
    private String pin;

    @Column(length = 50, nullable = true)
    private String userFname;

    @Column(length = 50, nullable = true)
    private String userLname;

    @Column(length = 255, nullable = true)
    private String userImage;

    @ManyToOne
    @JoinColumn(name = "phone_number_id")
    private PhoneNumberEntity phoneNumber;

    @Column(length = 50, nullable = true)
    private String bankNumber;

    @Column(length = 10, nullable = false)
    private String userRole;

    @Column()
    private boolean isActive = false;

    @Column()
    private boolean isDeleted = false;

    public UserDetail(String username, String email, String password, String pin, String userFname, String userLname,
            String userImage, PhoneNumber phoneNumber, String bankNumber, String userRole) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.pin = pin;
        this.userFname = userFname;
        this.userLname = userLname;
        this.userImage = userImage;
        this.phoneNumber = phoneNumber;
        this.bankNumber = bankNumber;
        this.userRole = userRole;
    }

}
