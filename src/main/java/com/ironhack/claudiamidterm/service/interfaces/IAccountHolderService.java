package com.ironhack.claudiamidterm.service.interfaces;

import com.ironhack.claudiamidterm.classes.*;
import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;

import java.math.*;
import java.security.*;
import java.util.*;

public interface IAccountHolderService {
    AccountHolder create(AccountHolderDTO accountHolderDTO);

    List<Account> getAllAccounts(CredentialsDTO credentialsDTO, String username);

    Money getAccountBalance(Long id, CredentialsDTO credentials, String name);

    Transference transfer(TransferenceDTO transferenceDTO, Principal principal);
}
