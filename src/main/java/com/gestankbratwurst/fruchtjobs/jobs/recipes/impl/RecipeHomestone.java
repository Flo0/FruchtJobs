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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.server.v1_15_R1.Item;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;
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
public class RecipeHomestone extends AbstractJobShapedRecipe {

  public RecipeHomestone(JobManager jobManager) {
    super(jobManager, JobType.ADVENTURER, 0, JobPerkType.RECIPE_HOME_STONE);

    choiceMap = new Char2ObjectOpenHashMap<>();
    choiceMap.put('D', new MaterialChoice(Material.DIAMOND));
    choiceMap.put('B', new MaterialChoice(Material.STONE_BRICKS));
    choiceMap.put('W', new MaterialChoice(Arrays.stream(Material.values())
        .filter(m -> m.toString().contains("WOOL") && !m.toString().contains("LEGACY"))
        .collect(Collectors.toList())));

    shape = new String[]{"WBW", "BDB", "WBW"};

    lines = new ArrayList<>();
    lines.add("§7Teleportiert dich nach hause.");
  }

  private final List<String> lines;
  private final Char2ObjectMap<RecipeChoice> choiceMap;
  private final String[] shape;

  @Override
  protected List<String> evalDescription(Player player) {
    return lines;
  }

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
    NBTItem nbt = new NBTItem(ItemLibrary.HOME_STONE.getItem());
    nbt.setString("STACKBLOCK", UUID.randomUUID().toString());
    return nbt.getItem();
  }

  @Override
  public void onCraft(Player player, int amount) {
    System.out.println("CRAFT");
    jobManager.getOnlineHolder(player).addExp(JobType.ADVENTURER, 24 * amount);
  }

  @Override
  public String getName() {
    return "homestone";
  }
}
