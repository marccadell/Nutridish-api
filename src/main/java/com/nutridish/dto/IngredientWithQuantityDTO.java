package com.nutridish.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientWithQuantityDTO {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal quantity;
}