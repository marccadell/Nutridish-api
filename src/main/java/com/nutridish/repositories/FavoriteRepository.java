package com.nutridish.repositories;

import com.nutridish.entities.FavoriteEntity;
import com.nutridish.entities.RecipeEntity;
import com.nutridish.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    List<FavoriteEntity> findByUserId(Long id);

    List<FavoriteEntity> findByUser(UserEntity user);

    FavoriteEntity findByUserAndRecipe(UserEntity user, RecipeEntity recipe);

    @Query("SELECT f FROM FavoriteEntity f JOIN f.user u JOIN f.recipe r WHERE u.id = :userId AND LOWER(r.name) LIKE %:searchKey%")
    List<FavoriteEntity> findByUserIdAndNameContainingIgnoreCase(Long userId, String searchKey);
}
