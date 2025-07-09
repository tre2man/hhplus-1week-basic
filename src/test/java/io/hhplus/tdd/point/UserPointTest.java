package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.InsufficientPointException;
import io.hhplus.tdd.exception.OverPointLimitException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 해당 테스트는 경계값(정책)을 기준으로 경계값, 미만/초과 값을 테스트로 진행했습니다.
 */
class UserPointTest {
    private static final long MIN_POINT = 0L;
    private static final long MAX_POINT = 1_000_000_000L;

    /**
     * 경계값 내부 포인트 객체 생성 성공
     */
    @ParameterizedTest
    @ValueSource(longs = {MIN_POINT, 1L,999_999_999L, MAX_POINT})
    void 경계값_포인트_객체_생성_성공(long point) {
        long id = 1L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        assertEquals(point, userPoint.point());
    }

    /**
     * 경계값 벗어난 음수 포인트 객체 생성 실패
     */
    @ParameterizedTest
    @ValueSource(longs = {-1L, Long.MIN_VALUE})
    void 경계값_벗어난_음수_포인트_객체_생성_실패(long point) {
        long id = 1L;

        assertThrows(
                InsufficientPointException.class,
                () -> new UserPoint(id, point, System.currentTimeMillis())
        );
    }

    /**
     * 경계값 벗어난 양수 포인트 객체 생성 실패
     * @param point
     */
    @ParameterizedTest
    @ValueSource(longs = {1_000_000_001L, Long.MAX_VALUE})
    void 경계값_벗어난_양수_포인트_객체_생성_실패(long point) {
        long id = 1L;

        assertThrows(
                OverPointLimitException.class,
                () -> new UserPoint(id, point, System.currentTimeMillis())
        );
    }

    /**
     * 추가하려는 포인트가 음수인 경우 예외 발생
     */
    @Test
    void 추가_포인트_음수_예외() {
        long id = 1L;
        long point = 1L;
        long addAmount = -1L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        assertThrows(
                InsufficientPointException.class,
                () -> userPoint.add(addAmount)
        );
    }

    /**
     * 감소하려는 포인트가 음수인 경우 예외 발생
     */
    @Test
    void 감소_포인트_음수_예외() {
        long id = 1L;
        long point = 100L;
        long subtractAmount = -1L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        assertThrows(
                InsufficientPointException.class,
                () -> userPoint.subtract(subtractAmount)
        );
    }

    /**
     * 감소 이후 포인트가 0 미만이 되는 경우 실패
     */
    @Test
    void 감소_포인트_0미만_예외() {
        long id = 1L;
        long subtractAmount = 1L;

        UserPoint userPoint = new UserPoint(id, MIN_POINT, System.currentTimeMillis());
        assertThrows(
                InsufficientPointException.class,
                () -> userPoint.subtract(subtractAmount)
        );
    }

    /**
     * 추가 이후 포인트가 최대값을 초과하는 경우 실패
     */
    @Test
    void 추가_포인트_최대값_초과_예외() {
        long id = 1L;
        long addAmount = 1L;

        UserPoint userPoint = new UserPoint(id, MAX_POINT, System.currentTimeMillis());
        assertThrows(
                OverPointLimitException.class,
                () -> userPoint.add(addAmount)
        );
    }

    /**
     * 포인트 생성 성공
     */
    @Test
    void 포인트_생성_성공() {
        long id = 1L;
        long point = 100L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        assertEquals(point, userPoint.point());
    }

    /**
     * 포인트 추가 성공
     */
    @Test
    void 포인트_추가_성공() {
        long id = 1L;
        long addAmount = 1L;

        UserPoint userPoint = new UserPoint(id, MIN_POINT, System.currentTimeMillis());
        UserPoint updatedUserPoint = userPoint.add(addAmount);

        assertEquals(MIN_POINT + addAmount, updatedUserPoint.point());
    }

    /**
     * 포인트 감소 성공
     */
    @Test
    void 포인트_감소_성공() {
        long id = 1L;
        long subtractAmount = 1L;

        UserPoint userPoint = new UserPoint(id, MAX_POINT, System.currentTimeMillis());
        UserPoint updatedUserPoint = userPoint.subtract(subtractAmount);

        assertEquals(MAX_POINT - subtractAmount, updatedUserPoint.point());
    }
}