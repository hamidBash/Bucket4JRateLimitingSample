package com.example.bucket4jratelimitingsample.AppConfig;

import com.example.bucket4jratelimitingsample.service.RateLimiterService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
* This class will be used to override the "preHandle" method
* which is used to handle all the incoming API requests to the spring-boot application.
* We’ll autowire the RateLimiterService to create and assign a token bucket
* for every valid API key sent in the requests.
* Using a ConsumptionProbe instance, that checks how many tokens are left,
* we can inform the client about the status of the API requests that remain
* and if the limit is exhausted, how long it would take to replenish the token bucket.
* If we have enough tokens left for the API key, we let the request pass
* else we return a 429 HTTP code to the client.*/
@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getHeader("api-key");
        if (apiKey == null || apiKey.isEmpty()) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Missing Header: api-key");
            return false;
        }
        Bucket bucket = rateLimiterService.resolveBucket(apiKey);
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if(probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        } else {
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                    "You have exhausted your API Request Quota");
            return false;
        }
    }
}
