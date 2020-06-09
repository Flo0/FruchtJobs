package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobPerkType;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.AbstractJobShapedRecipe;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 29.05.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeFlour extends AbstractJobShapedRecipe {

  public RecipeFlour(JobManager jobManager) {
    super(jobManager, JobType.FARMER, 0, JobPerkType.RECIPE_FLOUR_BIOMASS);
  }

  @Override
  protected List<String> evalDescription(Player player) {
    return new ArrayList<>();
  }

  @Override
  public Char2ObjectMap<RecipeChoice> getIngredients() {
    Char2ObjectMap<RecipeChoice> choices = new Char2ObjectOpenHashMap<>();
    choices.put('W', new MaterialChoice(Material.WHEAT));
    return choices;
  }

  @Override
  public String[] getShape() {
    return new String[]{"WWW"};
  }

  @Override
  public ItemStack getResult() {
    return ItemLibrary.FLOUR.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.FARMER, amount * 5);
  }

  @Override
  public String getName() {
    return "flour";
  }
}
