package com.prash.fintrackapi.dto;

import java.time.LocalDateTime;

import com.prash.fintrackapi.model.TransactionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private TransactionType type;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}