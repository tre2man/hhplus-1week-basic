package io.hhplus.tdd.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PointIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    // 초기 유저 조회 시 포인트는 0이어야 함
    @Test
    void 초기_유저_조회_포인트_0() throws Exception {
        // given
        long userId = 1L;

        // when
        mockMvc.perform(get("/point/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0L));
    }

    // 유저의 포인트 충전 기능이 양수 포인트일 경우 정상적으로 동작해야 함
    @Test
    void 유저_포인트_충전_성공() throws Exception {
        // given
        long userId = 1L;
        long chargeAmount = 1000L;

        // when
        mockMvc.perform(get("/point/{id}/charge", userId)
                .content(String.valueOf(chargeAmount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(chargeAmount));
    }

    // 유저의 포인트 충전 기능이 음수 포인트일 경우 실패해야 함
    @Test
    void 유저_포인트_충전_실패_음수() throws Exception {
        // given
        long userId = 1L;
        long chargeAmount = -1000L;

        // when
        mockMvc.perform(get("/point/{id}/charge", userId)
                .content(String.valueOf(chargeAmount)))
                .andExpect(status().isBadRequest());
    }

    // 유저의 포인트 충전 기능이 10억 이상일 경우 실패해야 함
    @Test
    void 유저_포인트_충전_실패_10억이상() throws Exception {
        // given
        long userId = 1L;
        long chargeAmount = 1_000_000_001L; // 10억 이상

        // when
        mockMvc.perform(get("/point/{id}/charge", userId)
                .content(String.valueOf(chargeAmount)))
                .andExpect(status().is4xxClientError());
    }

    // 유저의 포인트 사용 기능이 양수 포인트일 경우 정상적으로 동작해야 함
    @Test
    void 유저_포인트_사용_성공() throws Exception {
        // given
        long userId = 1L;
        long useAmount = 500L;

        // when
        mockMvc.perform(get("/point/{id}/use", userId)
                .content(String.valueOf(useAmount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(500L));
    }

    // 유저의 포인트 사용 기능이 음수 포인트일 경우 실패해야 함
    @Test
    void 유저_포인트_사용_실패_음수() throws Exception {
        // given
        long userId = 1L;
        long useAmount = -500L;

        // when
        mockMvc.perform(get("/point/{id}/use", userId)
                .content(String.valueOf(useAmount)))
                .andExpect(status().isBadRequest());
    }

    // 유저의 포인트 사용 결과가 0 이하일 경우 실패해야 함
    @Test
    void 유저_포인트_사용_실패_결과_0이하() throws Exception {
        // given
        long userId = 1L;
        long useAmount = 1000L; // 초기 포인트가 0이므로 사용 시 실패

        // when
        mockMvc.perform(get("/point/{id}/use", userId)
                .content(String.valueOf(useAmount)))
                .andExpect(status().isBadRequest());
    }

    // 유저의 포인트 충전 내역 조회 기능이 정상적으로 동작해야 함
    @Test
    void 유저_포인트_충전_내역_조회_성공() throws Exception {
        // given
        long userId = 1L;

        // when
        mockMvc.perform(patch("/point/{id}/charge", userId)
                .content("1000"))
                .andExpect(status().isOk());

        // then
        // 충전 내역이 정상적으로 조회되었는지 검증
        mockMvc.perform(get("/point/{id}/histories", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].amount").value(1000L));
    }
}
