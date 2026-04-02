package com.prash.fintrackapi.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netBalance;
    private Map<String, BigDecimal> categoryTotals;
    private List<TransactionResponseDTO> recentTransactions;
    private Map<String, BigDecimal> monthlySummary;
}