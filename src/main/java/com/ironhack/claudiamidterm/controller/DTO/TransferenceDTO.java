package com.ironhack.claudiamidterm.controller.DTO;

import javax.validation.constraints.*;
import java.math.*;

public class TransferenceDTO {

    @NotNull
    @NotEmpty
    private Long originId;
    @NotNull
    @NotEmpty
    private String receiverName;
    @NotNull
    @NotEmpty
    private Long destinationId;
    @NotNull
    @NotEmpty
    @Digits(integer = 6, fraction = 2)
    private BigDecimal amount;

    public TransferenceDTO() {
    }

    public TransferenceDTO(Long originId, String receiverName, Long destinationId, BigDecimal amount) {
        this.originId = originId;
        this.receiverName = receiverName;
        this.destinationId = destinationId;
        this.amount = amount;
    }

    public Long getOriginId() {return originId;}
    public void setOriginId(Long originId) {this.originId = originId;}
    public String getReceiverName() {return receiverName;}
    public void setReceiverName(String receiverName) {this.receiverName = receiverName;}
    public Long getDestinationId() {return destinationId;}
    public void setDestinationId(Long destinationId) {this.destinationId = destinationId;}
    public BigDecimal getAmount() {return amount;}
    public void setAmount(BigDecimal amount) {this.amount = amount;}
}
