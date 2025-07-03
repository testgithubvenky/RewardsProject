package com.rewards.program.DTO;

import java.util.Map;

public class RewardSummaryDTO {
	
	private Long customerId;
	private Map<String, Integer> monthlyPoints;
	private int totalPoints;
	public RewardSummaryDTO(Long customerId, Map<String, Integer> monthlyPoints, int totalPoints) {
		super();
		this.customerId = customerId;
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
	}	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Map<String, Integer> getMonthlyPoints() {
		return monthlyPoints;
	}
	public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
		this.monthlyPoints = monthlyPoints;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
}
