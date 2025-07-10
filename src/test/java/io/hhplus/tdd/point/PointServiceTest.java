package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.exception.InsufficientPointException;
import io.hhplus.tdd.exception.OverPointLimitException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class PointServiceTest {
    private static final long MIN_POINT = 0L;
    private static final long MAX_POINT = 1_000_000_000L;

    // UserPointService 는 테스트가 깨지지 않기 위하여 실 객체를 사용했습니다.
    private UserPointService userPointService;
    // UserPointTable 은 실제 DB를 사용할 수 없기에 테스트 대역을 사용했습니다.
    @Mock
    private UserPointTable userPointTable;
    // PointHistoryTable 은 실제 DB를 사용할 수 없기에 테스트 대역을 사용했습니다.
    @Mock
    private PointHistoryTable pointHistoryTable;


    // 유저의 포인트 충전 동작을 확인함으로서 정상적인 동작의 신뢰도 증가
    @Test
    @DisplayName("[성공] 유저의 포인트 충전 동작을 확인함으로서 정상적인 동작의 신뢰도 증가")
    void 정상_포인트_충전_성공() {
        // given
        // 유저의 포인트 충전 동작을 확인함으로서 정상적인 동작의 신뢰도 증가
        long userId = 1L;
        long originAmount = MIN_POINT;
        long addAmount = 1000L;

        UserPoint userPoint = new UserPoint(userId, originAmount, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(userId, originAmount)).thenReturn(userPoint);
        when(userPointTable.selectById(userId)).thenReturn(userPoint);

        // when
        userPointService.chargePoint(userPoint.id(), addAmount);

        // then
        // 유저의 포인트 충전 동작을 확인함으로서 정상적인 동작의 신뢰도 증가
        assertThat(userPoint.point()).isEqualTo(originAmount + addAmount);
    }

    // 유저가 음수 포인트 충전 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가
    @Test
    @DisplayName("[실패] 유저가 음수 포인트 충전 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가")
    void 음수_포인트_충전_실패() {
        // given
        long userId = 1L;
        long originAmount = MIN_POINT;
        long addAmount = -1000L;

        UserPoint userPoint = new UserPoint(userId, originAmount, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(userId, originAmount)).thenReturn(userPoint);
        when(userPointTable.selectById(userId)).thenReturn(userPoint);

        // when & then
        // 유저가 음수 포인트 충전 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가
        assertThrows(
                InsufficientPointException.class,
                () -> userPointService.chargePoint(userPoint.id(), addAmount));
    }

    // 유저가 포인트 충전 시 포인트 최대값을 초과하는 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가
    @Test
    @DisplayName("[실패] 유저가 포인트 충전 시 포인트 최대값을 초과하는 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가")
    void 포인트_최대값_초과_충전_실패() {
        // given
        long userId = 1L;
        long originAmount = MAX_POINT;
        long addAmount = 1L;

        UserPoint userPoint = new UserPoint(userId, originAmount, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(userId, originAmount)).thenReturn(userPoint);
        when(userPointTable.selectById(userId)).thenReturn(userPoint);

        // when & then
        // 유저가 포인트 충전 시 포인트 최대값을 초과하는 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가
        assertThrows(
                OverPointLimitException.class,
                () -> userPointService.chargePoint(userPoint.id(), addAmount));
    }

    // 유저의 포인트 사용 동작을 확인함으로서 정상적인 동작의 신뢰도 증가
    @Test
    @DisplayName("[성공] 유저의 포인트 사용 동작을 확인함으로서 정상적인 동작의 신뢰도 증가")
    void 정상_포인트_사용_성공() {
        // given
        long userId = 1L;
        long originAmount = 1000L;
        long subtractAmount = 500L;

        UserPoint userPoint = new UserPoint(userId, originAmount, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(userId, originAmount)).thenReturn(userPoint);
        when(userPointTable.selectById(userId)).thenReturn(userPoint);

        // when
        userPointService.usePoint(userPoint.id(), subtractAmount);

        // then
        // 유저의 포인트 사용 동작을 확인함으로서 정상적인 동작의 신뢰도 증가
        assertThat(userPoint.point()).isEqualTo(originAmount - subtractAmount);
    }

    // 유저가 음수 포인트 사용 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가
    @Test
    @DisplayName("[실패] 유저가 음수 포인트 사용 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가")
    void 음수_포인트_사용_실패() {
        // given
        long userId = 1L;
        long originAmount = 1000L;
        long subtractAmount = -1L;

        UserPoint userPoint = new UserPoint(userId, originAmount, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(userId, originAmount)).thenReturn(userPoint);
        when(userPointTable.selectById(userId)).thenReturn(userPoint);

        // when & then
        // 유저가 음수 포인트 사용 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가
        assertThrows(
                InsufficientPointException.class,
                () -> userPointService.usePoint(userPoint.id(), subtractAmount));
    }

    // 유저가 포인트를 사용한 이후의 금액이 0 미만이 되는 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가
    @Test
    @DisplayName("[실패] 유저가 포인트를 사용한 이후의 금액이 0 미만이 되는 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가")
    void 포인트_사용_0미만_실패() {
        // given
        long userId = 1L;
        long originAmount = MIN_POINT;
        long subtractAmount = 1L;

        UserPoint userPoint = new UserPoint(userId, originAmount, System.currentTimeMillis());
        when(userPointTable.insertOrUpdate(userId, originAmount)).thenReturn(userPoint);
        when(userPointTable.selectById(userId)).thenReturn(userPoint);

        // when & then
        // 유저가 포인트를 사용한 이후의 금액이 0 미만이 되는 동작을 시도했을 때 예외가 발생함으로서 정상적인 동작의 신뢰도 증가
        assertThrows(
                InsufficientPointException.class,
                () -> userPointService.usePoint(userPoint.id(), subtractAmount));
    }
}
