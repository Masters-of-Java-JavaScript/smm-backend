package ru.smmassistant.smmbackend.config.web;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import ru.smmassistant.smmbackend.service.client.VkClient;

@Configuration
public class WebConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .build();
    }

    @Bean
    public VkClient vkClient(final WebClient webClient, final ConfigurableBeanFactory configurableBeanFactory) {
        WebClientAdapter webClientAdapter = WebClientAdapter.forClient(webClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder(webClientAdapter)
            .embeddedValueResolver(configurableBeanFactory::resolveEmbeddedValue)
            .build();

        return httpServiceProxyFactory.createClient(VkClient.class);
    }
}
