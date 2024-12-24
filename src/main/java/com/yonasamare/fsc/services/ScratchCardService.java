package com.yonasamare.fsc.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yonasamare.fsc.models.ScratchCard;
import com.yonasamare.fsc.models.ScratchCardRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class ScratchCardService {

    public ScratchCardService() {
    }

    public List<ScratchCard> generateScratchcards(List<ScratchCardRequest> scratchCardRequests) {
        List<ScratchCard> scratchCards = new ArrayList<>();
        Random rand = new Random();
        Set<String> generatedNumbers = new HashSet<>(); // Track unique numbers

        for (ScratchCardRequest scratchCardRequest : scratchCardRequests) {
//            log.info("Generating {} scratch cards with denomination {}",
//                    scratchCardRequest.getSize(), scratchCardRequest.getDenomination());

            for (int j = 0; j < scratchCardRequest.getSize(); j++) {
                // Generate a unique 16-digit number
                StringBuilder number;
                do {
                    number = new StringBuilder();
                    for (int i = 0; i < 16; i++) {
                        number.append(rand.nextInt(10));
                        if ((i + 1) % 4 == 0 && i < 15) {
                            number.append("-");
                        }
                    }
                } while (generatedNumbers.contains(number.toString())); // Ensure uniqueness
                generatedNumbers.add(number.toString());

                // Create a new ScratchCard object
                ScratchCard scratchCard = new ScratchCard();
                scratchCard.setScratchCardNumber(number.toString());

                // Set the createdDate using LocalDateTime
                String createdDate = LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                scratchCard.setCreatedDate(createdDate);

                scratchCard.setRedeemed(false);
                scratchCard.setBalance(scratchCardRequest.getDenomination());

                // Add to the list of scratch cards
                scratchCards.add(scratchCard);

                // Reset the StringBuilder to generate a new card number
                number.setLength(0); // IMPORTANT: Prevent appending to the previous number
            }
        }
        // Write to JSON file
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("scratch_cards.json"), scratchCards);
        } catch (IOException e) {
//            log.error("Error writing to JSON file", e);
        }

        return scratchCards;
    }

    public List<Map<String, String>> validateScratchCards(List<String> scratchCardNumbers) {
        // Validate input parameters
        validateInput(scratchCardNumbers);

        List<Map<String, String>> results = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Read scratch cards from the JSON file once
            List<ScratchCard> scratchCards = mapper.readValue(
                    new File("scratch_cards.json"),
                    new TypeReference<>() {
                    }
            );

            // Iterate through the provided scratch card numbers
            for (String number : scratchCardNumbers) {
                boolean isValid = scratchCards.stream()
                        .anyMatch(card -> card.getScratchCardNumber().equals(number));

                // Create a map to tag the number as valid or invalid
                Map<String, String> result = new HashMap<>();
                result.put("scratchCardNumber", number);
                result.put("status", isValid ? "valid" : "invalid");
                results.add(result);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading or processing the JSON file", e);
        }

        return results;
    }

    // Helper method for input validation
    private void validateInput(List<String> scratchCardNumbers) {
        for (String scratchCardNumber : scratchCardNumbers) {
            if (scratchCardNumber.replaceAll("-", "").length() != 16) {
                throw new IllegalArgumentException("Scratch card number must be 16 digits long");
            }
            if (!scratchCardNumber.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}")) {
                throw new IllegalArgumentException("Scratch card number must be in the format XXXX-XXXX-XXXX-XXXX");
            }
        }
    }


}

