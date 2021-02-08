package com.ironhack.claudiamidterm.controller.DTO;

import javax.validation.constraints.*;
import java.math.*;

public class CreditCardDTO {


    @NotNull
    @DecimalMax(value = "0.2", message = "The interest rate cannot be higher than 0.2%")
    @DecimalMin(value = "0.1", message = "The interest rate cannot be lower than 0.1%")
    private BigDecimal interestRate;
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    @NotNull
    private BigDecimal balance;
    @DecimalMax(value = "100000.00", message = "Maximum credit is 100000")
    @DecimalMin(value = "100.00", message = "Minimum credit is 100")
    private BigDecimal creditLimit;

    public CreditCardDTO() {
    }

    public CreditCardDTO(@DecimalMax(value = "0.2", message = "The interest rate can not be higher than 0.2") @DecimalMin(value = "0.1", message = "The interest rate can not be lower than 0.1") Double interestRate, @NotNull Long primaryOwnerId, Long secondaryOwnerId, @NotNull Double balance, @DecimalMax(value = "100000.00", message = "The credit limit can not be higher than 100000") @DecimalMin(value = "100.00", message = "The credit limit can not be lower than 100000") Double creditLimit) {
        this.interestRate = new BigDecimal(interestRate);
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.creditLimit = new BigDecimal(creditLimit);

    }


    public CreditCardDTO( @NotNull Long primaryOwnerId, Long secondaryOwnerId, @NotNull Double balance, @DecimalMax(value = "100000.00", message = "The credit limit can not be higher than 100000") @DecimalMin(value = "100.00", message = "The credit limit can not be lower than 100000") Double creditLimit) {
        this.interestRate = new BigDecimal("0.2");
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.creditLimit = new BigDecimal(creditLimit);

    }

    public CreditCardDTO( @DecimalMax(value = "0.2", message = "The interest rate can not be higher than 0.2") @DecimalMin(value = "0.1", message = "The interest rate can not be lower than 0.1") Double interestRate, @NotNull Long primaryOwnerId, Long secondaryOwnerId, @NotNull Double balance) {
        this.interestRate = new BigDecimal(interestRate);
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.creditLimit = new BigDecimal("100");
    }

    public CreditCardDTO(@NotNull Long primaryOwnerId, Long secondaryOwnerId, @NotNull Double balance) {
        this.interestRate = new BigDecimal("0.2");
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.creditLimit = new BigDecimal("100");
    }


    public Long getPrimaryOwnerId() {return primaryOwnerId;}
    public void setPrimaryOwnerId(Long primaryOwnerId) {this.primaryOwnerId = primaryOwnerId;}
    public Long getSecondaryOwnerId() {return secondaryOwnerId;}
    public void setSecondaryOwnerId(Long secondaryOwnerId) {this.secondaryOwnerId = secondaryOwnerId;}
    public BigDecimal getBalance() {return balance;}
    public void setBalance(BigDecimal balance) {this.balance = balance;}
    public BigDecimal getCreditLimit() {return creditLimit;}
    public void setCreditLimit(BigDecimal creditLimit) {this.creditLimit = creditLimit;}
    public BigDecimal getInterestRate() {return interestRate;}
    public void setInterestRate(BigDecimal interestRate) {this.interestRate = interestRate;}
}
