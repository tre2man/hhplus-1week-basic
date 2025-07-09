package io.hhplus.tdd.exception;
public class InvalidPointException extends RuntimeException {
    public InvalidPointException() {
        super("포인트는 0 이상의 값이어야 합니다.");
    }
}
