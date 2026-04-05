package com.prash.fintrackapi.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prash.fintrackapi.dto.TransactionRequestDTO;
import com.prash.fintrackapi.dto.TransactionResponseDTO;
import com.prash.fintrackapi.model.TransactionType;
import com.prash.fintrackapi.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Valid @RequestBody TransactionRequestDTO request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        TransactionResponseDTO created = transactionService.createTransaction(request, userEmail);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(Authentication authentication) {
        String userEmail = authentication.getName();
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions(userEmail);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getTransactionById(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        TransactionResponseDTO transaction = transactionService.getTransactionById(id, userEmail);
        return ResponseEntity.ok(transaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequestDTO request,
            Authentication authentication) {
        String userEmail = authentication.getName();
        TransactionResponseDTO updated = transactionService.updateTransaction(id, request, userEmail);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @PathVariable Long id,
            Authentication authentication) {
        String userEmail = authentication.getName();
        transactionService.deleteTransaction(id, userEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByFilters(
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) TransactionType type,
            Authentication authentication) {
        String userEmail = authentication.getName();
        List<TransactionResponseDTO> transactions = transactionService.getTransactionsByFilters(
                userEmail, startDate, endDate, categoryId, type);
        return ResponseEntity.ok(transactions);
    }
}