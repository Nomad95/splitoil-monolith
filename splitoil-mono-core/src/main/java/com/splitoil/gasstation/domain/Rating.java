package com.splitoil.gasstation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
class Rating {

    private static final Set<Integer> allowedRatings;

    static {
        final HashSet<Integer> ints = new HashSet<>();
        ints.add(1);
        ints.add(2);
        ints.add(3);
        ints.add(4);
        ints.add(5);
        allowedRatings = ints;
    }

    private int rating;

    static Rating of(final int rating) {
        if (!allowedRatings.contains(rating)) {
            throw new RatingOutOfScopeException(rating);
        }

        return new Rating(rating);
    }
}
