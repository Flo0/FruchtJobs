package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtcore.recipes.IShapedCraftingRecipe;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 12.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class ReplaceRecipeArrow implements IShapedCraftingRecipe {

  public ReplaceRecipeArrow() {
    choices = new Char2ObjectOpenHashMap<>();
    choices.put('K', new MaterialChoice(Material.FLINT));
    choices.put('S', new MaterialChoice(Material.STICK));
    choices.put('F', new MaterialChoice(Material.FEATHER));
    arrows = ItemLibrary.FLINT_ARROW.getItem();
    arrows.setAmount(4);
  }

  private final Char2ObjectMap<RecipeChoice> choices;
  private final String[] shape = new String[]{"K", "S", "F"};
  private final ItemStack arrows;

  @Override
  public Char2ObjectMap<RecipeChoice> getIngredients() {
    return choices;
  }

  @Override
  public String[] getShape() {
    return shape;
  }

  @Override
  public ItemStack getResult() {
    return arrows.clone();
  }

  @Override
  public void onCraft(Player player, int amount) {

  }

  @Override
  public String getName() {
    return "replacearrow";
  }

  @Override
  public String[] getDescription(Player player) {
    return new String[0];
  }

  @Override
  public String getDisplayName(Player player) {
    return "§fPrimitiver Pfeil";
  }
}
