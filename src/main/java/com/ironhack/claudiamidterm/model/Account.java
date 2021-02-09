package com.ironhack.claudiamidterm.model;

import com.ironhack.claudiamidterm.classes.*;

import javax.persistence.*;
import java.math.*;
import java.time.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @ManyToOne(optional = false)
    protected AccountHolder primaryOwner;
    @ManyToOne(optional = true)
    protected AccountHolder secondaryOwner;
    @Embedded
    @AttributeOverrides(value ={
            @AttributeOverride(name = "amount", column = @Column(name = "balance_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "balance_currency"))
    })
    protected Money balance;

    @Embedded
    @AttributeOverrides(value ={
            @AttributeOverride(name = "amount", column = @Column(name = "penalty_fee_amount")),
            @AttributeOverride(name = "currency", column = @Column(name ="penalty_fee_currency"))
    })
    protected Money penaltyFee;

    @OneToMany(mappedBy = "originAccount")
    protected List<Transference> sentTransferences;
    @OneToMany(mappedBy = "destinationAccount")
    protected List<Transference> recievedTransferences;


    protected LocalDate createdDate;



    public Account() {}

    public Account(AccountHolder primaryOwner, Money balance) {
        this.primaryOwner = primaryOwner;
        this.balance = balance;
        this.penaltyFee = new Money(new BigDecimal("40"));
        this.createdDate = LocalDate.now();
    }

    public Account(AccountHolder primaryOwner, AccountHolder secondaryOwner, Money balance) {
        this.primaryOwner = primaryOwner;
        this.secondaryOwner = secondaryOwner;
        this.balance = balance;
        this.penaltyFee = new Money(new BigDecimal("40"));
        this.createdDate = LocalDate.now();
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public AccountHolder getPrimaryOwner() {return primaryOwner;}
    public void setPrimaryOwner(AccountHolder primaryOwner) {this.primaryOwner = primaryOwner;}
    public Money getBalance() {return balance;}
    public void setBalance(Money balance) {this.balance = balance;}
    public Money getPenaltyFee() {return penaltyFee;}
    public void setPenaltyFee(Money penaltyFee) {this.penaltyFee = penaltyFee;}
    public AccountHolder getSecondaryOwner() {return secondaryOwner;}
    public void setSecondaryOwner(AccountHolder secondaryOwner) {this.secondaryOwner = secondaryOwner;}
    public LocalDate getCreatedDate() {return createdDate;}
    public void setCreatedDate(LocalDate createdDate) {this.createdDate = createdDate;}


}
