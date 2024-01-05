package org.epam.gymservice.service.security;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    @Value("${my-props.jwt.max-attempts}")
    private int MAX_ATTEMPT;
    @Value("${my-props.jwt.cold-pause-min}")
    private long COLD_TIME;
    private final Cache<String, Integer> attemptsCache = Caffeine.newBuilder()
            .expireAfterAccess(COLD_TIME, TimeUnit.MINUTES)
            .maximumSize(100)
            .build();

    public void loginFailed(String key) {
        int attempts = attemptsCache.get(key, k -> 0);
        attempts++;
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        Integer attempts = attemptsCache.getIfPresent(key);
        return attempts != null && attempts >= MAX_ATTEMPT;
    }

}
