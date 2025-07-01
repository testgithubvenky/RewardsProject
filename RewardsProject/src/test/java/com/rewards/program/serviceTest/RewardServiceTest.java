package com.rewards.program.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rewards.program.DTO.RewardSummaryDTO;
import com.rewards.program.exception.ResourceNotFoundException;
import com.rewards.program.model.Transaction;
import com.rewards.program.repository.TransactionRepository;
import com.rewards.program.service.RewardService;

public class RewardServiceTest {

	@InjectMocks
	private RewardService rewardService;

	@Mock
	private TransactionRepository transactionRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testGetLastThreeMonthsTransactions_Success() {
		List<Transaction> transactions = List.of(new Transaction(1L, 100.0, LocalDate.now()));
		when(transactionRepository.findByTransactionDateAfter(any())).thenReturn(transactions);
		List<Transaction> result = rewardService.getLastThreeMonthsTransactions();
		assertEquals(1, result.size());
	}

	@Test
	void testGetLastThreeMonthsTransactions_Empty() {
		when(transactionRepository.findByTransactionDateAfter(any())).thenReturn(Collections.emptyList());
		assertThrows(ResourceNotFoundException.class, () -> rewardService.getLastThreeMonthsTransactions());
	}

	@Test
	void testGetCustomerTransactions_Success() {
		List<Transaction> transactions = List.of(new Transaction(1L, 120.0, LocalDate.now()));
		when(transactionRepository.findByCustomerId(1L)).thenReturn(transactions);
		List<Transaction> result = rewardService.getCustomerTransactions(1L);
		assertEquals(1, result.size());
	}

	@Test
	void testGetCustomerTransactionsByMonth_Success() {
		YearMonth month = YearMonth.now();
		List<Transaction> transactions = List.of(new Transaction(1L, 150.0, LocalDate.now()));
		when(transactionRepository.findByCustomerIdAndTransactionDateBetween(anyLong(), any(), any())).thenReturn(transactions);
		List<Transaction> result = rewardService.getCustomerTransactionsByMonth(1L, month);
		assertEquals(1, result.size());
	}

	@Test
	void testCalculateRewards_Success() {
		List<Transaction> transactions = List.of(new Transaction(1L, 120.0, LocalDate.now()));
		when(transactionRepository.findByCustomerId(1L)).thenReturn(transactions);
		RewardSummaryDTO summary = rewardService.calculateRewards(1L);
		assertEquals(90, summary.getTotalPoints()); 
	}
}
