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
public class RecipeIronArrowHead extends AbstractJobShapedRecipe {

  public RecipeIronArrowHead(JobManager jobManager) {
    super(jobManager, JobType.CRAFTER, 0, JobPerkType.PREPARATIONS);
    shape = new String[]{"I", "I"};
    desc = new ArrayList<>();
    choices = new Char2ObjectOpenHashMap<>();
    choices.put('I', new MaterialChoice(Material.IRON_INGOT));
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
    ItemStack item = ItemLibrary.IRON_ARROW_HEAD.getItem();
    item.setAmount(4);
    return item;
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.CRAFTER, amount * 12);
  }

  @Override
  public String getName() {
    return "ironarrowhead";
  }
}
