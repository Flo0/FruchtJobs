package com.gestankbratwurst.fruchtjobs.jobs.potioncrafting;

import com.gestankbratwurst.fruchtjobs.jobs.potioncrafting.recipes.TestRecipe;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 07.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PotionRecipeManager {

  protected PotionRecipeManager() {
    this.recipeList = new ArrayList<>();
    this.recipeMappings = new Object2ObjectOpenHashMap<>();
    init();
  }

  private final ArrayList<PotionRecipe> recipeList;
  private final Map<String, PotionRecipe> recipeMappings;

  public PotionRecipe getRecipeOf(String recipeName) {
    return recipeMappings.get(recipeName);
  }

  public PotionRecipe getRecipeOf(List<ItemStack> ingredients) {
    if (ingredients.size() > 5) {
      return null;
    }
    for (PotionRecipe recipe : recipeList) {
      if (recipe.isValid(ingredients)) {
        return recipe;
      }
    }
    return null;
  }

  private void init() {
    recipeList.add(new TestRecipe());
    for (PotionRecipe recipe : recipeList) {
      this.recipeMappings.put(recipe.getName(), recipe);
    }
  }

}