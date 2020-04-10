package com.gestankbratwurst.fruchtjobs.jobs.potioncrafting;

import com.gestankbratwurst.fruchtcore.util.Msg;
import com.gestankbratwurst.fruchtcore.util.common.UtilItem;
import com.gestankbratwurst.fruchtcore.util.common.UtilMath;
import com.gestankbratwurst.fruchtcore.util.common.UtilText;
import com.gestankbratwurst.fruchtcore.util.holograms.AbstractHologram;
import com.gestankbratwurst.fruchtcore.util.holograms.impl.HologramTextLine;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 07.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class PotionCauldron {

  private static final int CAULDRON_MAX_SIZE = 5;

  protected PotionCauldron(JobManager jobManager, PotionRecipeManager potionRecipeManager, AbstractHologram cauldronHologram,
      Location location) {
    this.random = ThreadLocalRandom.current();
    this.jobManager = jobManager;
    this.potionRecipeManager = potionRecipeManager;
    this.cauldronHologram = cauldronHologram;
    this.items = new ArrayList<>(5);
    this.location = location;
    this.isBrewing = false;
    cauldronHologram.appendTextLine("");
    cauldronHologram.appendTextLine("");
    updateHologramIngredients();
  }

  private final PotionRecipeManager potionRecipeManager;
  private final ThreadLocalRandom random;
  private final JobManager jobManager;
  private final Location location;
  @Getter(AccessLevel.PACKAGE)
  private final AbstractHologram cauldronHologram;
  private final List<ItemStack> items;
  private boolean isBrewing;
  private long ticksBrewing;
  private PotionRecipe currentRecipe;
  private UUID playerBrewingID;

  private void updateHologramIngredients() {
    StringBuilder builder = new StringBuilder();
    char empty = UtilText.getUnicodeChar(93, 5, 15);
    char full;
    if (isBrewing) {
      full = UtilText.getUnicodeChar(93, 5, 14);
    } else {
      full = UtilText.getUnicodeChar(93, 5, 13);
    }
    int e = 5;
    for (int i = 0; i < items.size(); i++) {
      builder.append(full);
      e--;
    }
    builder.append(String.valueOf(empty).repeat(Math.max(0, e)));
    ((HologramTextLine) cauldronHologram.getHologramLine(0)).update(builder.toString());
  }

  private void updateHologramBar() {
    String line = "";
    if (isBrewing) {
      line = UtilMath.getPercentageBar(ticksBrewing, currentRecipe.getBrewTime(), 10, "▄");
    }
    ((HologramTextLine) cauldronHologram.getHologramLine(1))
        .update(line);
  }

  protected void onInteract(PlayerInteractEvent event) {
    if (event.getHand() != EquipmentSlot.HAND) {
      return;
    }
    Player player = event.getPlayer();
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
      return;
    }
    event.setCancelled(true);
    if (player.isSneaking()) {
      if (isBrewing) {
        Msg.error(player, "Alchemie", "Es ist ein Brau-Prozess im Gang.");
        Msg.error(player, "Alchemie", "Items können nicht entnommen werden.");
        return;
      } else {
        throwOut();
        return;
      }
    }
    ItemStack handItem = event.getItem();
    if (handItem == null || handItem.getType() == Material.AIR) {
      Msg.send(player, "Alchemie", "Du brauchst ein Item in deiner Hand.");
      return;
    }
    if (items.size() == CAULDRON_MAX_SIZE) {
      Msg.send(player, "Alchemie", "Dieser Kessel ist voll.");
      Msg.send(player, "Alchemie", "Schleichen + Rechtsklick wirft alle Items aus dem Kessel.");
      return;
    }

    int amount = handItem.getAmount();
    items.add(handItem.asOne());
    if (amount == 1) {
      player.getInventory().setItemInMainHand(null);
    } else {
      handItem.subtract(1);
    }
    updateHologramIngredients();

    PotionRecipe recipe = potionRecipeManager.getRecipeOf(items);
    location.getWorld().playSound(location, Sound.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, 0.8F, 1.5F);
    location.getWorld().playSound(location, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 0.8F, 1.5F);

    if (recipe == null) {
      return;
    }

    JobHolder holder = jobManager.getOnlineHolder(player);
    if (!recipe.canCraft(holder)) {
      Msg.send(player, "Alchemie", "Du kannst dieses Rezept nicht verwenden.");
      this.throwOut();
      return;
    }

    this.currentRecipe = recipe;
    this.startBrewing(player.getUniqueId());
  }

  private void throwOut() {
    Location dropLoc = location.clone().add(0.5, 0.5, 0.5);
    for (ItemStack item : items) {
      dropLoc.getWorld().dropItemNaturally(dropLoc, item);
    }
    items.clear();
    updateHologramIngredients();
  }

  protected void startBrewing(UUID playerBrewingID) {
    this.playerBrewingID = playerBrewingID;
    isBrewing = true;
    ticksBrewing = 0L;
    updateHologramIngredients();
    updateHologramBar();
  }

  private void finishBrewing() {
    Player player = playerBrewingID == null ? null : Bukkit.getPlayer(playerBrewingID);
    if (player != null) {
      jobManager.getOnlineHolder(player).addExp(JobType.ALCHEMIST, currentRecipe.getbaseExp());
    }

    items.clear();
    Location dropLoc = this.location.clone().add(0.5, 0.5, 0.5);
    Item drop = dropLoc.getWorld().dropItemNaturally(dropLoc, currentRecipe.getResult());
    drop.setVelocity(new Vector(0, 0.2, 0));
    dropLoc.getWorld().playSound(dropLoc, Sound.BLOCK_BREWING_STAND_BREW, 0.9F, 1.5F);
    dropLoc.getWorld().playSound(dropLoc, Sound.ENTITY_FIREWORK_ROCKET_BLAST_FAR, 0.8F, 0.9F);
    dropLoc.getWorld().spawnParticle(Particle.SMOKE_LARGE, dropLoc, 4, 0, 0, 0, 0.1);
    dropLoc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, dropLoc, 3, 0.1, 0.1, 0.1, 0.1);
    currentRecipe.onFinish(this);

    this.playerBrewingID = null;
    isBrewing = false;
    currentRecipe = null;
    ticksBrewing = 0L;
    updateHologramBar();
    updateHologramIngredients();
  }

  protected void onTick() {
    if (isBrewing) {
      if (ticksBrewing++ % 10 == 0) {
        updateHologramBar();
        location.getWorld().playSound(location, Sound.BLOCK_BUBBLE_COLUMN_UPWARDS_AMBIENT, 0.8F, 0.5F);
        location.getWorld().playSound(location, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 0.8F, 0.5F);
        float pitch = (float) random.nextDouble(0.6, 1.2);
        location.getWorld().playSound(location, Sound.BLOCK_BUBBLE_COLUMN_BUBBLE_POP, 0.65F, pitch);
      }
      if (ticksBrewing % 5 == 0) {
        Location partLoc = this.location.clone().add(0.5, 0.75, 0.5);
        partLoc.getWorld().spawnParticle(Particle.CLOUD,
            partLoc, random.nextInt(2, 5), 0.4, 0.15, 0.4, 0.05);
      }
      if (ticksBrewing == currentRecipe.getBrewTime()) {
        finishBrewing();
      }
    }
  }

  protected void load(JsonObject data) {
    this.isBrewing = data.get("IsBrewing").getAsBoolean();
    this.ticksBrewing = data.get("TicksBrewing").getAsInt();
    this.currentRecipe = potionRecipeManager.getRecipeOf(data.get("CurrentRecipe").getAsString());
    String idString = data.get("CurrentID").getAsString();
    this.playerBrewingID = idString.equals("NONE") ? null : UUID.fromString(idString);
    try {
      this.items.addAll(Arrays.asList(UtilItem.deserialize(data.get("Items").getAsString())));
    } catch (IOException e) {
      e.printStackTrace();
    }
    updateHologramIngredients();
    updateHologramBar();
  }

  protected void save(JsonObject data) {
    data.addProperty("IsBrewing", isBrewing);
    data.addProperty("TicksBrewing", ticksBrewing);
    data.addProperty("CurrentRecipe", currentRecipe == null ? "NONE" : currentRecipe.getName());
    data.addProperty("CurrentID", playerBrewingID == null ? "NONE" : playerBrewingID.toString());
    String itemData = UtilItem.serialize(items.toArray(new ItemStack[0]));
    data.addProperty("Items", itemData);
    this.cauldronHologram.delete();
  }

}
