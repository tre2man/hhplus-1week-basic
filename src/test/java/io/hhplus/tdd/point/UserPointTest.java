package io.hhplus.tdd.point;

import io.hhplus.tdd.exception.InsufficientPointException;
import io.hhplus.tdd.exception.InvalidPointException;
import io.hhplus.tdd.exception.OverPointLimitException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * 해당 테스트는 경계값(정책)을 기준으로 경계값, 미만/초과 값을 테스트로 진행했습니다.
 */
class UserPointTest {
    /**
     * 최소 경계값 0 보다 1 작은 포인트 객체 생성 실패
     */
    @Test
    void 음수_포인트_객체_생성_실패() {
        long id = 1L;
        long point = -100L;

        assertThrows(
                InvalidPointException.class,
                () -> new UserPoint(id, point, System.currentTimeMillis())
        );
    }

    /**
     * 최소 경계값 0 포인트 객체 생성 성공
     */
    @Test
    void 경계값_포인트_객체_생성_성공() {
        long id = 1L;
        long point = 0L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        assert userPoint.point() == 0L;
    }

    /**
     * 최대 경계값 10억 포인트 객체 생성 성공
     */
    @Test
    void 최대값_포인트_객체_생성_성공() {
        long id = 1L;
        long point = 1_000_000_000L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        assert userPoint.point() == 1_000_000_000L;
    }

    /**
     * 최대 경계값 10억 보다 1 큰 포인트 객체 생성 실패
     */
    @Test
    void 최대값_초과_포인트_객체_생성_실패() {
        long id = 1L;
        long point = 1_000_000_001L;

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
                InvalidPointException.class,
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
                InvalidPointException.class,
                () -> userPoint.subtract(subtractAmount)
        );
    }

    /**
     * 감소 이후 포인트가 0 미만이 되는 경우 실패
     */
    @Test
    void 감소_포인트_0미만_예외() {
        long id = 1L;
        long point = 0L;
        long subtractAmount = 1L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
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
        long point = 1_000_000_000L;
        long addAmount = 1L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        assertThrows(
                OverPointLimitException.class,
                () -> userPoint.add(addAmount)
        );
    }

    /**
     * 포인트 생성 성공
     */
    void 포인트_생성_성공() {
        long id = 1L;
        long point = 100L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        assert userPoint.point() == 100L;
    }

    /**
     * 포인트 추가 성공
     */
    void 포인트_추가_성공() {
        long id = 1L;
        long point = 0L;
        long addAmount = 1L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        UserPoint updatedUserPoint = userPoint.add(addAmount);

        assert updatedUserPoint.point() == 1L;
    }

    /**
     * 포인트 감소 성공
     */
    void 포인트_감소_성공() {
        long id = 1L;
        long point = 1_000_000_000L;
        long subtractAmount = 1L;

        UserPoint userPoint = new UserPoint(id, point, System.currentTimeMillis());
        UserPoint updatedUserPoint = userPoint.subtract(subtractAmount);

        assert updatedUserPoint.point() == 999_999_999L;
    }
}