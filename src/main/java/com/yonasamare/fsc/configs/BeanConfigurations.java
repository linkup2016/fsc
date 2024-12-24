package com.yonasamare.fsc.configs;

import com.yonasamare.fsc.models.ScratchCardRequest;
import com.yonasamare.fsc.services.ScratchCardService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfigurations {


    @Bean
    public ScratchCardRequest scratchCardRequest() {
        return new ScratchCardRequest();
    }

    @Bean
    public ScratchCardService scratchCardService() {
        return new ScratchCardService();
    }
}
