package com.codeycoder.redis.command;

import com.codeycoder.redis.config.ObjectFactory;
import com.codeycoder.redis.protocol.ValueType;
import com.codeycoder.redis.storage.Storage;
import com.codeycoder.redis.storage.StreamRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Xrange extends AbstractHandler {
    public Xrange(ObjectFactory objectFactory) {
        super(objectFactory);
    }

    @Override
    public byte[] handle(String[] arguments) {
        if (arguments.length != 4) {
            throw new IllegalArgumentException("Expected 4 arguments, got " + arguments.length);
        }

        String streamKey = arguments[1];
        String start = arguments[2];
        // since we are using lexicographical comparison, it's safe to set 'end' to ':' here to support '+' as max end
        String end = arguments[3].equals("+") ? ":" : arguments[3];

        List foundStream =
                Optional.ofNullable(Storage.get(streamKey))
                        .filter(r -> r.valueType() == ValueType.STREAM)
                        .map(r -> (List<StreamRecord>) r.value())
                        .orElseGet(ArrayList::new)
                        .stream()
                        .filter(r -> idIsBetween(r.id(), start, end))
                        .map(StreamRecord::toSerializable)
                        .toList();
        return protocolSerializer().array(foundStream);
    }

    private boolean idIsBetween(String id, String start, String end) {
        return id.compareTo(start) >= 0 && id.compareTo(end) <= 0;
    }
}