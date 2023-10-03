package com.nutridish.services;

import com.nutridish.dto.MealPlanDTO;
import com.nutridish.entities.FavoriteEntity;
import com.nutridish.entities.NutritionEntity;
import com.nutridish.entities.RecipeEntity;
import com.nutridish.entities.UserEntity;
import com.nutridish.pojos.JsonRes;
import com.nutridish.repositories.FavoriteRepository;
import com.nutridish.repositories.RecipeRepository;
import com.nutridish.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;




    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeEntity> getRecipes(String searchKey, List<String> tags) {
        if (searchKey.isEmpty() && tags.isEmpty()) {
            return recipeRepository.findAll();
        } else if (tags.isEmpty()) {
            return recipeRepository.findByNameContainingIgnoreCase(searchKey);
        } else if (searchKey.isEmpty()) {
            return recipeRepository.findByDietaryTypeIgnoreCaseIn(tags);
        } else {
            return recipeRepository.findByNameContainingIgnoreCaseAndDietaryTypeIgnoreCaseIn(searchKey, tags);
        }
    }

    public List<RecipeEntity> getRecipesByType(String searchKey, List<String> tags, String type) {
        if (searchKey.equals("")) {
            if (tags.isEmpty()) {
                return recipeRepository.findByMealType(type);
            } else {
                return recipeRepository.findByMealTypeAndDietaryTypeIn(type, tags);
            }
        } else {
            if (tags.isEmpty()) {
                return recipeRepository.findByNameContainingIgnoreCaseAndMealType(searchKey, type);
            } else {
                return recipeRepository.findByNameContainingIgnoreCaseAndDietaryTypeInAndMealType(searchKey, tags, type);
            }
        }


    }

    public RecipeEntity getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public List<RecipeEntity> getRecipesFeatured() {
        return recipeRepository.findTop4ByFeaturedTrue();
    }

    public JsonRes<MealPlanDTO> getMealPlan(Long calories, String dietary) {

        List<RecipeEntity> breakfasts = recipeRepository.findByMealTypeAndDietaryType("breakfast", dietary);
        List<RecipeEntity> lunches = recipeRepository.findByMealTypeAndDietaryType("lunch", dietary);
        List<RecipeEntity> dinners = recipeRepository.findByMealTypeAndDietaryType("dinner", dietary);

        // Define the percentage of calories for each meal
        double breakfastPercentage = 0.33;  // 25%
        double lunchPercentage = 0.33;      // 35%
        double dinnerPercentage = 0.33;     // 40%

        // Calculate calories for each meal based on percentages
        Long breakfastCalories = Math.round(calories * breakfastPercentage);
        Long lunchCalories = Math.round(calories * lunchPercentage);
        Long dinnerCalories = Math.round(calories * dinnerPercentage);

        RecipeEntity selectedBreakfast = selectRecipe(breakfasts, breakfastCalories, dietary);
        RecipeEntity selectedLunch = selectRecipe(lunches, lunchCalories, dietary);
        RecipeEntity selectedDinner = selectRecipe(dinners, dinnerCalories, dietary);

        if(selectedDinner != null && selectedBreakfast != null && selectedLunch != null) {
            MealPlanDTO mealPlanDTO = new MealPlanDTO();
            mealPlanDTO.setRecipes(Arrays.asList(selectedBreakfast, selectedLunch, selectedDinner));
            mealPlanDTO.setCombinedNutrition(calculateCombinedNutrition(selectedBreakfast, selectedLunch, selectedDinner));

            return new JsonRes<>(true, 200, "Recipes found", mealPlanDTO);
        }

        return new JsonRes<>(false, 404, "Recipes not found", null);
    }

    private RecipeEntity selectRecipe(List<RecipeEntity> recipes, Long calories, String dietary) {
        List<RecipeEntity> candidates = new ArrayList<>();
        Random random = new Random();

        for (RecipeEntity recipe : recipes) {
            if (calculateCombinedCalories(recipe) <= calories && recipe.getDietaryType().equals(dietary)) {
                candidates.add(recipe);
            }
        }

        if (candidates.isEmpty()) {
            return null; // No suitable recipe found
        }

        int randomIndex = random.nextInt(candidates.size());
        return candidates.get(randomIndex);
    }

    private Long calculateCombinedCalories(RecipeEntity recipe) {
        Long combinedCalories = 0L;
        if (recipe.getNutrition() != null) {
            combinedCalories += recipe.getNutrition().getCalories();
        }
        return combinedCalories;
    }

    private NutritionEntity calculateCombinedNutrition(RecipeEntity breakfast, RecipeEntity lunch, RecipeEntity dinner) {
        NutritionEntity combinedNutrition = new NutritionEntity();
        combinedNutrition.setCalories(breakfast.getNutrition().getCalories() + lunch.getNutrition().getCalories() + dinner.getNutrition().getCalories());
        combinedNutrition.setFats(breakfast.getNutrition().getFats().add(lunch.getNutrition().getFats()).add(dinner.getNutrition().getFats()));
        combinedNutrition.setProtein(breakfast.getNutrition().getProtein().add(lunch.getNutrition().getProtein()).add(dinner.getNutrition().getProtein()));
        combinedNutrition.setCarbohydrates(breakfast.getNutrition().getCarbohydrates().add(lunch.getNutrition().getCarbohydrates()).add(dinner.getNutrition().getCarbohydrates()));

        return combinedNutrition;
    }

}