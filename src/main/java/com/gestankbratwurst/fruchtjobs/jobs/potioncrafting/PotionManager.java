package com.gestankbratwurst.fruchtjobs.jobs.potioncrafting;

import com.gestankbratwurst.fruchtcore.FruchtCore;
import com.gestankbratwurst.fruchtcore.util.common.NameSpaceFactory;
import com.gestankbratwurst.fruchtcore.util.holograms.AbstractHologram;
import com.gestankbratwurst.fruchtcore.util.holograms.impl.HologramManager;
import com.gestankbratwurst.fruchtjobs.FruchtJobs;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Campfire;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 07.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PotionManager implements Runnable {

  public PotionManager(FruchtJobs plugin) {
    this.gson = new GsonBuilder().disableHtmlEscaping().create();
    this.jobManager = plugin.getJobManager();
    this.potionRecipeManager = new PotionRecipeManager();
    this.loadedCauldrons = new Object2ObjectOpenHashMap<>();
    this.hologramManager = FruchtCore.getInstance().getUtilModule().getHologramManager();
    for (World world : Bukkit.getWorlds()) {
      for (Chunk chunk : world.getLoadedChunks()) {
        this.loadCauldrons(chunk);
      }
    }
    Bukkit.getPluginManager()
        .registerEvents(new PotionListener(plugin.getJobManager(), potionRecipeManager, this), plugin);
    Bukkit.getScheduler().runTaskTimer(plugin, this, 1L, 1L);
  }

  private final Gson gson;
  private final JobManager jobManager;
  private final HologramManager hologramManager;
  private final PotionRecipeManager potionRecipeManager;
  private final Map<Location, PotionCauldron> loadedCauldrons;

  protected void handleInteract(PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    if (block == null) {
      return;
    }
    if (block.getType() != Material.CAULDRON) {
      return;
    }
    PotionCauldron cauldron = loadedCauldrons.get(block.getLocation());
    if (cauldron == null) {
      return;
    }
    cauldron.onInteract(event);
  }

  protected void unloadCauldrons(Chunk chunk) {
    for (BlockState state : chunk.getTileEntities()) {
      if (state.getType() == Material.CAMPFIRE) {
        PotionCauldron cauldron = loadedCauldrons.get(state.getLocation().add(0, 1, 0));
        if (cauldron != null) {
          Campfire campfire = (Campfire) state;
          JsonObject jsonObject = new JsonObject();
          cauldron.save(jsonObject);
          campfire.getPersistentDataContainer()
              .set(NameSpaceFactory.provide("CAULDRON_DATA"), PersistentDataType.STRING, gson.toJson(jsonObject));
          campfire.update();
        }
      }
    }
  }

  protected void loadCauldrons(Chunk chunk) {
    for (BlockState state : chunk.getTileEntities()) {
      if (state.getType() == Material.CAMPFIRE) {
        Campfire campfire = (Campfire) state;
        PersistentDataContainer pdc = ((Campfire) state).getPersistentDataContainer();
        if (pdc.has(NameSpaceFactory.provide("CAULDRON_DATA"), PersistentDataType.STRING)) {
          String data = pdc.get(NameSpaceFactory.provide("CAULDRON_DATA"), PersistentDataType.STRING);
          createCauldron(campfire.getLocation().add(0, 1, 0), gson.fromJson(data, JsonObject.class));
        }
      }
    }
  }

  protected void removeCauldron(Location location) {
    PotionCauldron cauldron = loadedCauldrons.remove(location);
    if (cauldron != null) {
      cauldron.getCauldronHologram().delete();
    }
  }

  protected void createCauldron(Location location, JsonObject data) {
    AbstractHologram holo = hologramManager.createHologram(location.clone().add(0.5, 1.75, 0.5));
    PotionCauldron cauldron = new PotionCauldron(jobManager, potionRecipeManager, holo, location);
    loadedCauldrons.put(location, cauldron);
    if (data != null) {
      cauldron.load(data);
    }
  }

  @Override
  public void run() {
    for (PotionCauldron cauldron : this.loadedCauldrons.values()) {
      cauldron.onTick();
    }
  }

}
