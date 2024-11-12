package com.codeycoder.redis.storage;

import com.codeycoder.redis.protocol.ValueType;

import java.time.Instant;

public record StorageRecord(ValueType valueType, Object value, Instant expiration) {
}