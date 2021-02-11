package com.ironhack.claudiamidterm.controller.DTO;

import com.ironhack.claudiamidterm.enums.*;

import javax.validation.constraints.*;
import java.math.*;

public class CheckingAccountDTO {

    @NotNull
    private Long primaryOwnerId;
    private Long secondaryOwnerId;
    @NotNull
    private BigDecimal balance;
    @NotNull
    private String secretKey;


    public CheckingAccountDTO() {
    }

    public CheckingAccountDTO(@NotNull Long primaryOwnerId, @NotNull Double balance, @NotNull String secretKey) {
        this.primaryOwnerId = primaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.secretKey = secretKey;

    }

    public CheckingAccountDTO(@NotNull Long primaryOwnerId, Long secondaryOwnerId, @NotNull Double balance, @NotNull String secretKey) {
        this.primaryOwnerId = primaryOwnerId;
        this.secondaryOwnerId = secondaryOwnerId;
        this.balance = new BigDecimal(balance);
        this.secretKey = secretKey;

    }

    public Long getPrimaryOwnerId() {
        return primaryOwnerId;
    }

    public void setPrimaryOwnerId(Long primaryOwnerId) {
        this.primaryOwnerId = primaryOwnerId;
    }

    public Long getSecondaryOwnerId() {
        return secondaryOwnerId;
    }

    public void setSecondaryOwnerId(Long secondaryOwnerId) {
        this.secondaryOwnerId = secondaryOwnerId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


}
