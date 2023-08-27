package com.example.bucket4jratelimitingsample.service;

import io.github.bucket4j.Bucket;
/*
* We create a rate limiter service
* that has a function to return a token bucket based
* on the API key that is provided.*/
public interface RateLimiterService {
    Bucket resolveBucket(String apiKey);
}
