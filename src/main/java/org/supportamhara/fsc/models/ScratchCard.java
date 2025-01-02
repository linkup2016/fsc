package org.supportamhara.fsc.models;

import lombok.Data;

@Data
public class ScratchCard {
    private long id;
    private String scratchCardNumber;
    private String createdDate;
    private String redeemedDate;
    private boolean isRedeemed;
    private double balance;
    private String pin;
}
