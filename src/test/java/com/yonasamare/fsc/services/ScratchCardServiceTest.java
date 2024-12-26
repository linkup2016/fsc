package com.yonasamare.fsc.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yonasamare.fsc.models.ScratchCard;
import com.yonasamare.fsc.models.ScratchCardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.junit.jupiter.api.Assertions.*;

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
        result.forEach(e -> System.out.println(e.getScratchCardNumber()));
    }

    @Test
    public void testValidScratchCardNumber() {
        ScratchCardService validator = new ScratchCardService();
        List<String> scratchCardNumbers = Arrays.asList("1234-5678-9012-3456");
        try {
            validator.validateScratchCards(scratchCardNumbers);
            // If no exception is thrown, the test passes
        } catch (IllegalArgumentException e) {
            fail("Valid scratch card number was rejected");
        }
    }

    @Test
    public void testInvalidScratchCardNumber_Length() {
        ScratchCardService validator = new ScratchCardService();
        List<String> scratchCardNumbers = Arrays.asList("1234-5678-9012-345");
        try {
            validator.validateScratchCards(scratchCardNumbers);
            fail("Invalid scratch card number was not rejected");
        } catch (IllegalArgumentException e) {
            assertEquals("Scratch card number must be 16 digits long", e.getMessage());
        }
    }

    @Test
    public void testInvalidScratchCardNumber_Format() {
        ScratchCardService validator = new ScratchCardService();
        List<String> scratchCardNumbers = Arrays.asList("1234567890123456");
        try {
            validator.validateScratchCards(scratchCardNumbers);
            fail("Invalid scratch card number was not rejected");
        } catch (IllegalArgumentException e) {
            assertEquals("Scratch card number must be in the format XXXX-XXXX-XXXX-XXXX", e.getMessage());
        }
    }

    @Test
    public void testMultipleScratchCardNumbers() {
        ScratchCardService validator = new ScratchCardService();
        List<String> scratchCardNumbers = Arrays.asList("1234-5678-9012-3456", "9876-5432-1098-7654");
        try {
            validator.validateScratchCards(scratchCardNumbers);
            // If no exception is thrown, the test passes
        } catch (IllegalArgumentException e) {
            fail("Valid scratch card numbers were rejected");
        }
    }

    @Test
    public void testRedeemScratchCards() throws IOException {
        // Create a list of scratch card numbers
        List<String> scratchCardNumbers = Arrays.asList("1234-5678-9012-3456", "9876-5432-1098-7654");

        // Create a list of ScratchCard objects
        List<ScratchCard> scratchCards = new ArrayList<>();
        ScratchCard card1 = new ScratchCard();
        card1.setScratchCardNumber("1234-5678-9012-3456");
        card1.setRedeemed(false);
        scratchCards.add(card1);

        ScratchCard card2 = new ScratchCard();
        card2.setScratchCardNumber("9876-5432-1098-7654");
        card2.setRedeemed(true);
        scratchCards.add(card2);

        // Write the scratch cards to a JSON file
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("test_scratch_cards.json"), scratchCards);

        // Call the redeemScratchCards method
        ScratchCardService service = new ScratchCardService();
        List<Map<String, String>> results = service.redeemScratchCards(scratchCardNumbers);

        // Verify the results
        assertEquals(2, results.size());

        Map<String, String> result1 = results.get(0);
//        assertEquals("1234-5678-9012-3456", result1.get("scratchCardNumber"));
//        assertEquals("success", result1.get("status"));
//        assertEquals("Scratch card redeemed successfully", result1.get("message"));
//
//        Map<String, String> result2 = results.get(1);
//        assertEquals("9876-5432-1098-7654", result2.get("scratchCardNumber"));
//        assertEquals("already_redeemed", result2.get("status"));
//        assertEquals("Scratch card has already been redeemed", result2.get("message"));

        // Clean up
        new File("test_scratch_cards.json").delete();
    }
}