package com.yonasamare.fsc.controllers;

import com.yonasamare.fsc.models.ScratchCard;
import com.yonasamare.fsc.models.ScratchCardRequest;
import com.yonasamare.fsc.services.ScratchCardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/fsc")
public class FscController {

    @Autowired
    private ScratchCardService scratchCardService;

    @GetMapping
    public String healthCheck() {
        return "UP";
    }

    @PostMapping(path ="/validate", produces = "application/json")
    public List<String> validateScratchCards (@RequestParam("scratchCards") List<String> scratchCards) {

        return null;
    }

    @PostMapping(path = "/generate", consumes = "application/json", produces = "application/json")
    public List<ScratchCard> generateScratchCards (@RequestBody List<ScratchCardRequest> scratchCardRequest) {
//        scratchCardRequest.forEach(request -> log.info(" {} scratch card creation requests have been received.", scratchCardRequest.size()));
        return scratchCardService.generateScratchcards(scratchCardRequest);
    }
}
