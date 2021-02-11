package com.ironhack.claudiamidterm.controller.interfaces;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;

public interface IAccountController {
    public Account update(String id, String newBalance, CredentialsDTO credentials, Principal principal);
}
