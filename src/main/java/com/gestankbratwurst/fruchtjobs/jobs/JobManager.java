package com.gestankbratwurst.fruchtjobs.jobs;

import com.gestankbratwurst.fruchtjobs.FruchtJobs;
import com.gestankbratwurst.fruchtjobs.io.IOManager;
import com.gestankbratwurst.fruchtjobs.jobs.bossbar.JobBossBarManager;
import com.gestankbratwurst.fruchtjobs.jobs.guis.JobMainProvider;
import com.gestankbratwurst.fruchtjobs.jobs.guis.JobOptionsProvider;
import com.gestankbratwurst.fruchtjobs.jobs.guis.JobPerkProvider;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import net.crytec.inventoryapi.SmartInventory;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 24.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobManager {

  private static final Set<Biome> FOREST_BIOMES = ImmutableSet.<Biome>builder()
      .add(Biome.FOREST)
      .add(Biome.BAMBOO_JUNGLE)
      .add(Biome.BAMBOO_JUNGLE_HILLS)
      .add(Biome.BIRCH_FOREST)
      .add(Biome.BIRCH_FOREST_HILLS)
      .add(Biome.DARK_FOREST)
      .add(Biome.DARK_FOREST_HILLS)
      .add(Biome.FLOWER_FOREST)
      .add(Biome.FOREST)
      .add(Biome.GIANT_SPRUCE_TAIGA)
      .add(Biome.GIANT_SPRUCE_TAIGA_HILLS)
      .add(Biome.GIANT_TREE_TAIGA)
      .add(Biome.GIANT_TREE_TAIGA_HILLS)
      .add(Biome.JUNGLE)
      .add(Biome.JUNGLE_EDGE)
      .add(Biome.JUNGLE_HILLS)
      .add(Biome.SWAMP)
      .add(Biome.SWAMP_HILLS)
      .add(Biome.TALL_BIRCH_FOREST)
      .add(Biome.TALL_BIRCH_HILLS)
      .add(Biome.WOODED_BADLANDS_PLATEAU)
      .add(Biome.WOODED_HILLS)
      .build();

  private static final Set<Biome> PLAIN_BIOMES = ImmutableSet.<Biome>builder()
      .add(Biome.SWAMP)
      .add(Biome.SWAMP_HILLS)
      .add(Biome.BADLANDS)
      .add(Biome.BADLANDS_PLATEAU)
      .add(Biome.PLAINS)
      .add(Biome.RIVER)
      .add(Biome.ICE_SPIKES)
      .add(Biome.MUSHROOM_FIELD_SHORE)
      .add(Biome.MUSHROOM_FIELDS)
      .add(Biome.SAVANNA)
      .add(Biome.SAVANNA_PLATEAU)
      .add(Biome.SHATTERED_SAVANNA)
      .add(Biome.SHATTERED_SAVANNA_PLATEAU)
      .add(Biome.SNOWY_TAIGA)
      .add(Biome.SNOWY_TUNDRA)
      .add(Biome.SUNFLOWER_PLAINS)
      .build();

  private static final Set<Biome> ADVENTURE_BIOMES = ImmutableSet.<Biome>builder()
      .add(Biome.DESERT)
      .add(Biome.DESERT_HILLS)
      .add(Biome.DESERT_LAKES)
      .add(Biome.SWAMP_HILLS)
      .add(Biome.SWAMP)
      .add(Biome.SNOWY_TUNDRA)
      .add(Biome.DARK_FOREST)
      .add(Biome.DARK_FOREST_HILLS)
      .add(Biome.MUSHROOM_FIELDS)
      .add(Biome.MUSHROOM_FIELD_SHORE)
      .add(Biome.SAVANNA)
      .add(Biome.SHATTERED_SAVANNA_PLATEAU)
      .add(Biome.SAVANNA_PLATEAU)
      .add(Biome.SHATTERED_SAVANNA)
      .add(Biome.BADLANDS)
      .add(Biome.BADLANDS_PLATEAU)
      .add(Biome.WOODED_BADLANDS_PLATEAU)
      .add(Biome.ERODED_BADLANDS)
      .add(Biome.MODIFIED_BADLANDS_PLATEAU)
      .add(Biome.MODIFIED_WOODED_BADLANDS_PLATEAU)
      .build();

  private static final EnumSet<Material> FARM_RUN_SOIL = EnumSet
      .of(Material.DIRT, Material.GRASS_BLOCK, Material.GRASS_PATH, Material.FARMLAND);

  public JobManager(FruchtJobs plugin) {
    this.economy = plugin.getEconomy();
    this.jobExpGainManager = new JobExpGainManager(plugin.getSieveManager(), this);
    this.jobBossBarManager = plugin.getJobBossBarManager();
    Bukkit.getPluginManager().registerEvents(new JobListener(this, jobBossBarManager, jobExpGainManager), plugin);
    this.holderMap = new Object2ObjectOpenHashMap<>();
    this.ioManager = new IOManager(plugin);
    JobPerkLib.init(plugin);
    Bukkit.getScheduler().runTaskTimer(plugin, this::checkPlayerPerkApplicatins, 20L, 20L);
  }

  @Getter
  private final Economy economy;
  private final JobExpGainManager jobExpGainManager;
  private final JobBossBarManager jobBossBarManager;
  private final Object2ObjectMap<UUID, JobHolder> holderMap;
  private final IOManager ioManager;
  private final PotionEffect speedOne = new PotionEffect(PotionEffectType.SPEED, 22, 0);
  @Getter
  @Setter
  private double expScalar = 1.0;

  public void openJobGUI(Player player) {
    SmartInventory.builder().title("Jobs").size(5).provider(new JobMainProvider(this, getOnlineHolder(player))).build().open(player);
  }

  public void openJobOptionsGUI(Player player) {
    SmartInventory.builder().title("Optionen").size(3).provider(new JobOptionsProvider(this)).build().open(player);
  }

  public JobHolder getOnlineHolder(Player player) {
    return holderMap.get(player.getUniqueId());
  }

  public void openJobPerkGUI(Player player, JobType jobType, int page) {
    SmartInventory.builder()
        .title("Perks: " + jobType.getDisplayName())
        .size(5)
        .provider(new JobPerkProvider(this, getOnlineHolder(player), jobType, page))
        .build()
        .open(player);
  }

  protected void login(UUID playerID) {
    this.loadHolder(playerID);
  }

  protected void logout(UUID playerID) {
    this.saveHolder(playerID);
  }

  private void loadHolder(UUID playerID) {
    ioManager.loadDataAsync(playerID, json -> {
      JobHolder holder = new JobHolder(playerID, jobBossBarManager, this);
      holder.load(json);
      this.holderMap.put(playerID, holder);
    });
  }

  private void saveHolder(UUID playerID) {
    JsonObject data = new JsonObject();
    JobHolder holder = this.holderMap.get(playerID);
    if (holder == null) {
      return;
    }
    holder.save(data);
    ioManager.saveDataAsync(playerID, data);
    this.holderMap.remove(playerID);
  }

  private void checkPlayerPerkApplicatins() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      JobHolder holder = getOnlineHolder(player);
      Location loc = player.getLocation();
      Biome playerBiome = player.getWorld().getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
      if (holder.isActive(JobType.GATHERER)) {
        if (holder.hasPerk(JobPerkType.CAVE_DIVER)) {
          if (player.getLocation().getY() <= 56) {
            player.addPotionEffect(speedOne);
          }
        } else if (holder.hasPerk(JobPerkType.FOREST_WANDERER)) {
          if (FOREST_BIOMES.contains(playerBiome)) {
            player.addPotionEffect(speedOne);
          }
        } else if (holder.hasPerk(JobPerkType.PLAIN_RUNNER)) {
          if (PLAIN_BIOMES.contains(playerBiome)) {
            player.addPotionEffect(speedOne);
          }
        }
      }
      if (holder.isActive(JobType.FARMER)) {
        if (holder.hasPerk(JobPerkType.FARM_SOIL_RUNNER)) {
          if (FARM_RUN_SOIL.contains(player.getLocation().subtract(0, 0.1, 0).getBlock().getType())) {
            player.addPotionEffect(speedOne);
          }
        }
      }
      if (holder.isActive(JobType.ADVENTURER)) {
        if (holder.hasPerk(JobPerkType.ADVENTURE_RUNNER)) {
          if (ADVENTURE_BIOMES.contains(playerBiome)) {
            player.addPotionEffect(speedOne);
          }
        } else if (holder.hasPerk(JobPerkType.NETHER_RUNNER)) {
          if (playerBiome == Biome.NETHER) {
            player.addPotionEffect(speedOne);
          }
        }
      }
    }
  }

}