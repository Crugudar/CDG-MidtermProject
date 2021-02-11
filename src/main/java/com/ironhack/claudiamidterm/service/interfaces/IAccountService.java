package com.ironhack.claudiamidterm.service.interfaces;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;
import org.springframework.security.core.*;

public interface IAccountService {

    Account updateBalance(long id, String balance, CredentialsDTO credentials);
    // transfer(TransferenceDTO newTransference, Authentication authentication);
}
