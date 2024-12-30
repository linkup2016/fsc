package com.yonasamare.fsc.services;

import com.yonasamare.fsc.models.ScratchCard;
import com.yonasamare.fsc.models.ScratchCardRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class ScratchCardService {

    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(ScratchCardService.class);
    private static final String TABLE_NAME = "ScratchCardTable";
    private final DynamoDbClient dynamoDbClient;

    @Autowired
    public ScratchCardService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }



    public List<ScratchCard> generateScratchcards(List<ScratchCardRequest> scratchCardRequests) {
        List<ScratchCard> scratchCards = new ArrayList<>();
        Random rand = new Random();
        Set<String> generatedNumbers = new HashSet<>();

        for (ScratchCardRequest scratchCardRequest : scratchCardRequests) {
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
                } while (generatedNumbers.contains(number.toString()));
                generatedNumbers.add(number.toString());
                // Create a new ScratchCard object
                ScratchCard scratchCard = new ScratchCard();
                scratchCard.setScratchCardNumber(number.toString());
                scratchCard.setCreatedDate(LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                scratchCard.setRedeemed(false);
                scratchCard.setBalance(scratchCardRequest.getDenomination());
                scratchCard.setPin(generatePin());
                log.info("Generating a pin for scratch card number{} ", scratchCard.getScratchCardNumber());
                log.debug("Generated PIN: {}", scratchCard.getPin());

                // Add to DynamoDB
                log.info("Saving scratch card {} to DynamoDB.", scratchCard);
                saveScratchCardToDynamoDB(scratchCard);
                scratchCards.add(scratchCard);
            }
        }

        return scratchCards;
    }

    public List<Map<String, String>> validateScratchCards(List<String> scratchCardNumbers) {
        validateInput(scratchCardNumbers);
        List<Map<String, String>> results = new ArrayList<>();

        for (String number : scratchCardNumbers) {
            Map<String, String> result = new HashMap<>();
            result.put("scratchCardNumber", number);

            ScratchCard card = getScratchCardFromDynamoDB(number);
            if (card != null) {
                result.put("status", "valid");
            } else {
                result.put("status", "invalid");
            }

            results.add(result);
        }

        return results;
    }

    public List<Map<String, String>> redeemScratchCards(List<String> scratchCardNumbers) {
        validateInput(scratchCardNumbers);
        List<Map<String, String>> results = new ArrayList<>();

        for (String scratchCardNumber : scratchCardNumbers) {
            Map<String, String> result = new HashMap<>();
            result.put("scratchCardNumber", scratchCardNumber);

            ScratchCard card = getScratchCardFromDynamoDB(scratchCardNumber);
            if (card == null) {
                result.put("status", "invalid");
                result.put("message", "Scratch card does not exist");
            } else if (card.isRedeemed()) {
                result.put("status", "already_redeemed");
                result.put("message", "Scratch card has already been redeemed");
            } else {
                card.setRedeemed(true);
                card.setRedeemedDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                updateScratchCardInDynamoDB(card);
                result.put("status", "success");
                result.put("message", "Scratch card redeemed successfully");
            }

            results.add(result);
        }

        return results;
    }

    private void saveScratchCardToDynamoDB(ScratchCard scratchCard) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("scratchCardNumber", AttributeValue.builder().s(scratchCard.getScratchCardNumber()).build());
        item.put("pin", AttributeValue.builder().s(scratchCard.getPin()).build());
        item.put("balance", AttributeValue.builder().n(String.valueOf(scratchCard.getBalance())).build());
        item.put("createdDate", AttributeValue.builder().s(scratchCard.getCreatedDate()).build());
        item.put("redeemed", AttributeValue.builder().bool(scratchCard.isRedeemed()).build());
        if (scratchCard.getRedeemedDate() != null) {
            item.put("redeemedDate", AttributeValue.builder().s(scratchCard.getRedeemedDate()).build());
        }
        log.info("Saving scratch card {} to DynamoDB.", scratchCard.getScratchCardNumber());
        log.debug("Item: {}", item);
        dynamoDbClient.putItem(PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(item)
                .build());
    }

    private ScratchCard getScratchCardFromDynamoDB(String scratchCardNumber) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("scratchCardNumber", AttributeValue.builder().s(scratchCardNumber).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .build();

        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();
        if (item == null || item.isEmpty()) {
            return null;
        }

        ScratchCard scratchCard = new ScratchCard();
        scratchCard.setScratchCardNumber(item.get("scratchCardNumber").s());
        scratchCard.setPin(item.get("pin").s());
        scratchCard.setBalance(Double.parseDouble(item.get("balance").n()));
        scratchCard.setCreatedDate(item.get("createdDate").s());
        scratchCard.setRedeemed(item.get("redeemed").bool());
        if (item.containsKey("redeemedDate")) {
            scratchCard.setRedeemedDate(item.get("redeemedDate").s());
        }

        return scratchCard;
    }

    private void updateScratchCardInDynamoDB(ScratchCard scratchCard) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("scratchCardNumber", AttributeValue.builder().s(scratchCard.getScratchCardNumber()).build());

        Map<String, AttributeValueUpdate> updates = new HashMap<>();
        updates.put("redeemed", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().bool(scratchCard.isRedeemed()).build())
                .action(AttributeAction.PUT)
                .build());
        if (scratchCard.getRedeemedDate() != null) {
            updates.put("redeemedDate", AttributeValueUpdate.builder()
                    .value(AttributeValue.builder().s(scratchCard.getRedeemedDate()).build())
                    .action(AttributeAction.PUT)
                    .build());
        }

        dynamoDbClient.updateItem(UpdateItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(key)
                .attributeUpdates(updates)
                .build());
    }

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

    private static String generatePin() {
        SecureRandom random = new SecureRandom();
        StringBuilder pin = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            pin.append(random.nextInt(10));
        }
        return pin.toString();
    }
}
