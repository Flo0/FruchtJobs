package com.gestankbratwurst.fruchtjobs.jobs.sieving;


import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

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
public class SieveListener implements Listener {

  private final SieveManager sieveManager;

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    sieveManager.handleEvent(event);
  }

  @EventHandler
  public void onClose(InventoryCloseEvent event) {
    sieveManager.handleEvent(event);
  }

}
