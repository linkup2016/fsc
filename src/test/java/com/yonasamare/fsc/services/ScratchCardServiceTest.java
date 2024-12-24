package com.yonasamare.fsc.services;

import com.yonasamare.fsc.models.ScratchCard;
import com.yonasamare.fsc.models.ScratchCardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ScratchCardServiceTest {
    private ScratchCardService classUnderTest;
    private List<ScratchCardRequest> scratchCardRequests;

    @BeforeEach
    void setUp() {
        classUnderTest = new ScratchCardService();

        scratchCardRequests = new ArrayList<>();

        ScratchCardRequest scratchCardRequest = new ScratchCardRequest();
        scratchCardRequest.setDenomination(25.00);
        scratchCardRequest.setSize(10);

        ScratchCardRequest scratchCardRequest2 = new ScratchCardRequest();
        scratchCardRequest2.setDenomination(25.00);
        scratchCardRequest2.setSize(10);

        scratchCardRequests.add(scratchCardRequest);
        scratchCardRequests.add(scratchCardRequest2);

    }

    @Test
    void generateScratchcardNumber() {
        List<ScratchCard> result = classUnderTest.generateScratchcards(scratchCardRequests);
        result.forEach(e-> System.out.println(e.getScratchCardNumber()));
    }
}