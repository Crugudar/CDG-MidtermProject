package com.ironhack.claudiamidterm.model;

import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.enums.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.*;
import java.time.*;
import java.util.*;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class CheckingAccount extends StudentChecking {

    private final Money monthlyMaintenanceFee=new Money(new BigDecimal("12"));
    @Embedded
    @AttributeOverrides(value ={
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance;
    private boolean belowMinimumBalance;

    @PastOrPresent
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate lastMonthlyFee;

    public CheckingAccount() {
    }

    public CheckingAccount(Money minimumBalance, boolean belowMinimumBalance, @PastOrPresent LocalDate lastMonthlyFee) {
        this.minimumBalance = minimumBalance;
        this.belowMinimumBalance = belowMinimumBalance;
        this.lastMonthlyFee = LocalDate.now();
    }

    public CheckingAccount(AccountHolder primaryOwner, Money balance, String secretKey, AccountStatus status, Money minimumBalance, boolean belowMinimumBalance, @PastOrPresent LocalDate lastMonthlyFee) {
        super(primaryOwner, balance, secretKey, status);
        this.minimumBalance = minimumBalance;
        this.belowMinimumBalance = belowMinimumBalance;
        this.lastMonthlyFee = LocalDate.now();
    }


    public CheckingAccount(AccountHolder primaryOwner, Money balance, String secretKey, AccountStatus status) {
        super(primaryOwner, balance, secretKey, status);
        this.minimumBalance = new Money(new BigDecimal("250"));
        this.lastMonthlyFee = LocalDate.now();
    }

    public Money getMinimumBalance() {
        return minimumBalance;
    }
    public void setMinimumBalance(Money minimumBalance) {
        this.minimumBalance = minimumBalance;
    }
    public Money getMonthlyMaintenanceFee() {
        return monthlyMaintenanceFee;
    }
    public boolean isBelowMinimumBalance() {return belowMinimumBalance;}
    public void setBelowMinimumBalance(boolean belowMinimumBalance) {this.belowMinimumBalance = belowMinimumBalance;}

}