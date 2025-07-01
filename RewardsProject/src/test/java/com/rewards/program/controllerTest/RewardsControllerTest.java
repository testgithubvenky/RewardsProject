package com.rewards.program.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.rewards.program.DTO.RewardSummaryDTO;
import com.rewards.program.controller.RewardsController;
import com.rewards.program.model.Transaction;
import com.rewards.program.service.RewardService;

public class RewardsControllerTest {

	private MockMvc mockMvc;

	@Mock
	private RewardService rewardService;

	@InjectMocks
	private RewardsController rewardsController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(rewardsController).build();
	}

	@Test
	void testGetRecentTransactions() throws Exception {
		when(rewardService.getLastThreeMonthsTransactions()).thenReturn(List.of(new Transaction(1L, 100.0, LocalDate.now())));
		mockMvc.perform(get("/rewards/transactions/recent")).andExpect(status().isOk());
	}

	@Test
	void testGetCustomerTransactions() throws Exception {
		when(rewardService.getCustomerTransactions(1L)).thenReturn(List.of(new Transaction(1L, 100.0, LocalDate.now())));
		mockMvc.perform(get("/rewards/transactions/1")).andExpect(status().isOk());
	}

	@Test
	void testGetCustomerTransactionsByMonth() throws Exception {
		when(rewardService.getCustomerTransactionsByMonth(eq(1L), any())).thenReturn(List.of(new Transaction(1L, 100.0, LocalDate.now())));
		mockMvc.perform(get("/rewards/transactions/1/2025-06")).andExpect(status().isOk());
	}

	@Test
	void testGetRewardSummary() throws Exception {
		Map<String, Integer> monthly = Map.of("JUNE", 90);
		RewardSummaryDTO dto = new RewardSummaryDTO(1L, monthly, 90);
		when(rewardService.calculateRewards(1L)).thenReturn(dto);
		mockMvc.perform(get("/rewards/summary/1")).andExpect(status().isOk());
	}
}
