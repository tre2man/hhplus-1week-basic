package io.hhplus.tdd.exception;

public class OverPointLimitException extends RuntimeException {
    public OverPointLimitException() {
        super("포인트의 최대값을 초과했습니다.");
    }
}
