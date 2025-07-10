package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {
    private static final long MIN_POINT = 0L;
    private static final long MAX_POINT = 1_000_000_000L;

    // UserPointService 는 테스트가 깨지지 않기 위하여 실 객체를 사용했습니다.
    private PointService pointService;
    // UserPointTable 은 실제 DB를 사용할 수 없기에 테스트 대역을 사용했습니다.
    @Mock
    private UserPointTable userPointTable;
    // PointHistoryTable 은 실제 DB를 사용할 수 없기에 테스트 대역을 사용했습니다.
    @Mock
    private PointHistoryTable pointHistoryTable;

    @BeforeEach
    void setUp() {
        pointService = new PointService(userPointTable, pointHistoryTable);
    }

    // 유저의 포인트 충전 동작을 확인함으로서 정상적인 동작의 신뢰도 증가
    @Test
    @DisplayName("[성공] 유저의 포인트 충전 동작을 확인함으로서 정상적인 동작의 신뢰도 증가")
    void 정상_포인트_충전_성공() {
        // given
        long userId = 1L;
        long originAmount = MIN_POINT;
        long addAmount = 1000L;
        long expectedAmount = originAmount + addAmount;

        UserPoint updatedUserPoint = new UserPoint(userId, expectedAmount, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(new UserPoint(userId, originAmount, System.currentTimeMillis()));
        when(userPointTable.insertOrUpdate(userId, expectedAmount)).thenReturn(updatedUserPoint);

        // when
        pointService.chargePoint(userId, addAmount);

        // then
        when(userPointTable.selectById(userId)).thenReturn(updatedUserPoint);
        assertThat(pointService.getUserPoint(userId).point()).isEqualTo(expectedAmount);
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
                RuntimeException.class,
                () -> pointService.chargePoint(userPoint.id(), addAmount));
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
                RuntimeException.class,
                () -> pointService.chargePoint(userPoint.id(), addAmount));
    }

    // 유저의 포인트 사용 동작을 확인함으로서 정상적인 동작의 신뢰도 증가
    @Test
    @DisplayName("[성공] 유저의 포인트 사용 동작을 확인함으로서 정상적인 동작의 신뢰도 증가")
    void 정상_포인트_사용_성공() {
        // given
        long userId = 1L;
        long originAmount = 1000L;
        long subtractAmount = 500L;
        long expectedAmount = originAmount - subtractAmount;

        UserPoint updatedUserPoint = new UserPoint(userId, expectedAmount, System.currentTimeMillis());

        when(userPointTable.selectById(userId)).thenReturn(new UserPoint(userId, originAmount, System.currentTimeMillis()));
        when(userPointTable.insertOrUpdate(userId, expectedAmount)).thenReturn(updatedUserPoint);
        // when
        pointService.usePoint(userId, subtractAmount);

        // then
        when(userPointTable.selectById(userId)).thenReturn(updatedUserPoint);
        assertThat(pointService.getUserPoint(userId).point()).isEqualTo(expectedAmount);
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
                RuntimeException.class,
                () -> pointService.usePoint(userPoint.id(), subtractAmount));
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
                RuntimeException.class,
                () -> pointService.usePoint(userPoint.id(), subtractAmount));
    }
}
