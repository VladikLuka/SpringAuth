package com.epam.springauth.service;

import com.epam.springauth.model.CachedValue;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    public static final int MAX_ATTEMPT = 3;
    public static final int BLOCK_DURATION_SEC = 40;
    private LoadingCache<String, CachedValue> attemptsCache;

    public LoginAttemptService(){
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(BLOCK_DURATION_SEC, TimeUnit.SECONDS)
                .build(new CacheLoader<>() {
                    @Override
                    public CachedValue load(final String key){
                        return new CachedValue(0, LocalDateTime.now());
                    }
                });
    }

    public void loginFailed(final String key){
        CachedValue cachedValue = new CachedValue();
        try {
            cachedValue = attemptsCache.get(key);
            cachedValue.setAttempts(cachedValue.getAttempts() + 1);
        } catch (final ExecutionException e) {
            cachedValue.setAttempts(0);
        }
        if (isBlocked(key) && cachedValue.getBlockedTimeStamp() == null){
            cachedValue.setBlockedTimeStamp(LocalDateTime.now());
        }
        attemptsCache.put(key, cachedValue);
    }

    public boolean isBlocked(final String key){
        try{
            return attemptsCache.get(key).getAttempts() >= MAX_ATTEMPT;
        } catch (final ExecutionException e){
            return false;
        }
    }

    public CachedValue getCachedValue(final String key) {
        return attemptsCache.getUnchecked(key);
    }
}
