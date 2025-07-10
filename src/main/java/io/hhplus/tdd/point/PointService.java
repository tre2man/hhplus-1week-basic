package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

import java.util.List;

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

    void chargePoint(long userId, long addAmount) {
        long currentPoint = userPointTable.selectById(userId).point();
        long newPoint = currentPoint + addAmount;
        userPointTable.insertOrUpdate(userId, newPoint);
        pointHistoryTable.insert(userId, addAmount, TransactionType.CHARGE, System.currentTimeMillis());
    }

    void usePoint(long userId, long useAmount) {
        long currentPoint = userPointTable.selectById(userId).point();
        long newPoint = currentPoint - useAmount;
        userPointTable.insertOrUpdate(userId, newPoint);
        pointHistoryTable.insert(userId, useAmount, TransactionType.USE, System.currentTimeMillis());
    }

    List<PointHistory> getPointHistories(long userId) {
        return pointHistoryTable.selectAllByUserId(userId);
    }
}