package com.nutridish.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nutridish.dto.IngredientWithQuantityDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "recipes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Description is required")
    private String description;

    @Column(name = "meal_type")
    @NotBlank(message = "Meal type is required")
    private String mealType;

    @Column(name = "created_by")
    @NotBlank(message = "Created by is required")
    private String createdBy;

    @Column(name = "total_time")
    @NotBlank(message = "Total time is required")
    private int totalTime;

    @Column(name = "servings")
    @NotBlank(message = "Servings is required")
    private int servings;

    @Column(name = "image")
    @NotBlank(message = "Image is required")
    private String image;

    @Column(name = "featured")
    @NotBlank(message = "Featured is required")
    private boolean featured;

    @Column(name = "dietary_type")
    @NotBlank(message = "Dietary type is required")
    private String dietaryType;

    @Column(name = "instructions")
    @NotBlank(message = "Instructions is required")
    private String instructions;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "recipes_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<IngredientEntity> ingredients;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "recipe_tags",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagEntity> tags;


    @Transient
    private boolean favorite;

    @JsonIgnore
    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredientEntity> recipeIngredients;

    @JsonProperty("ingredients")
    public List<IngredientWithQuantityDTO> getIngredientsWithQuantity() {
        List<IngredientWithQuantityDTO> ingredientsWithQuantity = new ArrayList<>();

        for (RecipeIngredientEntity recipeIngredient : recipeIngredients) {
            IngredientWithQuantityDTO ingredientWithQuantityDTO = new IngredientWithQuantityDTO();
            ingredientWithQuantityDTO.setId(recipeIngredient.getIngredient().getId());
            ingredientWithQuantityDTO.setName(recipeIngredient.getIngredient().getName());
            ingredientWithQuantityDTO.setUnit(recipeIngredient.getIngredient().getUnit());
            ingredientWithQuantityDTO.setQuantity(recipeIngredient.getQuantity());
            ingredientsWithQuantity.add(ingredientWithQuantityDTO);
        }

        return ingredientsWithQuantity;
    }

    @JsonIgnoreProperties("recipe")
    @OneToOne(mappedBy = "recipe")
    private NutritionEntity nutrition;

}
