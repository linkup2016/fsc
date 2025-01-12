package org.supportamhara.fsc.services;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.supportamhara.fsc.models.ApiResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MerriamDictionaryService {

    private final WebClient webClient;

    public MerriamDictionaryService() {
        this.webClient = WebClient.builder().baseUrl("https://www.dictionaryapi.com/api/v3/references/sd2/json").clientConnector(new ReactorClientHttpConnector()).build();
    }

    public Mono<List<ApiResponse>> getDefinition(String word, String apiKey) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/{word}")
                            .queryParam("key", apiKey)
                            .build(word))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ApiResponse>>() {});
        } catch (WebClientResponseException e) {
            // Handle HTTP errors
            System.err.println("Error: " + e.getMessage());
            return null;
        } catch (Exception e) {
            // Handle other exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            return null;
        }
    }
}
