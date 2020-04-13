package com.gestankbratwurst.fruchtjobs.jobs.recipes.impl;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtcore.resourcepack.skins.Model;
import com.gestankbratwurst.fruchtcore.util.items.ItemBuilder;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobPerkType;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.AbstractJobShapedRecipe;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.AbstractJobShapelessRecipe;
import it.unimi.dsi.fastutil.chars.Char2ObjectMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.ArrayList;
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
 * This file is part of FruchtJobs and was created at the 12.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class RecipeTreatedWood extends AbstractJobShapelessRecipe {

  public RecipeTreatedWood(JobManager jobManager) {
    super(jobManager, JobType.CRAFTER, 0, JobPerkType.PREPARATIONS);
    desc = new ArrayList<>();
    desc.add("§7Material für weitere Rezepte.");
    choices = new ObjectOpenHashSet<>();
    choices.add(new MaterialChoice(Material.STICK));
    choices.add(new MaterialChoice(Material.POTION));
    choices.add(new MaterialChoice(Material.CLAY_BALL));
  }

  private final ArrayList<String> desc;
  private final ObjectSet<RecipeChoice> choices;

  @Override
  protected List<String> evalDescription(Player player) {
    return desc;
  }

  @Override
  public ItemStack getResult() {
    return ItemLibrary.TREATED_STICK.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    jobManager.getOnlineHolder(player).addExp(JobType.CRAFTER, amount * 6);
  }

  @Override
  public String getName() {
    return "treatedwood";
  }

  @Override
  public Set<RecipeChoice> getChoices() {
    return choices;
  }

}
