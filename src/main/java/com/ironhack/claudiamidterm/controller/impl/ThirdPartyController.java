package com.ironhack.claudiamidterm.controller.impl;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.controller.interfaces.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.service.interfaces.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.security.*;

@RestController
public class ThirdPartyController implements IThirdPartyController {

    @Autowired
    IThirdPartyService thirdPartyService;

    @PostMapping("/new/thirdParty")
    @ResponseStatus(HttpStatus.CREATED)
    public ThirdParty create(@RequestBody ThirdPartyDTO thirdPartyDTO) {
        return thirdPartyService.create(thirdPartyDTO);
    }
    @PatchMapping ("/third-party/send")
    @ResponseStatus(HttpStatus.OK)
    public void sendThirdParty(@RequestHeader(value="key")String key, @RequestBody ThirdPartyMovementDTO thirdPartyMovementDTO,Principal principal) {
        thirdPartyService.send(key,thirdPartyMovementDTO,principal);
    }

    @PatchMapping ("/third-party/receive")
    @ResponseStatus(HttpStatus.OK)
    public void receiveThirdParty(@RequestHeader(value="key")String key, @RequestBody ThirdPartyMovementDTO thirdPartyMovementDTO,Principal principal) {
        thirdPartyService.receive(key, thirdPartyMovementDTO, principal);
    }
}
