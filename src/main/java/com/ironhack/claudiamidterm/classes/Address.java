package com.ironhack.claudiamidterm.classes;

import javax.persistence.*;

@Embeddable
public class Address {
    private String direction;
    private int number;


    public Address(){}

    public Address(String direction, int number) {
        this.direction = direction;
        this.number = number;
    }


    public String getDirection() {return direction;}
    public void setDirection(String direction) {this.direction = direction;}
    public int getNumber() {return number;}
    public void setNumber(int number) {this.number = number;}
}
