package com.splitoil.infrastructure.json;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.math.BigDecimal;

public class NumberSerializeModule extends SimpleModule {
    public static final NumberSerializeModule instance = new NumberSerializeModule();

    private NumberSerializeModule() {
        this.addSerializer(BigDecimal.class, BigDecimalSerializer.getInstance());
    }

    public static NumberSerializeModule getInstance() {
        return instance;
    }
}
