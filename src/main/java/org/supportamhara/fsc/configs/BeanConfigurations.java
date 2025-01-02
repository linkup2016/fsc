package org.supportamhara.fsc.configs;

import org.supportamhara.fsc.models.ScratchCardRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
public class BeanConfigurations {
    @Value("${spring.aws.dynamodb.endpoint}")
    private String endpoint;
    @Value("${accessKeyId}")
    private String accessKeyId;

    @Value("${secretAccessKey}")
    private String secretAccessKey;
    @Value("${spring.aws.dynamodb.region}")
    private String region;

    @Bean
    @Profile("!local")
    public DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(region)) // Replace with your region
                .build();
    }

    @Bean
    @Profile("local")
    public DynamoDbClient dynamoDbClientLocal() {
        return DynamoDbClient.builder()
                .endpointOverride(URI.create(endpoint)) // DynamoDB Local endpoint
                .region(Region.of(region)) // Region can be anything for local
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey))) // Dummy credentials
                .build();
    }

    @Bean
    public ScratchCardRequest scratchCardRequest() {
        return new ScratchCardRequest();
    }
}
