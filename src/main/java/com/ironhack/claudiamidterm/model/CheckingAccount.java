package com.ironhack.claudiamidterm.model;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.enums.*;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class CheckingAccount extends StudentChecking {

    @Embedded
    @AttributeOverrides(value ={
            @AttributeOverride(name = "amount", column = @Column(name = "monthly_maintenance_fee_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "monthly_maintenance_fee_currency"))
    })
    private Money monthlyMaintenanceFee;
    @Embedded
    @AttributeOverrides(value ={
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance;
    private boolean belowMinimumBalance;


    public CheckingAccount() {}

    public CheckingAccount(AccountHolder primaryOwner, Money balance, String secretKey, AccountStatus status) {
        super(primaryOwner, balance, secretKey, status);
        this.belowMinimumBalance = false;
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
    public void setMonthlyMaintenanceFee(Money monthlyMaintenanceFee) {this.monthlyMaintenanceFee = monthlyMaintenanceFee;}
    public boolean isBelowMinimumBalance() {return belowMinimumBalance;}
    public void setBelowMinimumBalance(boolean belowMinimumBalance) {this.belowMinimumBalance = belowMinimumBalance;}

}