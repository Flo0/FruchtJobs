package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtcore.util.nbtapi.NBTItem;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobPerkType;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.AbstractJobShapedRecipe;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.RecipeChoice.MaterialChoice;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 08.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeCobblePouch extends AbstractJobShapedRecipe {

  public RecipeCobblePouch(JobManager jobManager) {
    super(jobManager, JobType.GATHERER, 0, JobPerkType.RECIPE_SMALL_STONE_POUCH);
    this.choices = new Char2ObjectOpenHashMap<>();
    choices.put('L', new MaterialChoice(Material.LEATHER));
    choices.put('I', new MaterialChoice(Material.IRON_INGOT));
    choices.put('S', new MaterialChoice(Material.STRING));
    choices.put('C', new MaterialChoice(Material.COBBLESTONE));
    shape = new String[]{"SIS", "LCL", "LLL"};
    this.desc = new ArrayList<>();
    desc.add("§7Sammelt automatisch Stein");
    desc.add("§7auf, bis er voll ist.");
  }

  private final Char2ObjectMap<RecipeChoice> choices;
  private final String[] shape;
  private final List<String> desc;

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
    NBTItem nbt = new NBTItem(ItemLibrary.COBBLE_POUCH.getItem());
    nbt.setString("STACK", UUID.randomUUID().toString());
    return nbt.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.GATHERER, 30 * amount);
  }

  @Override
  public String getName() {
    return "cobblepouchsmall";
  }

  @Override
  protected List<String> evalDescription(Player player) {
    return desc;
  }

}
