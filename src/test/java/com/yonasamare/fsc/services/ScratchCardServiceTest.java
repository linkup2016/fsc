package com.yonasamare.fsc.services;

import com.yonasamare.fsc.models.ScratchCard;
import com.yonasamare.fsc.models.ScratchCardRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScratchCardServiceTest {
    private DynamoDbClient dynamoDbClient;
    private ScratchCardService classUnderTest;
    private List<ScratchCardRequest> scratchCardRequests;

   @BeforeEach
    void setUp() {
        // Mock the DynamoDbClient for testing
        dynamoDbClient = mock(DynamoDbClient.class);

        // Initialize the service with the mocked client
        classUnderTest = new ScratchCardService(dynamoDbClient);

        // Prepare some scratch card requests for testing
        scratchCardRequests = new ArrayList<>();

        ScratchCardRequest scratchCardRequest1 = new ScratchCardRequest();
        scratchCardRequest1.setDenomination(25.00);
        scratchCardRequest1.setSize(10);

        ScratchCardRequest scratchCardRequest2 = new ScratchCardRequest();
        scratchCardRequest2.setDenomination(50.00);
        scratchCardRequest2.setSize(5);

        scratchCardRequests.add(scratchCardRequest1);
        scratchCardRequests.add(scratchCardRequest2);
    }

    @Test
    void generateScratchcardNumber() {
        // Call the method under test
        List<ScratchCard> result = classUnderTest.generateScratchcards(scratchCardRequests);

        // Verify the results
        assertNotNull(result);
        assertEquals(15, result.size()); // 10 + 5 cards requested
        result.forEach(card -> {
            assertNotNull(card.getScratchCardNumber());
            System.out.println(card.getScratchCardNumber());
        });
    }

/*    @Test
    void testValidScratchCardNumber() {
        List<String> scratchCardNumbers = Arrays.asList("1234-5678-9012-3456");

        // Call the validateScratchCards method
        assertDoesNotThrow(() -> classUnderTest.validateScratchCards(scratchCardNumbers));
    }*/

    @Test
    void testInvalidScratchCardNumber_Length() {
        List<String> scratchCardNumbers = Arrays.asList("1234-5678-9012-345");

        // Call the validateScratchCards method and expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            classUnderTest.validateScratchCards(scratchCardNumbers);
        });

        assertEquals("Scratch card number must be 16 digits long", exception.getMessage());
    }

    @Test
    void testInvalidScratchCardNumber_Format() {
        List<String> scratchCardNumbers = Arrays.asList("1234567890123456");

        // Call the validateScratchCards method and expect an exception
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            classUnderTest.validateScratchCards(scratchCardNumbers);
        });

        assertEquals("Scratch card number must be in the format XXXX-XXXX-XXXX-XXXX", exception.getMessage());
    }

/*    @Test
    void testMultipleScratchCardNumbers() {
        List<String> scratchCardNumbers = Arrays.asList("1234-5678-9012-3456", "9876-5432-1098-7654");

        // Call the validateScratchCards method
        assertDoesNotThrow(() -> classUnderTest.validateScratchCards(scratchCardNumbers));
    }

    @Test
    void testRedeemScratchCards() {
        // Create a list of scratch card numbers
        List<String> scratchCardNumbers = Arrays.asList("1234-5678-9012-3456", "9876-5432-1098-7654");

        // Call the redeemScratchCards method
        List<Map<String, String>> results = classUnderTest.redeemScratchCards(scratchCardNumbers);

        // Verify the results
        assertEquals(2, results.size());

        Map<String, String> result1 = results.get(0);
        assertEquals("1234-5678-9012-3456", result1.get("scratchCardNumber"));
        assertEquals("success", result1.get("status"));
        assertEquals("Scratch card redeemed successfully", result1.get("message"));

        Map<String, String> result2 = results.get(1);
        assertEquals("9876-5432-1098-7654", result2.get("scratchCardNumber"));
        assertEquals("success", result2.get("status"));
    }*/
}
