# 항해플러스 1주처 기본 과제

## 과제 목표
- PATCH `/point/{id}/charge` : 포인트 충전 API
- PATCH `/point/{id}/use` : 포인트 사용 API
- GET `/point/{id}` : 포인트 조회 API
- GET `/point/{id}/histories` : 포인트 내역 조회 API
- 잔고가 부족할 경우, 포인트 사용은 실패하여야 합니다.

## 정책
- 포인트의 최대 값은 10억 입니다.
- 포인트의 최소 값은 0 입니다.
- 포인트를 변경할 시 매개변수에는 음수값이 들어올 수 없습니다. 음수값이 들어올 경우에는 InvalidPointException 예외가 발생합니다.
=======
## 정책
- 포인트의 최대 값은 10억 점 입니다.