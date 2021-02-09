package com.ironhack.claudiamidterm.controller.DTO;

import java.math.*;

public class TransferenceDTO {


    private Long originId;
    private String receiverName;
    private Long destinationId;
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
