package com.nutridish.repositories;

import com.nutridish.entities.RecipeEntity;
import com.nutridish.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<RecipeEntity, Long> {

    List<RecipeEntity> findByMealType(String type);

    List<RecipeEntity> findByNameContainingIgnoreCaseAndMealType(String searchKey, String type);

    List<RecipeEntity> findTop4ByFeaturedTrue();

    List<RecipeEntity> findByNameContainingIgnoreCase(String searchKey);

    List<RecipeEntity> findByMealTypeAndNameContainingIgnoreCase(String mealType, String searchKey);

    List<RecipeEntity> findByNameContainingIgnoreCaseAndDietaryTypeIgnoreCaseIn(String searchKey, List<String> tags);

    List<RecipeEntity> findByDietaryTypeIgnoreCaseIn(List<String> tags);

    List<RecipeEntity> findByMealTypeAndDietaryTypeIn(String type, List<String> tags);

    List<RecipeEntity> findByNameContainingIgnoreCaseAndDietaryTypeInAndMealType(String searchKey, List<String> tags, String type);

    List<RecipeEntity> findByMealTypeAndNameContainingIgnoreCaseAndDietaryTypeIn(String mealType, String searchKey, List<String> tags);

    List<RecipeEntity> findByIdInAndDietaryTypeIn(List<Long> ids, List<String> tags);

    List<RecipeEntity> findByIdIn(List<Long> ids);

    List<RecipeEntity> findByMealTypeAndDietaryType(String type, String dietary);
}
