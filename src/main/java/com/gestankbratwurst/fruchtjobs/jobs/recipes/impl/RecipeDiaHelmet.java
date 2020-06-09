package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobPerkType;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.AbstractJobShapedRecipe;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 26.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeDiaHelmet extends AbstractJobShapedRecipe {

  public RecipeDiaHelmet(JobManager jobManager) {
    super(jobManager, JobType.CRAFTER, 13);
  }

  @Override
  protected List<String> evalDescription(Player player) {
    return Lists.newArrayList("§7Normaler Dia Helm.");
  }

  @Override
  public Char2ObjectMap<RecipeChoice> getIngredients() {
    Char2ObjectMap<RecipeChoice> choices = new Char2ObjectOpenHashMap<>();
    choices.put('D', new ExactChoice(ItemLibrary.CLEAR_DIAMOND.getItem()));
    return choices;
  }

  @Override
  public String[] getShape() {
    return new String[]{"DDD", "D D"};
  }

  @Override
  public ItemStack getResult() {
    return new ItemStack(Material.DIAMOND_HELMET);
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.CRAFTER, 50);
  }

  @Override
  public String getName() {
    return "diamondhelmet";
  }

}
