package com.nutridish.services;

import com.nutridish.entities.FavoriteEntity;
import com.nutridish.entities.RecipeEntity;
import com.nutridish.entities.UserEntity;
import com.nutridish.pojos.JsonRes;
import com.nutridish.repositories.FavoriteRepository;
import com.nutridish.repositories.RecipeRepository;
import com.nutridish.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteService {
    final FavoriteRepository favoriteRepository;
    final UserRepository userRepository;
    final RecipeRepository recipeRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, UserRepository userRepository, RecipeRepository recipeRepository) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.recipeRepository = recipeRepository;
    }

    public List<RecipeEntity> getUserFavourite(Long userId, String searchKey, List<String> tags) {
        List<FavoriteEntity> favoriteEntities;

        if (searchKey != null && !searchKey.isEmpty()) {
            favoriteEntities = favoriteRepository.findByUserIdAndNameContainingIgnoreCase(userId, searchKey);
        } else {
            favoriteEntities = favoriteRepository.findByUserId(userId);
        }

        List<Long> recipeIds = favoriteEntities.stream()
                .map(FavoriteEntity::getRecipe)
                .map(RecipeEntity::getId)
                .toList();

        List<RecipeEntity> recipes;

        if (!tags.isEmpty()) {
            recipes = recipeRepository.findByIdInAndDietaryTypeIn(recipeIds, tags);
        } else {
            recipes = recipeRepository.findByIdIn(recipeIds);
        }

        for (RecipeEntity recipe : recipes) {
            recipe.setFavorite(true);
        }

        return recipes;
    }

    public List<RecipeEntity> getRecipesByMealTypeAndFavoriteAndSearchKey(Long userId, String mealType, String searchKey, List<String> tags) {
        List<RecipeEntity> recipes;

        if (searchKey != null && !searchKey.isEmpty()) {
            if (!tags.isEmpty()) {
                recipes = recipeRepository.findByMealTypeAndNameContainingIgnoreCaseAndDietaryTypeIn(mealType, searchKey, tags);
            } else {
                recipes = recipeRepository.findByMealTypeAndNameContainingIgnoreCase(mealType, searchKey);
            }
        } else {
            if (!tags.isEmpty()) {
                recipes = recipeRepository.findByMealTypeAndDietaryTypeIn(mealType, tags);
            } else {
                recipes = recipeRepository.findByMealType(mealType);
            }
        }

        UserEntity user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            List<FavoriteEntity> userFavorites = favoriteRepository.findByUser(user);
            List<Long> favoriteRecipeIds = userFavorites.stream()
                    .map(FavoriteEntity::getRecipe)
                    .map(RecipeEntity::getId)
                    .toList();

            for (RecipeEntity recipe : recipes) {
                recipe.setFavorite(favoriteRecipeIds.contains(recipe.getId()));
            }
        }

        return recipes;
    }

    public JsonRes<FavoriteEntity> addRecipeToFavorites(Long userId, Long recipeId) {

        UserEntity user = userRepository.findById(userId)
                .orElse(null);

        RecipeEntity recipe = recipeRepository.findById(recipeId)
                .orElse(null);

        /*boolean isAlreadyFavorite = favoriteRepository.existsByUserAndRecipe(user, recipe);
        if (isAlreadyFavorite) {
            return new JsonRes<>(false, 400, "Recipe is already in favorites", null);
        }*/

        FavoriteEntity existingFavorite = favoriteRepository.findByUserAndRecipe(user, recipe);

        if (existingFavorite != null) {
            favoriteRepository.delete(existingFavorite);
            return new JsonRes<>(true, 200, "Recipe removed from favorites", null);
        } else {
            FavoriteEntity favorite = new FavoriteEntity();
            favorite.setUser(user);
            favorite.setRecipe(recipe);

            FavoriteEntity fr = favoriteRepository.save(favorite);

            return new JsonRes<>(true, 200, "Recipe added to favorites", fr);
        }
    }

    public List<RecipeEntity> getRecipesFeaturedFavorite(Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return null;
        }

        List<FavoriteEntity> userFavorites = favoriteRepository.findByUser(user);
        List<Long> favoriteRecipeIds = userFavorites.stream()
                .map(FavoriteEntity::getRecipe)
                .map(RecipeEntity::getId)
                .toList();

        List<RecipeEntity> featuredRecipes = recipeRepository.findTop4ByFeaturedTrue();

        for (RecipeEntity recipe : featuredRecipes) {
            recipe.setFavorite(favoriteRecipeIds.contains(recipe.getId()));
        }

        return featuredRecipes;
    }

    /*public List<RecipeEntity> getAllRecipesWithFavoriteIndicator(Long userId) {
        List<RecipeEntity> recipes = recipeRepository.findAll();

        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<FavoriteEntity> userFavorites = favoriteRepository.findByUser(user);

            List<Long> favoriteRecipeIds = userFavorites.stream()
                    .map(FavoriteEntity::getRecipe)
                    .map(RecipeEntity::getId)
                    .toList();

            for (RecipeEntity recipe : recipes) {
                recipe.setFavorite(favoriteRecipeIds.contains(recipe.getId()));
            }
        }

        return recipes;
    }*/

}
