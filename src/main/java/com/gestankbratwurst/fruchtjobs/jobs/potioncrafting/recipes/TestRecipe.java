package com.gestankbratwurst.fruchtjobs.jobs.potioncrafting.recipes;

import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import com.gestankbratwurst.fruchtjobs.jobs.potioncrafting.PotionCauldron;
import com.gestankbratwurst.fruchtjobs.jobs.potioncrafting.PotionRecipe;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 07.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class TestRecipe implements PotionRecipe {

  @Override
  public int getBrewTime() {
    return 200;
  }

  @Override
  public boolean canCraft(JobHolder holder) {
    return true;
  }

  @Override
  public boolean isValid(List<ItemStack> ingredients) {
    if (ingredients.size() != 3) {
      return false;
    }
    return ingredients.get(0).getType() == Material.POTION &&
        ingredients.get(1).getType() == Material.NETHER_WART &&
        ingredients.get(2).getType() == Material.GHAST_TEAR;
  }

  @Override
  public ItemStack getResult() {
    ItemStack result = new ItemStack(Material.POTION);
    PotionMeta meta = (PotionMeta) result.getItemMeta();
    PotionData baseData = new PotionData(PotionType.REGEN, false, false);
    meta.setBasePotionData(baseData);
    result.setItemMeta(meta);
    return result;
  }

  @Override
  public int getbaseExp() {
    return 20;
  }

  @Override
  public String getName() {
    return "Regen potion";
  }

  @Override
  public void onFinish(PotionCauldron cauldron) {

  }

}
