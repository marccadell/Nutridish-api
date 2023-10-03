package com.nutridish.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
@Table(name = "nutritions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NutritionEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "recipe_id")
    private RecipeEntity recipe;

    @Column(name = "calories")
    @NotBlank(message = "Calories is required")
    private Long calories;

    @Column(name = "protein")
    @NotBlank(message = "Protein is required")
    private BigDecimal protein;

    @Column(name = "carbohydrates")
    @NotBlank(message = "Carbohydrates is required")
    private BigDecimal carbohydrates;

    @Column(name = "fats")
    @NotBlank(message = "Fats is required")
    private BigDecimal fats;

}
