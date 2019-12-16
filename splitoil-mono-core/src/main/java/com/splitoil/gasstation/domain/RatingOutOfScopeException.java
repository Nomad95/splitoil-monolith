package com.splitoil.gasstation.domain;

class RatingOutOfScopeException extends RuntimeException {

    RatingOutOfScopeException(final int rating) {
        super(String.format("Rating %d is not allowed", rating));
    }
}
