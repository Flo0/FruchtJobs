package com.gestankbratwurst.fruchtjobs.jobs.potioncrafting;

import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import java.util.List;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 07.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public interface PotionRecipe {

  int getBrewTime();

  boolean canCraft(JobHolder holder);

  boolean isValid(List<ItemStack> ingredients);

  ItemStack getResult();

  int getbaseExp();

  String getName();

  void onFinish(PotionCauldron cauldron);

}