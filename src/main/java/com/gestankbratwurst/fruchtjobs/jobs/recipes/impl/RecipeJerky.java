package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtcore.recipes.IShapelessCraftingRecipe;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 13.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeJerky implements IShapelessCraftingRecipe {

  public RecipeJerky() {
    choices = new HashSet<>();
    RecipeChoice fleshChoice = new MaterialChoice(Material.ROTTEN_FLESH, Material.BEEF, Material.PORKCHOP, Material.RABBIT,
        Material.MUTTON);
    RecipeChoice saltChoice = new ExactChoice(ItemLibrary.STONE_SALT.getItem());
    choices.add(saltChoice);
    choices.add(fleshChoice);
  }

  private final Set<RecipeChoice> choices;

  @Override
  public ItemStack getResult() {
    return ItemLibrary.JERKY.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {

  }

  @Override
  public String getName() {
    return "jerky";
  }

  @Override
  public String[] getDescription(Player player) {
    return new String[0];
  }

  @Override
  public String getDisplayName(Player player) {
    return ItemLibrary.JERKY.getItem().getItemMeta().getDisplayName();
  }

  @Override
  public Set<RecipeChoice> getChoices() {
    return choices;
  }
}
