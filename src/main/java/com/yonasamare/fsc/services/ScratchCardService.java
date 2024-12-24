package com.yonasamare.fsc.services;

import com.yonasamare.fsc.models.ScratchCard;
import com.yonasamare.fsc.models.ScratchCardRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class ScratchCardService {

    public ScratchCardService() {}

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

        return scratchCards;
    }
}

