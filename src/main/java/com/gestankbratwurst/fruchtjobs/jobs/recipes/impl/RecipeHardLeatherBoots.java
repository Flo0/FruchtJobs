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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.ExactChoice;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 11.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeHardLeatherBoots extends AbstractJobShapedRecipe {

  public RecipeHardLeatherBoots(JobManager jobManager) {
    super(jobManager, JobType.CRAFTER, 0, JobPerkType.RECIPES_HARD_LEATHER);
    shape = new String[]{"H H", "H H"};
    desc = new ArrayList<>();
    desc.add("§7Besser als Eisen.");
    choices = new Char2ObjectOpenHashMap<>();
    choices.put('H', new ExactChoice(ItemLibrary.HARD_LEATHER.getItem()));
  }

  private final ArrayList<String> desc;
  private final Char2ObjectMap<RecipeChoice> choices;
  private final String[] shape;

  @Override
  protected List<String> evalDescription(Player player) {
    return desc;
  }

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
    return ItemLibrary.HARD_LEATHER_BOOTS.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.CRAFTER, amount * 40);
  }

  @Override
  public String getName() {
    return "hardleatherboots";
  }

}