package com.ironhack.claudiamidterm.service.interfaces;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;

import java.security.*;

public interface IThirdPartyService {
    ThirdParty create(ThirdPartyDTO thirdPartyDTO);

    void send(String key, ThirdPartyMovementDTO thirdPartyMovementDTO , Principal principal );
    void receive(String key, ThirdPartyMovementDTO thirdPartyMovementDTO , Principal principal );
}
