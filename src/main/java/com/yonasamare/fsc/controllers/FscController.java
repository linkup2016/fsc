package com.yonasamare.fsc.controllers;

import com.yonasamare.fsc.models.ScratchCard;
import com.yonasamare.fsc.models.ScratchCardRequest;
import com.yonasamare.fsc.services.ScratchCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/fsc")
public class FscController {

    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(FscController.class);
    @Autowired
    private ScratchCardService scratchCardService;

    @GetMapping(path = "/health")
    public String healthCheck() {
        return "UP";
    }

    @PostMapping(path ="/redeem", consumes = "application/json", produces = "application/json")
    public List<Map<String, String>> redeemScratchCards (@RequestBody List<String> scratchCardNumbers) {
        log.info(" {} scratch card validation requests have been received.", scratchCardNumbers.size());
        return scratchCardService.redeemScratchCards(scratchCardNumbers);
    }

    @PostMapping(path = "/generate", consumes = "application/json", produces = "application/json")
    public List<ScratchCard> generateScratchCards (@RequestBody List<ScratchCardRequest> scratchCardRequest) {
        log.info(" {} scratch card creation requests have been received.", scratchCardRequest.size());
        return scratchCardService.generateScratchcards(scratchCardRequest);
    }

    @PostMapping(path ="/validate", consumes = "application/json", produces = "application/json")
    public List<Map<String, String>> validateScratchCards (@RequestBody List<String> scratchCardNumbers) {
        log.info(" {} scratch card validation requests have been received.", scratchCardNumbers.size());
        return scratchCardService.validateScratchCards(scratchCardNumbers);
    }
}
