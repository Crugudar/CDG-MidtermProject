package com.ironhack.claudiamidterm.model;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.enums.*;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class StudentChecking extends Account {

    protected String secretKey;
    @Enumerated(value = EnumType.STRING)
    private AccountStatus status=AccountStatus.ACTIVE;


    public StudentChecking() {}

    public StudentChecking(AccountHolder primaryOwner, Money balance, String secretKey, AccountStatus status) {
        super(primaryOwner, balance);
        this.secretKey = secretKey;
    }



    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}