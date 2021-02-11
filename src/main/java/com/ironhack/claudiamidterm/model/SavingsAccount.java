package com.ironhack.claudiamidterm.model;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.enums.*;

import javax.persistence.*;
import java.math.*;
import java.time.*;

@Entity
@PrimaryKeyJoinColumn(name="id")
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public class SavingsAccount extends Account{


    private String secretKey;
    @Enumerated(value = EnumType.STRING)
    private AccountStatus status=AccountStatus.ACTIVE;
    @Embedded
    @AttributeOverrides(value ={
            @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency"))
    })
    private Money minimumBalance;
    private BigDecimal interestRate;
    private boolean belowMinimumBalance;
    private LocalDate lastInterestUpdate;


    public SavingsAccount() {}

    public SavingsAccount(AccountHolder primaryOwner, Money balance, String secretKey) {
        super(primaryOwner, balance);
        this.secretKey = secretKey;
        this.belowMinimumBalance =  false;
    }


    public Money getMinimumBalance() {return minimumBalance;}
    public void setMinimumBalance(Money minimumBalance) {this.minimumBalance = minimumBalance;}
    public BigDecimal getInterestRate() {return interestRate;}
    public void setInterestRate(BigDecimal interestRate) {this.interestRate = interestRate;}
    public boolean isBelowMinimumBalance() {return belowMinimumBalance; }
    public void setBelowMinimumBalance(boolean belowMinimumBalance) {this.belowMinimumBalance = belowMinimumBalance;}
    public String getSecretKey() {return secretKey;}
    public void setSecretKey(String secretKey) {this.secretKey = secretKey;}
    public AccountStatus getStatus() {return status;}
    public void setStatus(AccountStatus status) {this.status = status;}
    public LocalDate getLastInterestUpdate() {return lastInterestUpdate;}
    public void setLastInterestUpdate(LocalDate lastInterestUpdate) {this.lastInterestUpdate = lastInterestUpdate;}

}
