package com.sstankiewicz.phonebooking.services;

import com.aafanasev.fonoapi.retrofit.FonoApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
public class PhoneSpecificationService {
    public record PhoneSpecification(String technology, String _2gBands, String _3gBands, String _4gBands) {
    }

    private final FonoApiService fonoApiService;
    private final String fonoApiToken;

    private static final PhoneSpecification UNKNOWN_SPEC = new PhoneSpecification("UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN");

    public PhoneSpecificationService(FonoApiService fonoApiService, @Value("${fono-api.token}") String fonoApiToken) {
        this.fonoApiService = fonoApiService;
        this.fonoApiToken = fonoApiToken;
    }

    public Optional<PhoneSpecification> getSpecification(String brand, String model) {
        try {
            var responseBody = fonoApiService.getDevice(fonoApiToken, model, brand, 1).execute().body();
            if (responseBody == null) {
                log.warn("Couldn't find device specification in Fonoapi for {} {}", brand, model);
                return Optional.of(UNKNOWN_SPEC);
            }
            return responseBody.stream().findFirst().map(deviceEntity -> new PhoneSpecification(deviceEntity.getTechnology(), deviceEntity.get_2g_bands(), deviceEntity.get_3g_bands(), deviceEntity.get_4g_bands()));
        } catch (IOException e) {
            log.error("Error fetching device specification from Fonoapi for {} {}", brand, model, e);
            return Optional.of(UNKNOWN_SPEC);
        }
    }
}
