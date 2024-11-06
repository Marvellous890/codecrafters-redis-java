package com.codeycoder.redis.storage;

import java.time.Instant;

public record StorageRecord(String value, Instant expiration) {
}