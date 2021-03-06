package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.AbstractJobShapelessRecipe;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
public class RecipeBreadDought extends AbstractJobShapelessRecipe {

  public RecipeBreadDought(JobManager jobManager) {
    super(jobManager, JobType.FARMER, 8);
  }

  @Override
  protected List<String> evalDescription(Player player) {
    return new ArrayList<>();
  }

  @Override
  public ItemStack getResult() {
    return ItemLibrary.UNCOOKED_BREAD_BUN.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {

  }

  @Override
  public String getName() {
    return "breadbundough";
  }

  @Override
  public List<RecipeChoice> getChoices() {
    List<RecipeChoice> choices = new ArrayList<>();
    choices.add(new ExactChoice(ItemLibrary.DOUGH.getItem()));
    choices.add(new ExactChoice(ItemLibrary.DOUGH.getItem()));
    return choices;
  }

}
