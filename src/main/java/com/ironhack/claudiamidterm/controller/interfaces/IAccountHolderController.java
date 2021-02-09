package com.ironhack.claudiamidterm.controller.interfaces;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;

import java.math.*;
import java.security.*;
import java.util.*;

public interface IAccountHolderController {
    AccountHolder create(AccountHolderDTO accountHolderDTO);
    Money getAccountBalance(String id, CredentialsDTO credentials, Principal principal);
}
