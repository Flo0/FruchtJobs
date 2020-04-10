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
 * This file is part of FruchtJobs and was created at the 10.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeSieve extends AbstractJobShapedRecipe {

  public RecipeSieve(JobManager jobManager) {
    super(jobManager, JobType.ADVENTURER, 0, JobPerkType.SIEVEING);
    this.choices = new Char2ObjectOpenHashMap<>();
    this.choices.put('I', new MaterialChoice(Material.IRON_INGOT));
    this.choices.put('S', new MaterialChoice(Material.STRING));
    shape = new String[]{"SSS", "I I", " I "};
    desc = new ArrayList<>();
    desc.add("§7Kann zum Schürfen in");
    desc.add("§7Flüssen verwendet werden.");
  }

  private final Char2ObjectOpenHashMap<RecipeChoice> choices;
  private final String[] shape;
  private final List<String> desc;

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
    return ItemLibrary.SIEVE.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.ADVENTURER, 20);
  }

  @Override
  public String getName() {
    return "adventuresieve";
  }
}
