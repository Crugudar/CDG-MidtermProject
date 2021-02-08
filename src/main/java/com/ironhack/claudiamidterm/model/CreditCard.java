package com.ironhack.claudiamidterm.model;

import com.ironhack.claudiamidterm.classes.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.*;
import java.time.*;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class CreditCard extends Account{

    @NotNull
    @Embedded
    @AttributeOverrides(value ={
            @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency"))
    })
    private Money creditLimit;
    @NotNull
    @DecimalMax(value = "0.2")
    @DecimalMin(value = "0.1")
    private BigDecimal interestRate;
    private LocalDate lastInterestUpdate;


    public CreditCard() {}

    public CreditCard(AccountHolder primaryOwner, Money balance) {super(primaryOwner, balance);}


    public Money getCreditLimit() {return creditLimit;}
    public void setCreditLimit(Money creditLimit) {this.creditLimit = creditLimit;}
    public BigDecimal getInterestRate() {return interestRate;}
    public void setInterestRate(BigDecimal interestRate) {this.interestRate = interestRate;}
    public LocalDate getLastInterestUpdate() {return lastInterestUpdate; }
    public void setLastInterestUpdate(LocalDate lastInterestUpdate) {this.lastInterestUpdate = lastInterestUpdate; }


}
