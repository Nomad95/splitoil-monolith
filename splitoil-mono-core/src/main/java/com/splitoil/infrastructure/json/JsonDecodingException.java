package com.splitoil.infrastructure.json;

public class JsonDecodingException extends RuntimeException {
    public JsonDecodingException(Throwable e) {
        super(e);
    }
}
