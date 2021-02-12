package com.ironhack.claudiamidterm.service.impl;

import com.ironhack.claudiamidterm.controller.DTO.*;
import com.ironhack.claudiamidterm.model.*;
import com.ironhack.claudiamidterm.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.math.*;
import java.util.*;


@Service
public class FraudChecker {

    @Autowired
    TransferenceRepository transferenceRepository;

    public boolean moreThanPercent(TransferenceDTO transferenceDTO){

        BigDecimal sumLastDayAmounts = transferenceRepository.sumLastDayTransferences(transferenceDTO.getOriginId());
        BigDecimal maxOfTransferenceinOneDay = transferenceRepository.maxOfTransferenceinOneDay(transferenceDTO.getOriginId());

        boolean result;

        if(sumLastDayAmounts==null){
            sumLastDayAmounts=new BigDecimal("0");
        }

        if (maxOfTransferenceinOneDay.compareTo(BigDecimal.ZERO) == 0 ||
                maxOfTransferenceinOneDay.multiply(new BigDecimal("1.5")).compareTo(sumLastDayAmounts.add(transferenceDTO.getAmount())) > 0 ) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean inLessThanOneSecond(TransferenceDTO transferenceDTO){
        boolean result;

        List<Transference> transferences = transferenceRepository.lastSecondTransferences(transferenceDTO.getOriginId());
        if(transferences.size() != 0 ){
            result = false;
        }else{
            result = true;
        }

        return result;
    }
}
