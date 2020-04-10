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
 * This file is part of FruchtJobs and was created at the 09.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeButcherKnife extends AbstractJobShapedRecipe {


  public RecipeButcherKnife(JobManager jobManager) {
    super(jobManager, JobType.FARMER, 0, JobPerkType.RECIPE_BUTCHER_KNIFE);
    this.choiceMap = new Char2ObjectOpenHashMap<>();
    choiceMap.put('I', new MaterialChoice(Material.IRON_INGOT));
    choiceMap.put('S', new MaterialChoice(Material.STICK));
    choiceMap.put('L', new MaterialChoice(Material.LEATHER));
    this.shape = new String[]{"I", "S", "L"};
    this.desc = new ArrayList<>();
    desc.add("§7Extra drops von");
    desc.add("§7Nutztieren");
  }

  private final List<String> desc;

  @Override
  protected List<String> evalDescription(Player player) {
    return desc;
  }

  private final Char2ObjectMap<RecipeChoice> choiceMap;
  private final String[] shape;

  @Override
  public Char2ObjectMap<RecipeChoice> getIngredients() {
    return choiceMap;
  }

  @Override
  public String[] getShape() {
    return shape;
  }

  @Override
  public ItemStack getResult() {
    return ItemLibrary.BUTCHER_KNIFER.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.FARMER, 12 * amount);
  }

  @Override
  public String getName() {
    return "butcherknife";
  }

}
