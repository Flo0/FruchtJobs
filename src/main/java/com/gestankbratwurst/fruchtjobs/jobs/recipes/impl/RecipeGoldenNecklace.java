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
 * This file is part of FruchtJobs and was created at the 12.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeGoldenNecklace extends AbstractJobShapedRecipe {

  public RecipeGoldenNecklace(JobManager jobManager) {
    super(jobManager, JobType.CRAFTER, 0, JobPerkType.GOLD_SMITH);
    shape = new String[]{"NNN", "N N", "NGN"};
    desc = new ArrayList<>();
    desc.add("§7Schmuck und Handelsware.");
    desc.add("§7Kann verzaubert werden.");
    choices = new Char2ObjectOpenHashMap<>();
    choices.put('G', new MaterialChoice(Material.GOLD_INGOT));
    choices.put('N', new MaterialChoice(Material.GOLD_NUGGET));
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
    return ItemLibrary.GOLDEN_NECKLACE.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.CRAFTER, amount * 32);
  }

  @Override
  public String getName() {
    return "goldennecklace";
  }
}
