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
 * This file is part of FruchtJobs and was created at the 29.05.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class FurnacePretzel implements IFurnaceRecipe {

  private final RecipeChoice input = new ExactChoice(ItemLibrary.UNCOOKED_PRETZEL.getItem());

  @Override
  public RecipeChoice getInput() {
    return input;
  }

  @Override
  public int getExp() {
    return 8;
  }

  @Override
  public int getCookingTime() {
    return 20;
  }

  @Override
  public boolean mirrorOnSmoker() {
    return true;
  }

  @Override
  public boolean mirrorOnBlastfurnace() {
    return false;
  }

  @Override
  public ItemStack getResult() {
    return ItemLibrary.PRETZEL.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {

  }

  @Override
  public String getName() {
    return "pretzelfurnace";
  }

  @Override
  public String[] getDescription(Player player) {
    return new String[]{"", "§7Heilt 1 Leben."};
  }

  @Override
  public String getDisplayName(Player player) {
    return getResult().getItemMeta().getDisplayName();
  }
}
