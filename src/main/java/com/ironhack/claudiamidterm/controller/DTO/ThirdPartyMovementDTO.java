package com.ironhack.claudiamidterm.controller.DTO;

import javax.validation.constraints.*;
import java.math.*;

public class ThirdPartyMovementDTO {

    @NotNull
    @NotEmpty
    private Long accountId;
    @NotNull
    @NotEmpty
    private String accountSecretKey;
    @NotNull
    @NotEmpty
    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;
    @NotNull
    private String name;
    @NotNull
    private String hashedKey;


    public ThirdPartyMovementDTO() {
    }

    public ThirdPartyMovementDTO(@NotNull @NotEmpty Long accountId, @NotNull @NotEmpty String accountSecretKey, @NotNull @NotEmpty @Digits(integer = 6, fraction = 2) BigDecimal amount, @NotNull String name, @NotNull String hashedKey) {
        this.accountId = accountId;
        this.accountSecretKey = accountSecretKey;
        this.amount = amount;
        this.name = name;
        this.hashedKey = hashedKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedKey() {
        return hashedKey;
    }

    public void setHashedKey(String hashedKey) {
        this.hashedKey = hashedKey;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountSecretKey() {
        return accountSecretKey;
    }

    public void setAccountSecretKey(String accountSecretKey) {
        this.accountSecretKey = accountSecretKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
