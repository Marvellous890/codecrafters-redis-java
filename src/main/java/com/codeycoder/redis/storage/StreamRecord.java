package com.codeycoder.redis.storage;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public record StreamRecord(String id, List<Pair<String, String>> pairs) {
}