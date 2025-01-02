package org.supportamhara.fsc.models;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScratchCardRequest {

    @Min(0)
    private double denomination;

    @Min(1)
    private int size;

    public ScratchCardRequest() {}

    public double getDenomination() {
        return denomination;
    }

    public void setDenomination(double denomination) {
        this.denomination = denomination;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}

