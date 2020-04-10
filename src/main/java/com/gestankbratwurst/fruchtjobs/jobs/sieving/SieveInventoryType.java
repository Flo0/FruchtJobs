package com.gestankbratwurst.fruchtjobs.jobs.sieving;

import com.gestankbratwurst.fruchtcore.resourcepack.skins.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 10.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum SieveInventoryType {

  SAND(Model.SIEVE_SAND.getItem()),
  GRAVEL(Model.SIEVE_GRAVEL.getItem());

  @Getter
  private final ItemStack defaultSieveItem;

}
