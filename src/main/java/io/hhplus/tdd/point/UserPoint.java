package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.InsufficientPointException;
import io.hhplus.tdd.exception.InvalidPointException;
import io.hhplus.tdd.exception.OverPointLimitException;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {
    private static final long MAX_POINT = 1_000_000_000L;
    private static final long MIN_POINT = 0L;

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPoint {
        if (point < MIN_POINT) {
            throw new InvalidPointException();
        }
        if (point > MAX_POINT) {
            throw new OverPointLimitException();
        }
    }


    public UserPoint add(long amount) {
        if (amount < 0) {
            throw new InvalidPointException();
        }
        long newPoint = this.point + amount;
        if (newPoint > MAX_POINT) {
            throw new OverPointLimitException();
        }
        return new UserPoint(this.id, newPoint, System.currentTimeMillis());
    }

    public UserPoint subtract(long amount) {
        if (amount < 0) {
            throw new InvalidPointException();
        }
        long newPoint = this.point - amount;
        if (newPoint < MIN_POINT) {
            throw new InsufficientPointException();
        }
        return new UserPoint(this.id, newPoint, System.currentTimeMillis());
    }
}
