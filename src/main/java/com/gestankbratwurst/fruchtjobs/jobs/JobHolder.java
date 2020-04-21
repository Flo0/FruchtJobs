package com.gestankbratwurst.fruchtjobs.jobs;

import com.gestankbratwurst.fruchtcore.FruchtCore;
import com.gestankbratwurst.fruchtcore.tasks.TaskManager;
import com.gestankbratwurst.fruchtcore.util.Msg;
import com.gestankbratwurst.fruchtcore.util.actionbar.ActionBarBoard.Section;
import com.gestankbratwurst.fruchtcore.util.actionbar.ActionBarManager;
import com.gestankbratwurst.fruchtcore.util.common.UtilItem;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtcore.util.holograms.AbstractHologram;
import com.gestankbratwurst.fruchtcore.util.holograms.MovingHologram;
import com.gestankbratwurst.fruchtcore.util.holograms.impl.HologramManager;
import com.gestankbratwurst.fruchtjobs.jobs.bossbar.JobBossBarManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map.Entry;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.util.Vector;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 24.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobHolder {

  private static final Vector MOVING_UP = new Vector(0, 0.05, 0);
  private static final int TICKS_ALIVE = 25;

  public JobHolder(UUID ownerID, JobBossBarManager jobBossBarManager, JobManager jobManager) {
    this.economy = jobManager.getEconomy();
    this.taskManager = FruchtCore.getInstance().getTaskManager();
    this.jobBossBarManager = jobBossBarManager;
    this.jobManager = jobManager;
    this.actionBarManager = FruchtCore.getInstance().getUtilModule().getActionBarManager();
    this.hologramManager = FruchtCore.getInstance().getUtilModule().getHologramManager();
    this.professionMap = new EnumMap<>(JobType.class);
    this.ownerID = ownerID;
    this.appliedJobs = EnumSet.noneOf(JobType.class);
    this.jobMap = new EnumMap<>(JobType.class);
    this.jobPerks = EnumSet.noneOf(JobPerkType.class);
    this.smallCobblePouch = Bukkit.createInventory(null, 18, "Kleiner Steinbeutel");
    this.smallLogPouch = Bukkit.createInventory(null, 18, "Kleiner Holzbeutel");
    for (JobType type : JobType.values()) {
      jobMap.put(type, new LevelData());
    }
  }

  private final JobManager jobManager;
  private final TaskManager taskManager;
  private final JobBossBarManager jobBossBarManager;
  private final ActionBarManager actionBarManager;
  private final HologramManager hologramManager;
  @Getter
  private int maxJobs = 2;
  @Getter
  private final UUID ownerID;
  private final EnumMap<JobType, LevelData> jobMap;
  private final EnumMap<JobType, JobType> professionMap;
  private final EnumSet<JobType> appliedJobs;
  @Getter
  @Setter
  private boolean bossBarDisplaying;
  @Getter
  @Setter
  private boolean actionBarDisplaying;
  @Getter
  @Setter
  private boolean hologramDisplaying;
  @Getter
  private long minedStoneCount = 0L;
  private final EnumSet<JobPerkType> jobPerks;
  @Getter
  private final Inventory smallCobblePouch;
  @Getter
  private final Inventory smallLogPouch;

  private final Economy economy;

  public boolean isMaxLevel(JobType type) {
    return jobMap.get(type).getLevel() == type.getMaxLevel();
  }

  public JobType getProfession(JobType type) {
    return professionMap.get(type);
  }

  public void setProfession(JobType profession) {
    JobType mainJob = profession.getParentJob();
    this.appliedJobs.remove(mainJob);
    if (professionMap.containsKey(mainJob)) {
      JobType oldProfession = professionMap.get(mainJob);
      unApplyForJob(oldProfession);
      this.clearLevelAndExp(oldProfession);
    }
    getLevelData(profession).addExp(getLevelData(mainJob).getExp());
    professionMap.put(mainJob, profession);
    Player player = Bukkit.getPlayer(ownerID);
    if (player != null) {
      String profString = Msg.elem(profession.getDisplayName());
      String lvlString = Msg.elem("" + getLvl(profession));
      Msg.send(player, "Jobs", "Du bist nun ein " + profString + ".");
      Msg.send(player, "Jobs", "Dein Level in diesem Beruf ist " + lvlString);
      Msg.send(player, "Jobs", "Du kannst jetzt deine Perks neu verteilen.");
      UtilPlayer.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1F, .65F);
    }
  }

  public long addMinedStoneCount(int amount) {
    minedStoneCount += amount;
    return minedStoneCount;
  }

  public long[] getExpProgress(JobType type) {
    long diff = jobMap.get(type).getExpLevelDifference();
    long prog = jobMap.get(type).getExpToNextLevel();
    return new long[]{diff - prog, diff};
  }

  public void addPerkType(JobPerkType perk) {
    jobPerks.add(perk);
  }

  public int getJobSlotsLeft() {
    return maxJobs - appliedJobs.size();
  }

  public void applyForJob(JobType type) {
    appliedJobs.add(type);
  }

  public void unApplyForJob(JobType type) {
    appliedJobs.remove(type);
  }

  public int getLvl(JobType type) {
    return jobMap.get(type).getLevel();
  }

  public long getExp(JobType type) {
    return jobMap.get(type).getExp();
  }

  public double getProgress(JobType type) {
    return jobMap.get(type).getProgress();
  }

  public boolean isActive(JobType type) {
    return appliedJobs.contains(type) || professionMap.containsKey(type);
  }

  public LevelData getLevelData(JobType jobType) {
    return jobMap.get(jobType);
  }

  public void openSmallCobblePouch() {
    Player player = Bukkit.getPlayer(ownerID);
    if (player != null) {
      player.openInventory(smallCobblePouch);
    }
  }

  public void openSmallLogPouch() {
    Player player = Bukkit.getPlayer(ownerID);
    if (player != null) {
      player.openInventory(smallLogPouch);
    }
  }

  public void clearLevelAndExp(JobType type) {
    this.jobPerks.removeAll(JobPerkLib.getAllJobPerks(type));
    this.jobMap.put(type, new LevelData());
  }

  public boolean hasPerk(JobPerkType perkType) {
    return this.jobPerks.contains(perkType);
  }

  public void addExp(JobType jobType, long baseExp) {
    baseExp *= jobManager.getExpScalar();
    final long exp = baseExp;
    JobType profession = professionMap.get(jobType);
    if (profession != null) {
      jobType = profession;
    }
    final JobType type = jobType;
    if (this.isMaxLevel(type)) {
      return;
    }
    int preLvl = this.getLvl(jobType);
    this.jobMap.get(type).addExp(exp);
    int postLvl = this.getLvl(jobType);

    Player player = Bukkit.getPlayer(this.ownerID);
    if (player == null) {
      return;
    }

    economy.depositPlayer(player, exp * 0.06D);

    if (preLvl != postLvl) {
      if (postLvl != jobType.getMaxLevel()) {
        UtilPlayer.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1F, 1.65F);
        String lvlString = Msg.elem("" + postLvl);
        String jobString = Msg.elem(jobType.getDisplayName());
        Msg.send(player, "Jobs", "Du bist jetzt level " + lvlString + " in " + jobString + ".");
      } else {
        UtilPlayer.playSound(player, Sound.UI_TOAST_CHALLENGE_COMPLETE);
        String lvlString = Msg.elem("" + postLvl);
        String jobString = Msg.elem(jobType.getDisplayName());
        Msg.send(player, "Jobs", "Du bist jetzt level " + lvlString + " in " + jobString + ".");
        Msg.send(player, "Jobs", "Dies ist das maximale level für diesen Job.");
        if (jobType.getProfessions().length != 0) {
          String spezString = Msg.elem("Spezialisierung");
          Msg.send(player, "Jobs", "Du kannst dir jetzt eine " + spezString + " auswählen.");
        }
      }
    }

    if (actionBarDisplaying) {
      actionBarManager
          .getBoard(player)
          .getSection(Section.MIDDLE)
          .addTokenLayer(TICKS_ALIVE, type.toString() + "_EXP", 10, () -> "§e§l" + exp + " Exp §6" + type.getIconChar());
    }
    if (bossBarDisplaying) {
      taskManager.runBukkitSyncDelayed(() -> jobBossBarManager
          .update(ownerID, getProgress(type), "§6 " + type.getDisplayName() + ": §e§l+" + exp + " Exp §6", TICKS_ALIVE), 1L);
    }
    if (hologramDisplaying) {
      Location loc = player.getLocation().add(0, 1.5, 0);
      loc.add(loc.getDirection().normalize().multiply(2.5));
      MovingHologram moving = hologramManager.createMovingHologram(loc, MOVING_UP, TICKS_ALIVE);
      AbstractHologram hologram = moving.getHologram();
      hologram.setPlayerFilter(p -> p == player);
      hologram.appendTextLine("§e" + exp + " Exp §6§l" + type.getIconChar());
    }
  }

  public void save(JsonObject jsonObject) {
    JsonObject levelData = new JsonObject();
    for (Entry<JobType, LevelData> entry : jobMap.entrySet()) {
      JsonObject jobEntry = new JsonObject();
      jobEntry.addProperty("Exp", entry.getValue().getExp());
      jobEntry.addProperty("Lvl", entry.getValue().getLevel());
      levelData.add(entry.getKey().toString(), jobEntry);
    }
    jsonObject.add("LevelData", levelData);
    JsonArray jobArray = new JsonArray();
    for (JobType jobType : appliedJobs) {
      jobArray.add(jobType.toString());
    }
    jsonObject.add("AppliedJobs", jobArray);
    jsonObject.addProperty("MaxJobs", this.maxJobs);
    JsonObject options = new JsonObject();
    options.addProperty("UseActionBar", actionBarDisplaying);
    options.addProperty("UseBossBar", bossBarDisplaying);
    options.addProperty("UseHolograms", hologramDisplaying);
    jsonObject.add("Options", options);
    jsonObject.addProperty("MinedStoneCount", minedStoneCount);
    JsonObject proffesionJson = new JsonObject();
    for (JobType mainJob : professionMap.keySet()) {
      proffesionJson.addProperty(mainJob.toString(), professionMap.get(mainJob).toString());
    }
    jsonObject.add("Professions", proffesionJson);
    JsonArray jobPerkArray = new JsonArray();
    for (JobPerkType perk : jobPerks) {
      jobPerkArray.add(perk.toString());
    }
    jsonObject.add("JobPerks", jobPerkArray);
    jsonObject.addProperty("SmallCobblePouch", UtilItem.serialize(smallCobblePouch.getContents()));
    jsonObject.addProperty("SmallLogPouch", UtilItem.serialize(smallCobblePouch.getContents()));
  }

  public void load(JsonObject jsonObject) {
    if (jsonObject.has("LevelData")) {
      JsonObject levelData = jsonObject.get("LevelData").getAsJsonObject();
      for (JobType jobType : JobType.values()) {
        if (levelData.has(jobType.toString())) {
          JsonObject lvlJson = levelData.get(jobType.toString()).getAsJsonObject();
          LevelData jobLevel = new LevelData(lvlJson.get("Exp").getAsLong(), lvlJson.get("Lvl").getAsInt());
          this.jobMap.put(jobType, jobLevel);
        }
      }
    }
    if (jsonObject.has("AppliedJobs")) {
      JsonArray jobArray = jsonObject.get("AppliedJobs").getAsJsonArray();
      for (JsonElement element : jobArray) {
        this.appliedJobs.add(JobType.valueOf(element.getAsString()));
      }
    }
    if (jsonObject.has("MaxJobs")) {
      this.maxJobs = jsonObject.get("MaxJobs").getAsInt();
    }
    if (jsonObject.has("Options")) {
      JsonObject options = jsonObject.get("Options").getAsJsonObject();
      this.actionBarDisplaying = options.get("UseActionBar").getAsBoolean();
      this.bossBarDisplaying = options.get("UseBossBar").getAsBoolean();
      this.hologramDisplaying = options.get("UseHolograms").getAsBoolean();
    }
    if (jsonObject.has("MinedStoneCount")) {
      minedStoneCount = jsonObject.get("MinedStoneCount").getAsLong();
    }
    if (jsonObject.has("Professions")) {
      JsonObject professionsJson = jsonObject.get("Professions").getAsJsonObject();
      for (Entry<String, JsonElement> entry : professionsJson.entrySet()) {
        professionMap.put(JobType.valueOf(entry.getKey()), JobType.valueOf(entry.getValue().getAsString()));
      }
    }
    if (jsonObject.has("JobPerks")) {
      JsonArray jobPerkArray = jsonObject.get("JobPerks").getAsJsonArray();
      for (JsonElement element : jobPerkArray) {
        jobPerks.add(JobPerkType.valueOf(element.getAsString()));
      }
    }
    if (jsonObject.has("SmallCobblePouch")) {
      try {
        smallLogPouch.setContents(UtilItem.deserialize(jsonObject.get("SmallCobblePouch").getAsString()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (jsonObject.has("SmallLogPouch")) {
      try {
        smallLogPouch.setContents(UtilItem.deserialize(jsonObject.get("SmallLogPouch").getAsString()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
