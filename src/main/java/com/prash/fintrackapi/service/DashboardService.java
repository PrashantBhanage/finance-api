package com.prash.fintrackapi.service;

import com.prash.fintrackapi.dto.DashboardDTO;
import com.prash.fintrackapi.dto.TransactionResponseDTO;
import com.prash.fintrackapi.model.Transaction;
import com.prash.fintrackapi.model.TransactionType;
import com.prash.fintrackapi.model.User;
import com.prash.fintrackapi.repository.TransactionRepository;
import com.prash.fintrackapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    // Get dashboard data for user
    public DashboardDTO getDashboard(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Calculate totals
        BigDecimal totalIncome = transactionRepository.sumAmountByUserAndType(user, TransactionType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumAmountByUserAndType(user, TransactionType.EXPENSE);

        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        if (totalExpense == null) totalExpense = BigDecimal.ZERO;

        BigDecimal netBalance = totalIncome.subtract(totalExpense);

        // Category-wise totals
        Map<String, BigDecimal> categoryTotals = new HashMap<>();
        List<Transaction> userTransactions = transactionRepository.findByUser(user);

        Map<String, BigDecimal> incomeByCategory = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME && t.getCategory() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        Map<String, BigDecimal> expenseByCategory = userTransactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE && t.getCategory() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)
                ));

        // Combine income and expense by category
        for (String category : incomeByCategory.keySet()) {
            BigDecimal income = incomeByCategory.get(category);
            BigDecimal expense = expenseByCategory.getOrDefault(category, BigDecimal.ZERO);
            categoryTotals.put(category, income.subtract(expense));
        }

        for (String category : expenseByCategory.keySet()) {
            if (!categoryTotals.containsKey(category)) {
                categoryTotals.put(category, expenseByCategory.get(category).negate());
            }
        }

        // Recent transactions
        Pageable pageable = PageRequest.of(0, 10);
        List<TransactionResponseDTO> recentTransactions = transactionRepository.findRecentByUser(user, pageable)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());

        // Monthly summary (current year)
        Map<String, BigDecimal> monthlySummary = new HashMap<>();
        int currentYear = LocalDate.now().getYear();
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            LocalDate start = yearMonth.atDay(1);
            LocalDate end = yearMonth.atEndOfMonth();

            List<Transaction> monthlyTransactions = transactionRepository
                    .findByUserAndDateBetween(user, start, end);

            BigDecimal monthlyTotal = monthlyTransactions.stream()
                    .map(t -> t.getType() == TransactionType.INCOME ? t.getAmount() : t.getAmount().negate())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            monthlySummary.put(yearMonth.toString(), monthlyTotal);
        }

        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setTotalIncome(totalIncome);
        dashboard.setTotalExpense(totalExpense);
        dashboard.setNetBalance(netBalance);
        dashboard.setCategoryTotals(categoryTotals);
        dashboard.setRecentTransactions(recentTransactions);
        dashboard.setMonthlySummary(monthlySummary);

        return dashboard;
    }

    // Helper method to map Transaction to TransactionResponseDTO
    private TransactionResponseDTO mapToResponseDTO(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setDate(transaction.getDate());
        dto.setNote(transaction.getNote());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());

        if (transaction.getCategory() != null) {
            dto.setCategoryId(transaction.getCategory().getId());
            dto.setCategoryName(transaction.getCategory().getName());
        }

        return dto;
    }
}