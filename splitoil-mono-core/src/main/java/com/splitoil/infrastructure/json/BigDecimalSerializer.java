package com.splitoil.infrastructure.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalSerializer extends NumberSerializer {
    private static final BigDecimalSerializer instance = new BigDecimalSerializer();

    private BigDecimalSerializer() {
        super(BigDecimal.class);
    }

    public static BigDecimalSerializer getInstance() {
        return instance;
    }

    public void serialize(Number value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        BigDecimal number = (BigDecimal)value;
        if (number.scale() > 0) {
            super.serialize(value, jgen, provider);
        } else {
            BigDecimal scaledValue = number.setScale(1, RoundingMode.UNNECESSARY);
            super.serialize(scaledValue, jgen, provider);
        }
    }
}
