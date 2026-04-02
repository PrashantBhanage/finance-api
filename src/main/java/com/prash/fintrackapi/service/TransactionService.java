package com.prash.fintrackapi.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prash.fintrackapi.dto.TransactionRequestDTO;
import com.prash.fintrackapi.dto.TransactionResponseDTO;
import com.prash.fintrackapi.model.Category;
import com.prash.fintrackapi.model.Transaction;
import com.prash.fintrackapi.model.TransactionType;
import com.prash.fintrackapi.model.User;
import com.prash.fintrackapi.repository.CategoryRepository;
import com.prash.fintrackapi.repository.TransactionRepository;
import com.prash.fintrackapi.repository.UserRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    // Create a new transaction
    public TransactionResponseDTO createTransaction(TransactionRequestDTO request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setNote(request.getNote());
        transaction.setUser(user);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
            transaction.setCategory(category);
        }

        Transaction saved = transactionRepository.save(transaction);
        return mapToResponseDTO(saved);
    }

    // Get all transactions for user
    public List<TransactionResponseDTO> getAllTransactions(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return transactionRepository.findByUser(user).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get transaction by ID
    public TransactionResponseDTO getTransactionById(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponseDTO(transaction);
    }

    // Update transaction
    public TransactionResponseDTO updateTransaction(Long id, TransactionRequestDTO request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDate(request.getDate());
        transaction.setNote(request.getNote());
        transaction.setUpdatedAt(LocalDateTime.now());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));
            transaction.setCategory(category);
        } else {
            transaction.setCategory(null);
        }

        Transaction updated = transactionRepository.save(transaction);
        return mapToResponseDTO(updated);
    }

    // Delete transaction
    public void deleteTransaction(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));

        if (!transaction.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        transactionRepository.deleteById(id);
    }

    // Filter transactions
    public List<TransactionResponseDTO> getTransactionsByFilters(String userEmail, LocalDateTime startDate, LocalDateTime endDate, Long categoryId, TransactionType type) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions;

        if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByUserAndDateBetween(user, startDate.toLocalDate(), endDate.toLocalDate());
        } else if (categoryId != null) {
            transactions = transactionRepository.findByUserAndCategoryId(user, categoryId);
        } else if (type != null) {
            transactions = transactionRepository.findByUserAndType(user, type);
        } else {
            transactions = transactionRepository.findByUser(user);
        }

        return transactions.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
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