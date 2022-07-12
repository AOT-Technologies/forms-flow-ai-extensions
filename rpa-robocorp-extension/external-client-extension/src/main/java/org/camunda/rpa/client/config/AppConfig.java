package org.camunda.rpa.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.rpa.client.data.repository.RepoFinder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${spring.webclient.max-buffer-size}")
    private Integer maxBufferSize;

    @Bean
    public WebClient getWebClient(){
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(maxBufferSize * 1024 * 1024))
                        .build())
                .build();
    }

    @Bean
    public RepoFinder getRepoFinder(){
        return new RepoFinder();
    }

    @Bean
    @Qualifier("bpmObjectMapper")
    public ObjectMapper bpmObjectMapper(){
        return new ObjectMapper();
    }
}
