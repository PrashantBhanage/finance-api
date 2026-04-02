package com.prash.fintrackapi.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prash.fintrackapi.model.Transaction;
import com.prash.fintrackapi.model.TransactionType;
import com.prash.fintrackapi.model.User;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByUser(User user);

    List<Transaction> findByUserAndDateBetween(User user, LocalDate startDate, LocalDate endDate);

    List<Transaction> findByUserAndCategoryId(User user, Long categoryId);

    List<Transaction> findByUserAndType(User user, TransactionType type);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.user = :user AND t.type = :type")
    BigDecimal sumAmountByUserAndType(@Param("user") User user, @Param("type") TransactionType type);

    @Query("SELECT t FROM Transaction t WHERE t.user = :user ORDER BY t.date DESC")
    List<Transaction> findRecentByUser(@Param("user") User user, org.springframework.data.domain.Pageable pageable);
}