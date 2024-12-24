package com.yonasamare.fsc.models;

public enum Denominations {
    TWENTY_FIVE("$25.00"),
    FIFTY("$50.00"),
    HUNDRED("$100.00"),
    FIVE_HUNDRED("$500.00"),
    THOUSAND("$1,000.00");

    private final String value;

    Denominations(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
