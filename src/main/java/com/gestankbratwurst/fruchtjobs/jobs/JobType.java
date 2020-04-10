package com.gestankbratwurst.fruchtjobs.jobs;

import com.gestankbratwurst.fruchtcore.util.items.ItemBuilder;
import com.gestankbratwurst.fruchtjobs.util.UtilMath;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

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
public enum JobType {

  MINE_WORKER(false, "Minenarbeiter", Material.IRON_PICKAXE, '⛏', new String[]{},
      new JobType[]{}, 100),
  FOREST_WORKER(false, "Waldsammler", Material.IRON_AXE, '⛏', new String[]{},
      new JobType[]{}, 100),
  AGRICULTURAL_ECONOMIST(false, "Agrarökonom", Material.IRON_HOE, '⛏', new String[]{},
      new JobType[]{}, 100),
  SHEPERD_BREEDER(false, "Schäfer und Züchter", Material.SHEARS, '⛏', new String[]{},
      new JobType[]{}, 100),
  MONSTER_HUNTER(false, "Monster Jäger", Material.IRON_SWORD, '⛏', new String[]{},
      new JobType[]{}, 100),
  HUNTER_AND_SHEPERD(false, "Jäger und Hirte", Material.LEAD, '⛏', new String[]{},
      new JobType[]{JobType.SHEPERD_BREEDER, JobType.MONSTER_HUNTER}, 100),
  ARCHAEOLOGIST(false, "Archäologe", Material.BONE, '⛏', new String[]{},
      new JobType[]{}, 100),
  DEEP_SEA_FISHER(false, "Tiefseefischer", Material.HEART_OF_THE_SEA, '⛏', new String[]{},
      new JobType[]{}, 100),
  MARINE_BIOLOGIST(false, "Meeresbiologe", Material.HORN_CORAL, '⛏', new String[]{},
      new JobType[]{}, 100),
  SMITH(false, "Schmied", Material.ANVIL, '⛏', new String[]{},
      new JobType[]{}, 100),
  CRAFTING_MASTER(false, "Handwerksmeister", Material.FLETCHING_TABLE, '⛏', new String[]{},
      new JobType[]{}, 100),
  WITCHER(false, "Hexer", Material.ENCHANTED_BOOK, '⛏', new String[]{},
      new JobType[]{}, 100),
  DRUID(false, "Druide", Material.CAULDRON, '⛏', new String[]{},
      new JobType[]{}, 100),
  GATHERER(true, "Sammler", Material.STONE_PICKAXE, '⛏', new String[]{},
      new JobType[]{JobType.MINE_WORKER, JobType.FOREST_WORKER}, 20),
  FARMER(true, "Farmer", Material.STONE_HOE, '☘', new String[]{},
      new JobType[]{JobType.AGRICULTURAL_ECONOMIST, JobType.HUNTER_AND_SHEPERD}, 16),
  ADVENTURER(true, "Abenteurer", Material.MAP, '⚳', new String[]{},
      new JobType[]{JobType.ARCHAEOLOGIST}, 20),
  CRAFTER(true, "Handwerker", Material.CRAFTING_TABLE, '⚒', new String[]{},
      new JobType[]{JobType.SMITH, JobType.CRAFTING_MASTER}, 20),
  FISHER(true, "Fischer", Material.FISHING_ROD, '⛏', new String[]{},
      new JobType[]{JobType.MARINE_BIOLOGIST, JobType.DEEP_SEA_FISHER}, 20),
  ALCHEMIST(true, "Alchemist", Material.GREEN_DYE, '⚒', new String[]{},
      new JobType[]{JobType.WITCHER, JobType.DRUID}, 20);

  @Getter
  private final boolean mainJob;
  @Getter
  private final String displayName;
  private final Material iconMaterial;
  @Getter
  private final char iconChar;
  private final String[] description;
  @Getter
  private final JobType[] professions;
  @Getter
  private final int maxLevel;

  public JobType getParentJob() {
    JobType parent = this;
    switch (this) {
      case MINE_WORKER:
      case FOREST_WORKER:
      case FISHER:
        parent = GATHERER;
        break;
      case AGRICULTURAL_ECONOMIST:
      case HUNTER_AND_SHEPERD:
        parent = FARMER;
        break;
      case MONSTER_HUNTER:
      case SHEPERD_BREEDER:
        parent = HUNTER_AND_SHEPERD;
        break;
      case ARCHAEOLOGIST:
      case MARINE_BIOLOGIST:
        parent = ADVENTURER;
        break;
      case SMITH:
      case CRAFTING_MASTER:
        parent = CRAFTER;
        break;
      case WITCHER:
      case DRUID:
        parent = ALCHEMIST;
        break;
    }
    return parent;
  }

  public ItemStack getMainIcon(JobHolder jobHolder, boolean asProfession) {
    ItemBuilder builder = new ItemBuilder(iconMaterial);

    builder.name("§6" + this.displayName + " §e[§f" + jobHolder.getLvl(this) + "§e]");
    builder.lore("");
    builder.lore("§eAktiv: " + (jobHolder.isActive(this) ? "§a✔" : "§c✖"));
    builder.lore("");
    long[] progressExp = jobHolder.getExpProgress(this);
    double percent = jobHolder.getProgress(this) * 100D;
    percent = (int) (percent * 100);
    percent /= 100D;
    builder
        .lore("§eFortschritt: §a" + progressExp[0] + "§2/§a" + progressExp[1] + "   §e[§f" + percent + "%§e]");
    builder.lore(UtilMath.getProgressBar(jobHolder.getProgress(this), 25, "⏹"));
    builder.lore("");
    builder.lore("§eExp Total: §f" + jobHolder.getExp(this));
    builder.lore("");
    builder.lore(this.description);
    if (this.professions.length != 0) {
      builder.lore("§6Spezialisierungen: §7[Ab lvl " + this.maxLevel + "]");
      for (JobType subJob : this.professions) {
        builder.lore(" §e- " + subJob.getDisplayName());
        if (subJob.professions.length != 0) {
          for (JobType subSubType : subJob.professions) {
            builder.lore("   §7- " + subSubType.getDisplayName());
          }
        }
      }
    }
    builder.lore("");
    if (asProfession) {
      builder.lore("§6[§eKlicke zum Auswählen§6]");
    } else {
      builder.lore("§6[§eKlicke für Infos§6]");
    }

    builder.flag(ItemFlag.HIDE_ATTRIBUTES);

    return builder.build();
  }

  public ItemStack getMainButtonIcon(JobHolder jobHolder) {
    boolean hasChosen = jobHolder.isActive(this);
    int maxJobs = jobHolder.getMaxJobs();
    int jobsLeft = jobHolder.getJobSlotsLeft();
    ItemBuilder builder = new ItemBuilder(hasChosen ? Material.GREEN_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE);
    builder.name(hasChosen ? "§aJob ist aktiv" : (jobsLeft == 0 ? "§cKeine Jobs mehr wählbar" : "§aJob aktivieren"));
    builder.lore("");
    builder.lore("§fJobs frei: §a" + jobsLeft + "§2/§a" + maxJobs);
    builder.lore("");
    builder.lore("§6[§eKlicke zum " + (hasChosen ? "deaktivieren" : "aktivieren") + "§6]");
    return builder.build();
  }

}