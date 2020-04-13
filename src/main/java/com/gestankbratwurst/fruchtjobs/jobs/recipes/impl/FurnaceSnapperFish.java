package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtcore.recipes.IFurnaceRecipe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 13.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class FurnaceSnapperFish implements IFurnaceRecipe {

  @Override
  public RecipeChoice getInput() {
    return new ExactChoice(ItemLibrary.SNAPPER.getItem());
  }

  @Override
  public int getExp() {
    return 1;
  }

  @Override
  public int getCookingTime() {
    return 200;
  }

  @Override
  public ItemStack getResult() {
    return ItemLibrary.COOKED_SNAPPER.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {

  }

  @Override
  public String getName() {
    return "furnacesnapperfish";
  }

  @Override
  public String[] getDescription(Player player) {
    return new String[0];
  }

  @Override
  public String getDisplayName(Player player) {
    return ItemLibrary.COOKED_SNAPPER.getItem().getItemMeta().getDisplayName();
  }
}
