package com.ironhack.claudiamidterm.model;

import com.ironhack.claudiamidterm.classes.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.*;

@Entity
public class Transference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "origin_account")
    private Account originAccount;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "destination_account")
    private Account destinationAccount;
    @NotNull
    private Money amount;
    private LocalDateTime transferenceDate;


    public Transference() {}

    public Transference(@NotNull Account originAccount, @NotNull Account destinationAccount, @NotNull Money amount) {
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.transferenceDate = LocalDateTime.now();
    }


    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public Account getOriginAccount() {return originAccount;}
    public void setOriginAccount(Account originAccount) {this.originAccount = originAccount;}
    public Account getDestinationAccount() {return destinationAccount;}
    public void setDestinationAccount(Account destinationAccount) {this.destinationAccount = destinationAccount;}
    public Money getAmount() {return amount;}
    public void setAmount(Money amount) {this.amount = amount;}
    public LocalDateTime getTransactionDate() {return transferenceDate;}
    public void setTransactionDate(LocalDateTime transactionDate) {this.transferenceDate = transactionDate;}
}