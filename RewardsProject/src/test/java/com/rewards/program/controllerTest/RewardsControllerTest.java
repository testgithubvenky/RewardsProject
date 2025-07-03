package com.rewards.program.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.rewards.program.DTO.RewardSummaryDTO;
import com.rewards.program.controller.RewardsController;
import com.rewards.program.model.Transaction;
import com.rewards.program.service.RewardService;

public class RewardsControllerTest {

	@Mock
	private RewardService rewardService;

	@InjectMocks
	private RewardsController rewardsController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetRecentTransactions() {
		List<Transaction> transactions = Arrays.asList( new Transaction(1L, 100.0, LocalDate.now()));
		when(rewardService.getLastThreeMonthsTransactions()).thenReturn(transactions);
		List<Transaction> result = rewardsController.getRecentTransactions();
		assertEquals(1, result.size());
	}

	@Test
	void testGetCustomerTransactions_WithMonth() {
		YearMonth month = YearMonth.now();
		List<Transaction> transactions = Arrays.asList(new Transaction(1L, 100.0, LocalDate.now()));
		when(rewardService.getCustomerTransactionsByMonth(1L, month)).thenReturn(transactions);
		List<Transaction> result = rewardsController.getCustomerTransactions(1L, month);
		assertEquals(1, result.size());
	}

	@Test
	void testGetCustomerTransactions_WithoutMonth() {
		List<Transaction> transactions = Arrays.asList(new Transaction(1L, 100.0, LocalDate.now()));
		when(rewardService.getCustomerTransactions(1L)).thenReturn(transactions);
		List<Transaction> result = rewardsController.getCustomerTransactions(1L, null);
		assertEquals(1, result.size());
	}

	@Test
	void testGetRewardSummary() {
		RewardSummaryDTO summary = new RewardSummaryDTO(1L, new HashMap<>(), 100);
		when(rewardService.calculateRewards(1L)).thenReturn(summary);
		RewardSummaryDTO result = rewardsController.getRewardSummary(1L);
		assertEquals(100, result.getTotalPoints());
	}
}
