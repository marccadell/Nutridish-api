package com.nutridish.dto;

import com.nutridish.entities.NutritionEntity;
import com.nutridish.entities.RecipeEntity;
import lombok.Data;

import java.util.List;

@Data
public class MealPlanDTO {
    private List<RecipeEntity> recipes;
    private NutritionEntity combinedNutrition;
}