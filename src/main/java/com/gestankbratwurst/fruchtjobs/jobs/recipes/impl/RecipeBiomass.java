package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobPerkType;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.AbstractJobShapelessRecipe;
import java.util.ArrayList;
import java.util.HashSet;
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
 * This file is part of FruchtJobs and was created at the 04.05.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeBiomass extends AbstractJobShapelessRecipe {

  public RecipeBiomass(JobManager jobManager) {
    super(jobManager, JobType.FARMER, 0, JobPerkType.RECIPE_FLOUR_BIOMASS);
  }

  @Override
  protected List<String> evalDescription(Player player) {
    return new ArrayList<>();
  }

  @Override
  public ItemStack getResult() {
    return ItemLibrary.BIOMASS.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.FARMER, amount * 10);
  }

  @Override
  public String getName() {
    return "biomass";
  }

  @Override
  public List<RecipeChoice> getChoices() {
    List<RecipeChoice> choices = new ArrayList<>();
    RecipeChoice bioChoice = new MaterialChoice(
        Material.WHEAT,
        Material.BEETROOT,
        Material.CARROT,
        Material.POTATO,
        Material.POISONOUS_POTATO,
        Material.MELON_SLICE,
        Material.SUGAR_CANE,
        Material.CACTUS,
        Material.BROWN_MUSHROOM,
        Material.RED_MUSHROOM,
        Material.KELP,
        Material.NETHER_WART);
    choices.add(bioChoice.clone());
    choices.add(bioChoice.clone());
    choices.add(bioChoice.clone());
    choices.add(bioChoice.clone());
    choices.add(bioChoice.clone());
    choices.add(bioChoice.clone());
    choices.add(bioChoice.clone());
    choices.add(bioChoice.clone());
    choices.add(bioChoice.clone());
    return choices;
  }
}
