package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.AbstractJobShapelessRecipe;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 26.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeClearDiamond extends AbstractJobShapelessRecipe {

  public RecipeClearDiamond(JobManager jobManager) {
    super(jobManager, JobType.CRAFTER, 13);
  }

  @Override
  protected List<String> evalDescription(Player player) {
    return Lists.newArrayList("§7Wird zur Herstellung von", "§7normalen Diamant-", "§7Rüstungen verwendet");
  }

  @Override
  public ItemStack getResult() {
    return ItemLibrary.CLEAR_DIAMOND.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.CRAFTER, 8);
  }

  @Override
  public String getName() {
    return "cleardiamond";
  }

  @Override
  public List<RecipeChoice> getChoices() {
    return Lists.newArrayList(new MaterialChoice(Material.DIAMOND), new MaterialChoice(Material.GLOWSTONE_DUST));
  }

}