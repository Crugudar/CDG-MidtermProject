package com.ironhack.claudiamidterm.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import com.ironhack.claudiamidterm.classes.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.*;
import java.util.*;
import java.util.stream.*;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class AccountHolder extends User{

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate dateOfBirth;
    @Embedded
    @AttributeOverrides(value ={
            @AttributeOverride(name = "direction", column = @Column(name = "primary_address_direction")),
            @AttributeOverride(name = "number", column = @Column(name = "primary_address_number"))
    })
    @NotNull
    private Address primaryAddress;
    @Embedded
    @AttributeOverrides(value ={
            @AttributeOverride(name = "direction", column = @Column(name = "mailing_address_direction")),
            @AttributeOverride(name = "number", column = @Column(name = "mailing_address_number"))
    })
    private Address mailingAddress;

    @OneToMany(mappedBy = "primaryOwner")
    @JsonIgnore
    private List<Account> primaryAccounts;
    @OneToMany(mappedBy = "secondaryOwner")
    @JsonIgnore
    private List<Account> secondaryAccounts;

    @Transient
    private List <Account> allAccounts = new ArrayList<>();


    public AccountHolder() {}


    public AccountHolder(String name, String username, String password, LocalDate dateOfBirth, @NotNull Address primaryAddress, Address mailingAddress) {
        super(name, username, password);
        this.dateOfBirth = dateOfBirth;
        this.primaryAddress = primaryAddress;
        this.mailingAddress = mailingAddress;
        this.primaryAccounts = primaryAccounts;
        this.secondaryAccounts = secondaryAccounts;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public Address getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Address mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public List<Account> getPrimaryAccounts() {
        return primaryAccounts;
    }

    public void setPrimaryAccounts(List<Account> primaryAccounts) {
        this.primaryAccounts = primaryAccounts;
    }

    public List<Account> getSecondaryAccounts() {
        return secondaryAccounts;
    }

    public void setSecondaryAccounts(List<Account> secondaryAccounts) {
        this.secondaryAccounts = secondaryAccounts;
    }
}
