package com.rewards.program.serviceTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.rewards.program.DTO.RewardSummaryDTO;
import com.rewards.program.exception.ResourceNotFoundException;
import com.rewards.program.model.Transaction;
import com.rewards.program.repository.TransactionRepository;
import com.rewards.program.service.RewardService;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {
	@InjectMocks
	private RewardService rewardService;
	@Mock
	public TransactionRepository transactionRepository;

	@Test
	void testGetLastThreeMonthsTransactions_success() {
		List<Transaction> mockTxns = List.of(new Transaction(1L, 100L, LocalDate.now().minusDays(10), 150.0));
		when(transactionRepository.findByTransactionDateAfter(any(LocalDate.class))).thenReturn(mockTxns);
		List<Transaction> result = rewardService.getLastThreeMonthsTransactions();
		assertEquals(1, result.size());
	}

	@Test
	void testGetLastThreeMonthsTransactions_empty_throwsException() {
		when(transactionRepository.findByTransactionDateAfter(any(LocalDate.class))).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> rewardService.getLastThreeMonthsTransactions());
	}

	@Test
	void testGetCustomerTransactions_success() {
		List<Transaction> txns = List.of(new Transaction(2L, 101L, LocalDate.now(), 80.0));
		when(transactionRepository.findByCustomerId(101L)).thenReturn(txns);
		assertEquals(1, rewardService.getCustomerTransactions(101L).size());
	}

	@Test
	void testGetCustomerTransactions_notFound() {
		when(transactionRepository.findByCustomerId(anyLong())).thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class, () -> rewardService.getCustomerTransactions(111L));
	}

	@Test
	void testGetCustomerTransactionsByMonth_success() {
		YearMonth ym = YearMonth.of(2025, 7);
		LocalDate start = ym.atDay(1);
		LocalDate end = ym.atEndOfMonth();
		List<Transaction> txns = List.of(new Transaction(3L, 201L, LocalDate.of(2025, 7, 10), 60.0));
		when(transactionRepository.findByCustomerIdAndTransactionDateBetween(eq(201L), eq(start), eq(end)))
		.thenReturn(txns);
		List<Transaction> result = rewardService.getCustomerTransactionsByMonth(201L, ym);
		assertEquals(60.0, result.get(0).getAmount());
	}

	@Test
	void testGetCustomerTransactionsByMonth_noResults_throwsException() {
		when(transactionRepository.findByCustomerIdAndTransactionDateBetween(anyLong(), any(), any()))
		.thenReturn(List.of());
		assertThrows(ResourceNotFoundException.class,
				() -> rewardService.getCustomerTransactionsByMonth(201L, YearMonth.of(2025, 8)));
	}

	@Test
	void testCalculateRewards_pointsAndMonthlyBreakdown() {
		List<Transaction> txns = List.of(
				new Transaction(10L, 301L, LocalDate.of(2025, 7, 12), 120.0),  
				new Transaction(11L, 301L, LocalDate.of(2025, 7, 15), 70.0)    
				);
		when(transactionRepository.findByCustomerId(301L)).thenReturn(txns);
		RewardSummaryDTO dto = rewardService.calculateRewards(301L);
		assertEquals(110, dto.getTotalPoints());
		assertEquals(2, dto.getTransactions().size());
		assertEquals(110, dto.getMonthlyPoints().get("JULY"));
	}

	@Test
	void testCalculatePoints_logic() {
		assertEquals(0, RewardService.calculatePoints(49));
		assertEquals(10, RewardService.calculatePoints(60));     
		assertEquals(50, RewardService.calculatePoints(100));    
		assertEquals(90, RewardService.calculatePoints(120));    
		assertEquals(100, RewardService.calculatePoints(125));   
	}
}
