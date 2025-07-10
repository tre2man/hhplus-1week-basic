package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    UserPoint getUserPoint(long userId) {
        return userPointTable.selectById(userId);
    }

    UserPoint chargePoint(long userId, long addAmount) {
        if (addAmount < 0) {
            throw new IllegalArgumentException("충전 금액은 양수여야 합니다.");
        }
        long currentPoint = userPointTable.selectById(userId).point();
        long newPoint = currentPoint + addAmount;
        userPointTable.insertOrUpdate(userId, newPoint);
        pointHistoryTable.insert(userId, addAmount, TransactionType.CHARGE, System.currentTimeMillis());
        return userPointTable.selectById(userId);
    }

    UserPoint usePoint(long userId, long useAmount) {
        if (useAmount < 0) {
            throw new IllegalArgumentException("사용 금액은 양수여야 합니다.");
        }
        long currentPoint = userPointTable.selectById(userId).point();
        long newPoint = currentPoint - useAmount;
        userPointTable.insertOrUpdate(userId, newPoint);
        pointHistoryTable.insert(userId, useAmount, TransactionType.USE, System.currentTimeMillis());
        return userPointTable.selectById(userId);
    }

    List<PointHistory> getPointHistories(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}