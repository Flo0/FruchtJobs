package com.gestankbratwurst.fruchtjobs.jobs;

import com.gestankbratwurst.fruchtcore.util.Msg;
import com.gestankbratwurst.fruchtcore.util.common.NameSpaceFactory;
import com.gestankbratwurst.fruchtjobs.jobs.bossbar.JobBossBarManager;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 24.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class JobListener implements Listener {

  private final JobManager jobManager;
  private final JobBossBarManager jobBossBarManager;
  private final JobExpGainManager jobExpGainManager;

  @EventHandler(priority = EventPriority.HIGH)
  public void onCraft(CraftItemEvent event) {
    if (event.isCancelled()) {
      return;
    }
    jobExpGainManager.handleEvent(event);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onJoin(PlayerJoinEvent event) {
    jobManager.login(event.getPlayer().getUniqueId());
    jobBossBarManager.login(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onQuit(PlayerQuitEvent event) {
    jobManager.logout(event.getPlayer().getUniqueId());
    jobBossBarManager.logout(event.getPlayer());
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onTame(EntityTameEvent event) {
    if (event.isCancelled()) {
      return;
    }
    jobExpGainManager.handleEvent(event);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onKill(EntityDeathEvent event) {
    if (event.isCancelled()) {
      return;
    }
    jobExpGainManager.handleEvent(event);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onFill(PlayerBucketFillEvent event) {
    if (event.isCancelled()) {
      return;
    }
    jobExpGainManager.handleEvent(event);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onConsumeMilk(PlayerItemConsumeEvent event) {
    if (event.isCancelled()) {
      return;
    }
    if (event.getItem().getType() == Material.MILK_BUCKET) {
      Player player = event.getPlayer();
      if (player.getPersistentDataContainer().has(NameSpaceFactory.provide("MILK_DRINK"), PersistentDataType.LONG)) {
        long lastTime = player.getPersistentDataContainer().get(NameSpaceFactory.provide("MILK_DRINK"), PersistentDataType.LONG);
        long delta = System.currentTimeMillis() - lastTime;
        if (delta < 10000) {
          event.setCancelled(true);
          double sec = 10D - (delta / 1000D);
          sec = ((int) (sec * 10D)) / 10D;
          String time = Msg.elem("" + sec) + "s";
          Msg.error(player, "Milch", "Du musst noch " + time + " warten, bis du wieder Milch trinken kannst.");
        } else {
          player.getPersistentDataContainer()
              .set(NameSpaceFactory.provide("MILK_DRINK"), PersistentDataType.LONG, System.currentTimeMillis());
        }
      } else {
        player.getPersistentDataContainer()
            .set(NameSpaceFactory.provide("MILK_DRINK"), PersistentDataType.LONG, System.currentTimeMillis());
      }
    }
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onShearing(PlayerShearEntityEvent event) {
    if (event.isCancelled()) {
      return;
    }
    jobExpGainManager.handleEvent(event);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onBreed(EntityBreedEvent event) {
    if (event.isCancelled()) {
      return;
    }
    jobExpGainManager.handleEvent(event);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onBreak(BlockBreakEvent event) {
    if (event.isCancelled()) {
      return;
    }
    jobExpGainManager.handleEvent(event);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onInteract(PlayerInteractEvent event) {
    if (event.isCancelled()) {
      return;
    }
    jobExpGainManager.handleEvent(event);
  }

  @EventHandler(priority = EventPriority.HIGH)
  public void onFish(PlayerFishEvent event) {
    if (event.isCancelled()) {
      return;
    }
    jobExpGainManager.handleEvent(event);
  }

}
