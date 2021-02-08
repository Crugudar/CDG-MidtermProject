package com.ironhack.claudiamidterm.controller.DTO;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;

import javax.validation.constraints.*;
import java.time.*;

public class AccountHolderDTO {

    @NotNull(message = "name is required")
    private String name;
    private String username;
    @NotNull(message = "password is required")
    private String password;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @NotNull(message = "dateOfBirth is required")
    private LocalDate dateOfBirth;
    @NotNull(message = "primary address direction is required")
    private String primaryAddressDirection;
    @NotNull(message = "primary address number is required")
    private Integer primaryAddressNumber;
    private String mailingAddressDirection;
    private Integer mailingAddressNumber;

    public AccountHolderDTO() {
    }

    public AccountHolderDTO(@NotNull(message = "name is required") String name, String username, @NotNull(message = "password is required") String password, @NotNull(message = "dateOfBirth is required") LocalDate dateOfBirth, @NotNull(message = "primary address direction is required") String primaryAddressDirection, @NotNull(message = "primary address number is required") Integer primaryAddressNumber) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddressDirection = primaryAddressDirection;
        this.primaryAddressNumber = primaryAddressNumber;
    }

    public AccountHolderDTO(@NotNull(message = "name is required") String name, @NotNull(message = "password is required") String password, @NotNull(message = "dateOfBirth is required") LocalDate dateOfBirth, @NotNull(message = "primary address direction is required") String primaryAddressDirection, @NotNull(message = "primary address number is required") Integer primaryAddressNumber, String mailingAddressDirection, Integer mailingAddressNumber) {
        this.name = name;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddressDirection = primaryAddressDirection;
        this.primaryAddressNumber = primaryAddressNumber;
        this.mailingAddressDirection = mailingAddressDirection;
        this.mailingAddressNumber = mailingAddressNumber;
    }

    public AccountHolderDTO(@NotNull(message = "name is required") String name,@NotNull(message = "password is required") String password, @NotNull(message = "dateOfBirth is required") LocalDate dateOfBirth, @NotNull(message = "primary address direction is required") String primaryAddressDirection,@NotNull(message = "primary address number is required") Integer primaryAddressNumber) {
        this.name = name;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddressDirection = primaryAddressDirection;
        this.primaryAddressNumber = primaryAddressNumber;
    }

    public AccountHolderDTO(@NotNull(message = "name is required") String name, String username,
                            @NotNull(message = "password is required") String password,
                            @NotNull(message = "dateOfBirth is required") LocalDate dateOfBirth,
                            @NotNull(message = "primary address direction is required") String primaryAddressDirection,
                            @NotNull(message = "primary address number is required") Integer primaryAddressNumber,
                            String mailingAddressDirection, Integer mailingAddressNumber) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.primaryAddressDirection = primaryAddressDirection;
        this.primaryAddressNumber = primaryAddressNumber;
        this.mailingAddressDirection = mailingAddressDirection;
        this.mailingAddressNumber = mailingAddressNumber;
    }


    public String getName(){return name;}
    public void setName(String name) {this.name = name;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public LocalDate getDateOfBirth() {return dateOfBirth;}
    public void setDateOfBirth(LocalDate dateOfBirth) {this.dateOfBirth = dateOfBirth;}
    public String getPrimaryAddressDirection() {return primaryAddressDirection;}
    public void setPrimaryAddressDirection(String primaryAddressDirection) {this.primaryAddressDirection = primaryAddressDirection;}
    public Integer getPrimaryAddressNumber() {return primaryAddressNumber;}
    public void setPrimaryAddressNumber(Integer primaryAddressNumber) {this.primaryAddressNumber = primaryAddressNumber;}
    public String getMailingAddressDirection() {return mailingAddressDirection;}
    public void setMailingAddressDirection(String mailingAddressDirection) {this.mailingAddressDirection = mailingAddressDirection;}
    public Integer getMailingAddressNumber() {return mailingAddressNumber;}
    public void setMailingAddressNumber(Integer mailingAddressNumber) {this.mailingAddressNumber = mailingAddressNumber;}

    @Override
    public String toString() {
        return "AccountHolderDTO{" +
                "name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", primaryAddressDirection='" + primaryAddressDirection + '\'' +
                ", primaryAddressNumber=" + primaryAddressNumber +
                ", mailingAddressDirection='" + mailingAddressDirection + '\'' +
                ", mailingAddressNumber=" + mailingAddressNumber +
                '}';
    }
}
