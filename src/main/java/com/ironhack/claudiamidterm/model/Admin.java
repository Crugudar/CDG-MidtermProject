package com.ironhack.claudiamidterm.model;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name="id")
public class Admin extends User{

    public Admin() {}

    public Admin(String name, String username, String password) {
        super(name, username, password);
    }

}
