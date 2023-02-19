package com.sstankiewicz.phonebooking.configuration;

import com.aafanasev.fonoapi.retrofit.FonoApiFactory;
import com.aafanasev.fonoapi.retrofit.FonoApiService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FonoApiConfiguration {
    @Bean
    public FonoApiService fonoApiService() {
        return new FonoApiFactory().create();
    }
}
