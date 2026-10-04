package com.ridelink.drivervehicle.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP client used by the Driver &amp; Vehicle Service to communicate with the
 * Account Service for interservice validation (e.g. verifying that a userId
 * exists before creating a driver profile for it).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AccountServiceClient {

    private final RestTemplate restTemplate;

    @Value("${account.service.url}")
    private String accountServiceUrl;

    /**
     * Checks whether the given user exists in the Account Service.
     * Fails "open" (returns true) if the Account Service is unreachable so that
     * this microservice does not become fully coupled to Account Service uptime,
     * while still rejecting requests for users that are confirmed not to exist.
     */
    public boolean userExists(String userId) {
        try {
            restTemplate.getForEntity(accountServiceUrl + "/api/users/" + userId, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (HttpClientErrorException e) {
            log.warn("Account Service returned an error while validating userId {}: {}", userId, e.getStatusCode());
            return true;
        } catch (ResourceAccessException e) {
            log.warn("Account Service is unreachable while validating userId {}: {}", userId, e.getMessage());
            return true;
        }
    }
}
