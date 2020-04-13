package com.gestankbratwurst.fruchtjobs.jobs.recipes;

import com.gestankbratwurst.fruchtcore.recipes.IPlayerConditional;
import com.gestankbratwurst.fruchtcore.recipes.IShapelessCraftingRecipe;
import com.gestankbratwurst.fruchtcore.util.Msg;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import com.gestankbratwurst.fruchtjobs.jobs.JobManager;
import com.gestankbratwurst.fruchtjobs.jobs.JobPerkType;
import com.gestankbratwurst.fruchtjobs.jobs.JobType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 12.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public abstract class AbstractJobShapelessRecipe implements IShapelessCraftingRecipe, IPlayerConditional {

  public AbstractJobShapelessRecipe(JobManager jobManager, JobType jobType, int minLevel, JobPerkType... perks) {
    this.jobManager = jobManager;
    this.jobType = jobType;
    this.minLevel = minLevel;
    this.perks = perks;
  }

  protected final JobManager jobManager;
  private final JobType jobType;
  private final int minLevel;
  private final JobPerkType[] perks;

  @Override
  public boolean canExecute(Player player) {
    JobHolder holder = jobManager.getOnlineHolder(player);
    if (!holder.isActive(jobType)) {
      return false;
    }
    if (holder.getLvl(jobType) < minLevel) {
      return false;
    }
    for (JobPerkType perkType : perks) {
      if (!holder.hasPerk(perkType)) {
        return false;
      }
    }
    return check(player);
  }

  @Override
  public void onFailure(Player player) {
    String jobString = Msg.elem(jobType.getDisplayName());
    String lvlString = Msg.elem("" + minLevel);
    if (minLevel > 0) {
      Msg.error(player, "Jobs", "Du benötigst mind. lvl " + lvlString + " in " + jobString);
    }
    if (perks.length > 0) {
      if (perks.length == 1) {
        Msg.error(player, "Jobs", "Du benötigst den Perk: " + Msg.elem(perks[0].getDisplayName()));
      } else {
        Msg.error(player, "Jobs", "Du benötigst folgende Perks:");
        for (JobPerkType perkType : perks) {
          player.sendMessage("   - " + perkType.getDisplayName());
        }
      }
    }
    UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.5F, 0.35F);
  }

  @Override
  public String[] getDescription(Player player) {
    List<String> desc = new ArrayList<>(evalDescription(player));
    JobHolder holder = jobManager.getOnlineHolder(player);
    desc.add("");
    if (minLevel > 0) {
      desc.add("§eJob: " + (holder.isActive(jobType) ? "§a" : "§c") + jobType.getDisplayName());
      desc.add("§eLvl: " + (holder.getLvl(jobType) >= minLevel ? "§a" : "§c") + minLevel);
    }
    if (perks.length > 1) {
      desc.add("§ePerks:");
      for (JobPerkType perkType : perks) {
        desc.add("§f  -" + (holder.hasPerk(perkType) ? "§a" : "§c") + perkType.getDisplayName());
      }
    } else if (perks.length == 1) {
      desc.add("§ePerk: " + (holder.hasPerk(perks[0]) ? "§a" : "§c") + perks[0].getDisplayName());
    }
    return desc.toArray(new String[0]);
  }

  @Override
  public String getDisplayName(Player player) {
    ItemStack result = this.getResult();
    if (result.hasItemMeta()) {
      return result.getItemMeta().getDisplayName();
    } else {
      return result.getI18NDisplayName();
    }
  }

  protected boolean check(Player player) {
    return true;
  }

  protected abstract List<String> evalDescription(Player player);

}
