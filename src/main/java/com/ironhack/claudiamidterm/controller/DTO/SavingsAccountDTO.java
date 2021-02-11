package com.ironhack.claudiamidterm.controller.DTO;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.enums.*;
import com.ironhack.claudiamidterm.model.*;

import javax.validation.constraints.*;
import java.math.*;
import java.util.*;

public class SavingsAccountDTO {

    @NotNull
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    @NotNull
    private BigDecimal balance;
    @NotNull
    private String secretKey;
    @DecimalMax(value = "0.5", message = "The interest rate cannot be higher than 0.5")
    @DecimalMin(value = "0" , message = "The interest rate cannot be 0 or below")
    private BigDecimal interestRate;
    @DecimalMax(value = "1000.00", message = "The maximum balance is 1000")
    @DecimalMin(value = "100.00", message = "The minimum balance is 100")
    private BigDecimal minimumBalance;


    public SavingsAccountDTO() {
    }

    public SavingsAccountDTO(@NotNull Long primaryOwnerId, Long secondaryOwnerId, @NotNull Double balance, @NotNull String secretKey, @DecimalMax(value = "0.5", message = "The interest rate has to be less than 0.5") @DecimalMin(value = "0", message = "The interest rate has to be more than 0") Double interestRate, @DecimalMax(value = "1000.00", message = "The minimum balance has to be less than 1000") @DecimalMin(value = "100.00", message = "The minimum balance has to be more than 100") Double minimumBalance) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.secretKey = secretKey;

        this.minimumBalance = new BigDecimal(minimumBalance);
        this.interestRate = new BigDecimal(interestRate);
    }

    public SavingsAccountDTO(@NotNull Long primaryOwnerId, Long secondaryOwnerId, @NotNull Double balance, @NotNull String secretKey, @DecimalMax(value = "1000.00", message = "The minimum balance has to be less than 1000") @DecimalMin(value = "100.00", message = "The minimum balance has to be more than 100") BigDecimal minimumBalance){
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.secretKey = secretKey;
        this.minimumBalance = minimumBalance;
        this.interestRate = new BigDecimal("0.0025");
    }

    public SavingsAccountDTO(@NotNull Long primaryOwnerId, Long secondaryOwnerId, @NotNull Double balance, @NotNull String secretKey,@DecimalMax(value = "0.5", message = "The interest rate has to be less than 0.5") @DecimalMin(value = "0", message = "The interest rate has to be more than 0") Double interestRate ){
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.secretKey = secretKey;
        this.minimumBalance = new BigDecimal("1000");
        this.interestRate = new BigDecimal(interestRate);
    }

    public SavingsAccountDTO(@NotNull Long primaryOwnerId, Long secondaryOwnerId, @NotNull Double balance, @NotNull String secretKey) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.secretKey = secretKey;
        this.minimumBalance = new BigDecimal("1000");
        this.interestRate = new BigDecimal("0.0025");
    }


    public Long getPrimaryOwnerId() {return primaryOwnerId;}
    public void setPrimaryOwnerId(Long primaryOwnerId) {this.primaryOwnerId = primaryOwnerId;}
    public Long getSecondaryOwnerId() {return secondaryOwnerId;}
    public void setSecondaryOwnerId(Long secondaryOwnerId) {this.secondaryOwnerId = secondaryOwnerId;}
    public BigDecimal getBalance() {return balance;}
    public void setBalance(BigDecimal balance) {this.balance = balance;}
    public String getSecretKey() {return secretKey;}
    public void setSecretKey(String  secretKey) {this.secretKey = secretKey;}
    public BigDecimal getMinimumBalance() {return minimumBalance;}
    public void setMinimumBalance(BigDecimal minimumBalance) {this.minimumBalance = minimumBalance;}
    public BigDecimal getInterestRate() {return interestRate;}
    public void setInterestRate(BigDecimal interestRate) {this.interestRate = interestRate;}

}
