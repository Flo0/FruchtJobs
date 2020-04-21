package com.gestankbratwurst.fruchtjobs.jobs;

import com.destroystokyo.paper.block.TargetBlockInfo.FluidMode;
import com.gestankbratwurst.fruchtcore.FruchtCore;
import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtcore.tasks.TaskManager;
import com.gestankbratwurst.fruchtcore.util.common.NameSpaceFactory;
import com.gestankbratwurst.fruchtcore.util.common.UtilBlock;
import com.gestankbratwurst.fruchtcore.util.common.UtilMobs;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtcore.util.tuples.Pair;
import com.gestankbratwurst.fruchtjobs.jobs.sieving.SieveManager;
import com.google.common.base.Stopwatch;
import it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import it.unimi.dsi.fastutil.doubles.Double2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 26.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class JobExpGainManager {

  public JobExpGainManager(SieveManager sieveManager, JobManager jobManager) {
    this.random = ThreadLocalRandom.current();
    this.taskManager = FruchtCore.getInstance().getTaskManager();
    this.jobManager = jobManager;
    this.sieveManager = sieveManager;
    this.oreMap = new EnumMap<>(Material.class);
    this.logMap = new EnumMap<>(Material.class);
    this.cropsMap = new EnumMap<>(Material.class);
    this.stoneMap = EnumSet.noneOf(Material.class);
    this.herbsMap = new EnumMap<>(Material.class);
    this.fruitBlocks = new EnumMap<>(Material.class);
    this.digMap = new EnumMap<>(Material.class);
    this.rubbleSet = EnumSet.noneOf(Material.class);
    this.digAddSet = EnumSet.noneOf(Material.class);
    this.findingsSet = EnumSet.noneOf(Material.class);
    this.fishMap = new EnumMap<>(Material.class);
    this.domesticatedAnimalMap = new EnumMap<>(EntityType.class);
    this.wildAnimalMap = new EnumMap<>(EntityType.class);
    this.pickaxes = EnumSet.noneOf(Material.class);
    this.axes = EnumSet.noneOf(Material.class);
    this.hoes = EnumSet.noneOf(Material.class);
    this.saltWaterBiomes = EnumSet.noneOf(Biome.class);
    this.adventureSoilSet = EnumSet.noneOf(Material.class);
    this.learnedButcherDrops = new EnumMap<>(EntityType.class);
    this.netherSearcherBlockDrops = new Double2ObjectOpenHashMap<>();
    this.oreVeinDrops = new Double2ObjectOpenHashMap<>();
    this.extraDigDrops = new Double2ObjectOpenHashMap<>();
    this.fragmentChestMap = new Double2ObjectOpenHashMap<>();
    this.fragmentDigMap = new Double2ObjectOpenHashMap<>();
    this.saltWaterFishMap = new Double2ObjectOpenHashMap<>();
    this.freshWaterFishMap = new Double2ObjectOpenHashMap<>();
    this.recipeMaps = new Object2ObjectLinkedOpenHashMap<>();
    this.smithItems = new ObjectArrayList<>();
    this.craftItems = new ObjectArrayList<>();
    init();
  }

  private final ThreadLocalRandom random;
  private final Map<ItemStack, JobExpPackage> recipeMaps;
  private final ObjectList<ItemStack> smithItems;
  private final ObjectList<ItemStack> craftItems;
  private final EnumSet<Biome> saltWaterBiomes;
  private final EnumSet<Material> stoneMap;
  private final EnumSet<Material> pickaxes;
  private final EnumSet<Material> axes;
  private final EnumSet<Material> hoes;
  private final EnumSet<Material> rubbleSet;
  private final EnumSet<Material> digAddSet;
  private final EnumSet<Material> findingsSet;
  private final EnumSet<Material> adventureSoilSet;
  private final EnumMap<Material, Long> fishMap;
  private final EnumMap<Material, Long> oreMap;
  private final EnumMap<Material, Long> logMap;
  private final EnumMap<Material, Long> cropsMap;
  private final EnumMap<Material, Long> fruitBlocks;
  private final EnumMap<Material, Long> herbsMap;
  private final EnumMap<Material, Long> digMap;
  private final EnumMap<EntityType, Long> domesticatedAnimalMap;
  private final EnumMap<EntityType, Long> wildAnimalMap;
  private final EnumMap<EntityType, Pair<Double, Supplier<ItemStack>>> learnedButcherDrops;
  private final Double2ObjectMap<ItemStack> netherSearcherBlockDrops;
  private final Double2ObjectMap<Supplier<ItemStack>> extraDigDrops;
  private final Double2ObjectMap<ItemStack> oreVeinDrops;
  private final Double2ObjectMap<ItemStack> fragmentChestMap;
  private final Double2ObjectMap<ItemStack> fragmentDigMap;
  private final Double2ObjectMap<ItemStack> saltWaterFishMap;
  private final Double2ObjectMap<ItemStack> freshWaterFishMap;
  private final JobManager jobManager;
  private final TaskManager taskManager;
  private final SieveManager sieveManager;

  protected void handleEvent(CraftItemEvent event) {
    int timesCrafted = 1;
    if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
      int lowest = 256;
      for (ItemStack ingredient : event.getInventory().getMatrix()) {
        if (ingredient != null) {
          if (ingredient.getAmount() < lowest) {
            lowest = ingredient.getAmount();
          }
        }
      }
      timesCrafted = lowest;
    }
    ItemStack result = event.getCurrentItem();
    if (result == null || result.getType() == Material.AIR) {
      return;
    }
    for (Entry<ItemStack, JobExpPackage> entry : recipeMaps.entrySet()) {
      if (entry.getKey().isSimilar(result)) {
        JobExpPackage jobExpPackage = entry.getValue();
        JobHolder holder = jobManager.getOnlineHolder((Player) event.getWhoClicked());
        if (holder.isActive(jobExpPackage.getJobType())) {
          double exp = jobExpPackage.getBaseExp();
          if (holder.hasPerk(JobPerkType.SMITH_APPRENTICE)) {
            for (ItemStack item : smithItems) {
              if (item.isSimilar(result)) {
                exp += Math.max(1, exp * 0.1);
              }
            }
          } else if (holder.hasPerk(JobPerkType.CRAFT_APPRENTICE)) {
            for (ItemStack item : craftItems) {
              if (item.isSimilar(result)) {
                exp += Math.max(1, exp * 0.15);
              }
            }
          }
          holder.addExp(jobExpPackage.getJobType(), (int) exp * timesCrafted);
        }
        break;
      }
    }
  }

  protected void handleEvent(EntityTameEvent event) {
    AnimalTamer tamer = event.getOwner();
    if (tamer instanceof Player) {
      JobHolder holder = jobManager.getOnlineHolder((Player) tamer);
      if (holder.isActive(JobType.FARMER)) {
        holder.addExp(JobType.FARMER, holder.hasPerk(JobPerkType.BETTER_BUTCHER) ? 38 : 30L);
      }
    }
  }

  protected void handleEvent(EntityBreedEvent event) {
    LivingEntity breeder = event.getBreeder();
    if (!(breeder instanceof Player)) {
      return;
    }
    JobHolder holder = jobManager.getOnlineHolder((Player) breeder);
    if (!holder.isActive(JobType.FARMER)) {
      return;
    }
    holder.addExp(JobType.FARMER, (holder.hasPerk(JobPerkType.BETTER_BUTCHER) ? 20 : 14));
  }

  protected void handleEvent(PlayerShearEntityEvent event) {
    JobHolder holder = jobManager.getOnlineHolder(event.getPlayer());
    if (!holder.isActive(JobType.FARMER)) {
      return;
    }
    holder.addExp(JobType.FARMER, (holder.hasPerk(JobPerkType.BETTER_BUTCHER) ? 10 : 8));
    if (holder.hasPerk(JobPerkType.LEARNED_BUTCHER)) {
      if (random.nextBoolean()) {
        Location dropLoc = event.getEntity().getLocation();
        ItemStack item = new ItemStack(Material.STRING);
        if (random.nextBoolean()) {
          item.add();
        }
        dropLoc.getWorld().dropItemNaturally(dropLoc, item);
      }
    }
  }

  protected void handleEvent(PlayerBucketFillEvent event) {
    final JobHolder holder = jobManager.getOnlineHolder(event.getPlayer());
    if (!holder.isActive(JobType.FARMER)) {
      return;
    }
    final Player player = event.getPlayer();

    taskManager.runBukkitSync(() -> {
      ItemStack item = player.getInventory().getItemInMainHand();
      if (item.getType() == Material.MILK_BUCKET) {
        holder.addExp(JobType.FARMER, (holder.hasPerk(JobPerkType.BETTER_BUTCHER) ? 11 : 8));
      }
    });

  }

  protected void handleEvent(EntityDeathEvent event) {
    LivingEntity defender = event.getEntity();
    if (!defender.isDead()) {
      return;
    }
    EntityDamageEvent lastEvent = defender.getLastDamageCause();
    if (lastEvent instanceof EntityDamageByEntityEvent) {
      Entity attacker = ((EntityDamageByEntityEvent) lastEvent).getDamager();
      if (attacker instanceof Projectile) {
        ProjectileSource source = ((Projectile) attacker).getShooter();
        if (source instanceof Player) {
          attacker = (Entity) source;
        } else {
          return;
        }
      }
      if (attacker instanceof Player) {
        Player player = (Player) attacker;
        JobHolder holder = jobManager.getOnlineHolder(player);
        if (holder.isActive(JobType.FARMER)) {
          EntityType type = defender.getType();

          if (holder.hasPerk(JobPerkType.THE_HUNT_MUST_GO_ON)) {
            if (domesticatedAnimalMap.containsKey(type) || wildAnimalMap.containsKey(type)) {
              if (!UtilMobs.isDomesticated(defender)) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 280, 1));
              }
            }
          }

          long farmExp = domesticatedAnimalMap.getOrDefault(type, 0L);
          long huntExp = wildAnimalMap.getOrDefault(type, 0L);
          if (farmExp > 0) {
            if (UtilMobs.isDomesticated(defender)) {
              farmExp += Math.max(1, farmExp * 0.2);
              if (holder.hasPerk(JobPerkType.BETTER_BUTCHER)) {
                farmExp += Math.max(1, farmExp * 0.1);
              }
              holder.addExp(JobType.FARMER, farmExp);
              if (holder.hasPerk(JobPerkType.LEARNED_BUTCHER)) {
                Pair<Double, Supplier<ItemStack>> entry = learnedButcherDrops.get(type);
                if (random.nextDouble() <= entry.getLeft()) {
                  defender.getLocation().getWorld().dropItemNaturally(defender.getLocation(), entry.getRight().get());
                }
              }
            } else {
              farmExp /= 2D;
              if (holder.hasPerk(JobPerkType.BETTER_HUNTING)) {
                farmExp += Math.max(1, farmExp * 0.33);
              }
              holder.addExp(JobType.FARMER, farmExp);
              if (holder.hasPerk(JobPerkType.WILD_HUNT)) {
                if (random.nextDouble() <= 0.185D) {
                  Location dropLoc = event.getEntity().getLocation();
                  for (ItemStack drop : event.getDrops()) {
                    dropLoc.getWorld().dropItemNaturally(dropLoc, drop);
                  }
                }
              }
            }
            holder.addExp(JobType.FARMER, farmExp);
          } else if (huntExp > 0) {
            if (holder.hasPerk(JobPerkType.WILD_HUNT)) {
              if (random.nextDouble() <= 0.185D) {
                Location dropLoc = event.getEntity().getLocation();
                for (ItemStack drop : event.getDrops()) {
                  dropLoc.getWorld().dropItemNaturally(dropLoc, drop);
                }
              }
            }
            if (holder.hasPerk(JobPerkType.BETTER_HUNTING)) {
              huntExp += Math.max(1, huntExp * 0.33);
            }
            holder.addExp(JobType.FARMER, huntExp);
          }
        }
      }
    }

  }

  protected void handleEvent(BlockBreakEvent event) {
    JobHolder holder = jobManager.getOnlineHolder(event.getPlayer());
    if (UtilBlock.isPlayerPlaced(event.getBlock())) {
      return;
    }
    Material material = event.getBlock().getType();
    Location dropLoc = event.getBlock().getLocation().clone().add(0.5, 0.5, 0.5);
    if (holder.isActive(JobType.GATHERER)) {
      ItemStack hitItem = event.getPlayer().getInventory().getItemInMainHand();
      if (holder.hasPerk(JobPerkType.SILKY_MINING)) {
        if (pickaxes.contains(hitItem.getType())) {
          if (random.nextDouble() < 0.1D) {
            for (ItemStack drop : event.getBlock().getDrops(hitItem)) {
              dropLoc.getWorld().dropItemNaturally(dropLoc, drop);
            }
          }
        }
      } else if (holder.hasPerk(JobPerkType.SILKY_WOODCUTTING)) {
        if (axes.contains(hitItem.getType())) {
          if (random.nextDouble() < 0.1D) {
            for (ItemStack drop : event.getBlock().getDrops(hitItem)) {
              dropLoc.getWorld().dropItemNaturally(dropLoc, drop);
            }
          }
        }
      }
      handleGatherer(holder, event);
      if (holder.hasPerk(JobPerkType.STONE_SALT)) {
        if (stoneMap.contains(material)) {
          if (random.nextDouble() < 0.0333) {
            dropLoc.getWorld().dropItemNaturally(dropLoc, ItemLibrary.STONE_SALT.getItem());
          }
        }
      }
      if (holder.hasPerk(JobPerkType.LUCKY_HERB)) {
        if (herbsMap.containsKey(material)) {
          if (random.nextDouble() < 0.01D) {
            dropLoc.getWorld().dropItemNaturally(dropLoc, ItemLibrary.LUCKY_CLOVER.getItem());
          }
        }
      }
      if (holder.hasPerk(JobPerkType.SECRET_ORE_VEINS)) {
        if (stoneMap.contains(material)) {
          for (Double2ObjectMap.Entry<ItemStack> entry : oreVeinDrops.double2ObjectEntrySet()) {
            if (random.nextDouble() <= entry.getDoubleKey()) {
              dropLoc.getWorld().dropItemNaturally(dropLoc, entry.getValue());
            }
          }
        }
      } else if (holder.hasPerk(JobPerkType.TAR_KNOWLEDGE)) {
        if (logMap.containsKey(material)) {
          if (random.nextDouble() <= 0.1D) {
            dropLoc.getWorld().dropItemNaturally(dropLoc, ItemLibrary.RESIN.getItem());
          }
        }
      } else if (holder.hasPerk(JobPerkType.HERBAL_KNOWLEDGE)) {
        boolean extraChance = holder.hasPerk(JobPerkType.HERBAL_TOOLS) && hoes.contains(hitItem.getType());
        if (herbsMap.containsKey(material)) {
          if (material.toString().contains("MUSHROOM")) {
            if (random.nextDouble() <= (extraChance ? 0.16 : 0.08)) {
              dropLoc.getWorld().dropItemNaturally(dropLoc, ItemLibrary.WHITE_MUSHROOMS.getItem());
            }
          } else {
            if (random.nextDouble() <= (extraChance ? 0.056 : 0.028)) {
              dropLoc.getWorld().dropItemNaturally(dropLoc, ItemLibrary.ROOTS.getItem());
            }
            if (random.nextDouble() <= (extraChance ? 0.11 : 0.055)) {
              dropLoc.getWorld().dropItemNaturally(dropLoc, ItemLibrary.WILD_POLLEN.getItem());
            }
          }
        }
      }
    }
    if (holder.isActive(JobType.FARMER)) {
      handleFarmer(holder, event);
    }
    if (holder.isActive(JobType.ADVENTURER)) {
      handleAdventurer(holder, event);
    }
  }

  protected void handleEvent(PlayerFishEvent event) {
    JobHolder holder = jobManager.getOnlineHolder(event.getPlayer());
    if (event.getState() != State.CAUGHT_FISH) {
      return;
    }
    if (holder.isActive(JobType.FISHER)) {
      Entity entity = event.getCaught();
      if (entity instanceof Item) {
        Item item = (Item) entity;
        long fishExp = fishMap.getOrDefault(item.getItemStack().getType(), 0L) + (event.getExpToDrop() * 2);
        if (fishExp != 0L) {
          Location loc = entity.getLocation();
          Biome fishBiome = entity.getWorld().getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
          if (holder.hasPerk(JobPerkType.SWEET_WATER_FISHING)) {
            if (!saltWaterBiomes.contains(fishBiome)) {
              for (Double2ObjectMap.Entry<ItemStack> entry : freshWaterFishMap.double2ObjectEntrySet()) {
                if (random.nextDouble() <= entry.getDoubleKey()) {
                  item.setItemStack(entry.getValue());
                  fishExp += Math.min(1, fishExp * 0.30);
                  break;
                }
              }
            }
          } else if (holder.hasPerk(JobPerkType.SALT_WATER_FISHING)) {
            if (saltWaterBiomes.contains(fishBiome)) {
              for (Double2ObjectMap.Entry<ItemStack> entry : saltWaterFishMap.double2ObjectEntrySet()) {
                if (random.nextDouble() <= entry.getDoubleKey()) {
                  item.setItemStack(entry.getValue());
                  fishExp += Math.min(1, fishExp * 0.30);
                  break;
                }
              }
            }
          }
          holder.addExp(JobType.FISHER, fishExp);
        }
      }
    }
  }

  protected void handleEvent(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    JobHolder holder = jobManager.getOnlineHolder(player);
    Block block = event.getClickedBlock();
    ItemStack hitItem = event.getItem();
    if (holder.hasPerk(JobPerkType.RECIPE_SALT)) {
      if (hitItem != null && hitItem.getType() == Material.PAPER) {
        Block targetBlock = player.getTargetBlock(3, FluidMode.SOURCE_ONLY);
        if (targetBlock != null) {
          if (targetBlock.getType() == Material.WATER) {
            Biome targetBiome = targetBlock.getWorld().getBiome(targetBlock.getX(), targetBlock.getY(), targetBlock.getZ());
            if (saltWaterBiomes.contains(targetBiome)) {
              player.getInventory().addItem(ItemLibrary.STONE_SALT.getItem()).values()
                  .forEach(left -> player.getWorld().dropItemNaturally(player.getLocation(), left));
              targetBlock.getWorld().playSound(targetBlock.getLocation(), Sound.ENTITY_BOAT_PADDLE_WATER, 0.8F, 1.3F);
              holder.addExp(JobType.FISHER, 1);
              if (hitItem.getAmount() == 1) {
                player.getInventory().setItemInMainHand(null);
              } else {
                hitItem.subtract();
              }
            }
          }
        }
      }
    }
    if (block == null) {
      return;
    }
    if (UtilBlock.isPlayerPlaced(block)) {
      return;
    }
    if (holder.isActive(JobType.FARMER)) {
      Material mat = block.getType();
      if (mat == Material.SWEET_BERRY_BUSH) {
        Ageable ageable = (Ageable) block.getBlockData();
        if (ageable.getAge() == ageable.getMaximumAge()) {
          holder.addExp(JobType.FARMER, 3L);
        }
      }
      if (hitItem != null) {
        if (hoes.contains(hitItem.getType())) {
          if (holder.hasPerk(JobPerkType.GREEN_THUMB)) {
            if (cropsMap.containsKey(mat)) {
              new BlockBreakEvent(block, event.getPlayer()).callEvent();
              BlockData bd = block.getBlockData();
              if (bd instanceof Ageable) {
                Ageable ab = (Ageable) bd;
                if (ab.getAge() == ab.getMaximumAge()) {
                  Damageable damageable = (Damageable) hitItem.getItemMeta();
                  double chance = 1D;
                  if (hitItem.containsEnchantment(Enchantment.DURABILITY)) {
                    chance = 1D / (hitItem.getEnchantmentLevel(Enchantment.DURABILITY) + 1D);
                  }
                  if (random.nextDouble() <= chance) {
                    if (damageable.getDamage() >= hitItem.getType().getMaxDurability()) {
                      event.getPlayer().getInventory().setItemInMainHand(null);
                      UtilPlayer.playSound(event.getPlayer(), Sound.ENTITY_ITEM_BREAK);
                    } else {
                      damageable.setDamage(damageable.getDamage() + 1);
                      hitItem.setItemMeta((ItemMeta) damageable);
                    }
                  }
                  UtilPlayer.playSound(event.getPlayer(), Sound.ITEM_HOE_TILL, 0.85F, 1.25F);
                  for (ItemStack drop : block.getDrops(hitItem)) {
                    block.getWorld().dropItemNaturally(block.getLocation(), drop);
                  }
                  taskManager.runBukkitSync(() -> block.setType(mat));
                }
              }
            }
          }
        }
      }
    }
    if (holder.isActive(JobType.ADVENTURER)) {
      Material mat = block.getType();
      if (mat == Material.CHEST || mat == Material.TRAPPED_CHEST) {
        Chest chest = (Chest) block.getState();
        PersistentDataContainer pdc = chest.getPersistentDataContainer();
        if (!pdc.has(NameSpaceFactory.provide(event.getPlayer().getUniqueId().toString()), PersistentDataType.INTEGER)) {
          pdc.set(NameSpaceFactory.provide(event.getPlayer().getUniqueId().toString()), PersistentDataType.INTEGER, 1);
          if (holder.hasPerk(JobPerkType.FRAGMENT_SEARCH)) {
            Inventory inv = chest.getBlockInventory();
            for (Double2ObjectMap.Entry<ItemStack> entry : fragmentChestMap.double2ObjectEntrySet()) {
              if (random.nextDouble() <= entry.getDoubleKey()) {
                inv.addItem(entry.getValue()).values().forEach(remaining ->
                    player.getInventory().addItem(remaining).values().forEach(left ->
                        player.getWorld().dropItemNaturally(player.getLocation(), left)));
              }
            }
          }
          chest.update(true);
          int exp = 50;
          if (holder.hasPerk(JobPerkType.NETHER_SEARCHER)) {
            exp += Math.max(1, exp * 0.25D);
          }
          holder.addExp(JobType.ADVENTURER, exp);
          UtilPlayer.playSound(event.getPlayer(), Sound.BLOCK_CHEST_OPEN, 1F, 0.4F);
        }
      }
    }
  }

  private void handleAdventurer(JobHolder holder, BlockBreakEvent event) {
    Block block = event.getBlock();
    Material material = block.getType();
    long digExp = digMap.getOrDefault(material, 0L);
    if (digExp != 0L) {
      Location dropLoc = block.getLocation();
      if (holder.hasPerk(JobPerkType.FRAGMENT_DIG) && adventureSoilSet.contains(material)) {
        for (Double2ObjectMap.Entry<ItemStack> entry : fragmentDigMap.double2ObjectEntrySet()) {
          if (random.nextDouble() <= entry.getDoubleKey()) {
            dropLoc.getWorld().dropItemNaturally(dropLoc, entry.getValue());
          }
        }
      }
      if (holder.hasPerk(JobPerkType.SPADE_FINDINGS) && adventureSoilSet.contains(material)) {
        for (Double2ObjectMap.Entry<Supplier<ItemStack>> entry : extraDigDrops.double2ObjectEntrySet()) {
          if (random.nextDouble() <= entry.getDoubleKey()) {
            dropLoc.getWorld().dropItemNaturally(dropLoc, entry.getValue().get());
          }
        }
      } else if (holder.hasPerk(JobPerkType.NETHER_SEARCHER)) {
        if (material == Material.SOUL_SAND) {
          for (Double2ObjectMap.Entry<ItemStack> entry : netherSearcherBlockDrops.double2ObjectEntrySet()) {
            if (random.nextDouble() <= entry.getDoubleKey()) {
              dropLoc.getWorld().dropItemNaturally(dropLoc, entry.getValue());
            }
          }
        } else if (material == Material.SPAWNER) {
          digExp += Math.max(1, digExp * 0.25D);
        }
      }
      if (holder.hasPerk(JobPerkType.DIGGER)) {
        if (rubbleSet.contains(material)) {
          digExp += Math.max(1, digExp * 0.1);
        } else if (digAddSet.contains(material)) {
          digExp += Math.max(1, digExp * 0.15);
        }
      } else if (holder.hasPerk(JobPerkType.BONE_FINDER)) {
        if (material == Material.BONE_BLOCK) {
          if (random.nextDouble() >= 0.2) {
            ItemStack drop = new ItemStack(Material.BONE);
            if (random.nextBoolean()) {
              drop.add();
            }
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
          }
          if (random.nextDouble() >= 0.5) {
            ItemStack drop = new ItemStack(Material.BONE_MEAL);
            if (random.nextBoolean()) {
              drop.add();
            }
            block.getWorld().dropItemNaturally(block.getLocation(), drop);
          }
          digExp += Math.max(1, digExp * 0.25);
        } else if (findingsSet.contains(material)) {
          digExp += Math.max(1, digExp * 0.1);
        }
      }
      holder.addExp(JobType.ADVENTURER, digExp);
    }
  }

  private void handleFarmer(JobHolder holder, BlockBreakEvent event) {
    Block block = event.getBlock();
    Material material = block.getType();
    long farmingExp = cropsMap.getOrDefault(material, 0L);
    long fruitExp = fruitBlocks.getOrDefault(material, 0L);
    if (farmingExp != 0L) {
      if (holder.hasPerk(JobPerkType.BETTER_HARVEST)) {
        farmingExp += Math.max(1, farmingExp * 0.1);
      }
      BlockData bd = block.getBlockData();
      if (bd instanceof Ageable) {
        Ageable ab = (Ageable) bd;
        if (ab.getAge() == ab.getMaximumAge()) {
          holder.addExp(JobType.GATHERER, farmingExp);
        }
      }
    } else if (fruitExp != 0L) {
      if (holder.hasPerk(JobPerkType.BETTER_HARVEST)) {
        fruitExp += Math.max(1, farmingExp * 0.1);
      }
      holder.addExp(JobType.GATHERER, fruitExp);
    } else if (material == Material.SUGAR_CANE) {
      int canes = 1;
      while ((block = block.getRelative(BlockFace.UP)).getType() == Material.SUGAR_CANE) {
        canes++;
      }
      if (holder.hasPerk(JobPerkType.BETTER_HARVEST)) {
        canes += Math.max(1, canes * 0.1);
      }
      holder.addExp(JobType.GATHERER, canes);
    } else if (material == Material.BAMBOO) {
      int bamboos = 1;
      while ((block = block.getRelative(BlockFace.UP)).getType() == Material.BAMBOO) {
        bamboos++;
      }
      if (holder.hasPerk(JobPerkType.BETTER_HARVEST)) {
        bamboos += Math.max(1, bamboos * 0.1);
      }
      holder.addExp(JobType.GATHERER, Math.max(1, bamboos / 2));
    }
  }

  private void handleGatherer(JobHolder holder, BlockBreakEvent event) {
    Material material = event.getBlock().getType();
    long miningExp = oreMap.getOrDefault(material, 0L);
    long woodcuttingExp = logMap.getOrDefault(material, 0L);
    long herbExp = herbsMap.getOrDefault(material, 0L);
    if (miningExp != 0L) {
      if (holder.hasPerk(JobPerkType.ORE_HUNTER)) {
        miningExp += Math.max(1, miningExp * 0.1D);
      }
      holder.addExp(JobType.GATHERER, miningExp);
    } else if (stoneMap.contains(material)) {
      if (holder.addMinedStoneCount(1) % 4 == 0) {
        holder.addExp(JobType.GATHERER, 1);
        if (holder.hasPerk(JobPerkType.STONE_HUNTER) && holder.getMinedStoneCount() % 20 == 0) {
          holder.addExp(JobType.GATHERER, 20L);
        }
      }
    } else if (herbExp != 0L) {
      if (holder.hasPerk(JobPerkType.PLANT_HUNTER)) {
        herbExp += Math.max(1, herbExp * 0.1);
      }
      holder.addExp(JobType.GATHERER, herbExp);
    } else if (woodcuttingExp != 0L) {
      if (holder.hasPerk(JobPerkType.TREE_HUNTER)) {
        woodcuttingExp += Math.max(1, woodcuttingExp * 0.1D);
      }
      holder.addExp(JobType.GATHERER, woodcuttingExp);
    }
  }

  private void init() {
    Stopwatch sw = Stopwatch.createStarted();

    fishMap.put(Material.COD, 16L);
    fishMap.put(Material.SALMON, 16L);
    fishMap.put(Material.PUFFERFISH, 24L);
    fishMap.put(Material.TROPICAL_FISH, 19L);

    oreMap.put(Material.COAL_ORE, 8L);
    oreMap.put(Material.IRON_ORE, 12L);
    oreMap.put(Material.REDSTONE_ORE, 13L);
    oreMap.put(Material.LAPIS_ORE, 28L);
    oreMap.put(Material.GOLD_ORE, 22L);
    oreMap.put(Material.DIAMOND_ORE, 35L);
    oreMap.put(Material.EMERALD_ORE, 40L);

    stoneMap.add(Material.STONE);
    stoneMap.add(Material.DIORITE);
    stoneMap.add(Material.ANDESITE);
    stoneMap.add(Material.GRANITE);

    for (Material material : Material.values()) {
      if (material.toString().contains("PICKAXE")) {
        pickaxes.add(material);
      } else if (material.toString().contains("AXE")) {
        axes.add(material);
      } else if (material.toString().contains("HOE")) {
        hoes.add(material);
      }
    }

    herbsMap.put(Material.DEAD_BUSH, 3L);
    herbsMap.put(Material.BROWN_MUSHROOM, 6L);
    herbsMap.put(Material.RED_MUSHROOM, 6L);
    herbsMap.put(Material.LILY_PAD, 4L);
    herbsMap.put(Material.DANDELION, 4L);
    herbsMap.put(Material.POPPY, 4L);
    herbsMap.put(Material.BLUE_ORCHID, 5L);
    herbsMap.put(Material.ALLIUM, 5L);
    herbsMap.put(Material.AZURE_BLUET, 5L);
    herbsMap.put(Material.RED_TULIP, 5L);
    herbsMap.put(Material.ORANGE_TULIP, 5L);
    herbsMap.put(Material.WHITE_TULIP, 5L);
    herbsMap.put(Material.PINK_TULIP, 5L);
    herbsMap.put(Material.OXEYE_DAISY, 5L);
    herbsMap.put(Material.CORNFLOWER, 5L);
    herbsMap.put(Material.LILY_OF_THE_VALLEY, 12L);
    herbsMap.put(Material.WITHER_ROSE, 100L);
    herbsMap.put(Material.SUNFLOWER, 8L);
    herbsMap.put(Material.LILAC, 8L);
    herbsMap.put(Material.ROSE_BUSH, 8L);
    herbsMap.put(Material.PEONY, 8L);
    herbsMap.put(Material.BEE_NEST, 160L);
    herbsMap.put(Material.GRASS, 1L);
    herbsMap.put(Material.TALL_GRASS, 1L);
    herbsMap.put(Material.BEEHIVE, 160L);

    cropsMap.put(Material.WHEAT, 3L);
    cropsMap.put(Material.CARROTS, 3L);
    cropsMap.put(Material.POTATOES, 3L);
    cropsMap.put(Material.NETHER_WART, 4L);
    cropsMap.put(Material.BEETROOTS, 4L);

    fruitBlocks.put(Material.MELON, 6L);
    fruitBlocks.put(Material.PUMPKIN, 6L);
    fruitBlocks.put(Material.COCOA, 7L);

    digMap.put(Material.PODZOL, 4L);
    digMap.put(Material.MYCELIUM, 20L);
    digMap.put(Material.GRAVEL, 2L);
    digMap.put(Material.SAND, 1L);
    digMap.put(Material.CLAY, 12L);
    digMap.put(Material.MOSSY_COBBLESTONE, 12L);
    digMap.put(Material.SPAWNER, 1200L);
    digMap.put(Material.NETHER_QUARTZ_ORE, 7L);
    digMap.put(Material.SOUL_SAND, 4L);
    digMap.put(Material.MAGMA_BLOCK, 3L);
    digMap.put(Material.SMOKER, 80L);
    digMap.put(Material.BLAST_FURNACE, 80L);
    digMap.put(Material.BARREL, 50L);
    digMap.put(Material.LOOM, 80L);
    digMap.put(Material.FLETCHING_TABLE, 80L);
    digMap.put(Material.CARTOGRAPHY_TABLE, 80L);
    digMap.put(Material.COBWEB, 3L);
    digMap.put(Material.BELL, 120L);
    digMap.put(Material.LECTERN, 80L);
    digMap.put(Material.SMITHING_TABLE, 80L);
    digMap.put(Material.GRINDSTONE, 80L);
    digMap.put(Material.ANVIL, 80L);
    digMap.put(Material.COMPOSTER, 12L);
    digMap.put(Material.CAULDRON, 80L);
    digMap.put(Material.BREWING_STAND, 120L);
    digMap.put(Material.BONE_BLOCK, 100L);

    rubbleSet.add(Material.SAND);
    rubbleSet.add(Material.GRAVEL);
    rubbleSet.add(Material.CLAY);

    digAddSet.add(Material.PODZOL);
    digAddSet.add(Material.MYCELIUM);
    digAddSet.add(Material.MOSSY_COBBLESTONE);
    digAddSet.add(Material.SPAWNER);
    digAddSet.add(Material.NETHER_QUARTZ_ORE);
    digAddSet.add(Material.MAGMA_BLOCK);
    digAddSet.add(Material.COBWEB);

    adventureSoilSet.add(Material.SAND);
    adventureSoilSet.add(Material.GRAVEL);
    adventureSoilSet.add(Material.CLAY);
    adventureSoilSet.add(Material.PODZOL);
    adventureSoilSet.add(Material.MYCELIUM);

    findingsSet.add(Material.SMOKER);
    findingsSet.add(Material.BLAST_FURNACE);
    findingsSet.add(Material.LOOM);
    findingsSet.add(Material.FLETCHING_TABLE);
    findingsSet.add(Material.CARTOGRAPHY_TABLE);
    findingsSet.add(Material.BELL);
    findingsSet.add(Material.LECTERN);
    findingsSet.add(Material.SMITHING_TABLE);
    findingsSet.add(Material.GRINDSTONE);
    findingsSet.add(Material.ANVIL);
    findingsSet.add(Material.COMPOSTER);
    findingsSet.add(Material.CAULDRON);
    findingsSet.add(Material.BREWING_STAND);

    recipeMaps.put(new ItemStack(Material.FLINT_AND_STEEL), JobExpPackage.of(JobType.CRAFTER, 9));
    recipeMaps.put(new ItemStack(Material.BUCKET), JobExpPackage.of(JobType.CRAFTER, 18));
    recipeMaps.put(new ItemStack(Material.SHEARS), JobExpPackage.of(JobType.CRAFTER, 12));
    recipeMaps.put(new ItemStack(Material.WOODEN_PICKAXE), JobExpPackage.of(JobType.CRAFTER, 2));
    recipeMaps.put(new ItemStack(Material.WOODEN_AXE), JobExpPackage.of(JobType.CRAFTER, 2));
    recipeMaps.put(new ItemStack(Material.WOODEN_SWORD), JobExpPackage.of(JobType.CRAFTER, 2));
    recipeMaps.put(new ItemStack(Material.WOODEN_HOE), JobExpPackage.of(JobType.CRAFTER, 2));
    recipeMaps.put(new ItemStack(Material.WOODEN_SHOVEL), JobExpPackage.of(JobType.CRAFTER, 1));
    recipeMaps.put(new ItemStack(Material.BOOKSHELF), JobExpPackage.of(JobType.CRAFTER, 30));
    recipeMaps.put(new ItemStack(Material.ANVIL), JobExpPackage.of(JobType.CRAFTER, 188));
    recipeMaps.put(new ItemStack(Material.IRON_SHOVEL), JobExpPackage.of(JobType.CRAFTER, 7));
    recipeMaps.put(new ItemStack(Material.IRON_AXE), JobExpPackage.of(JobType.CRAFTER, 19));
    recipeMaps.put(new ItemStack(Material.IRON_PICKAXE), JobExpPackage.of(JobType.CRAFTER, 19));
    recipeMaps.put(new ItemStack(Material.IRON_SWORD), JobExpPackage.of(JobType.CRAFTER, 13));
    recipeMaps.put(new ItemStack(Material.BOW), JobExpPackage.of(JobType.CRAFTER, 13));
    recipeMaps.put(new ItemStack(Material.STONE_SWORD), JobExpPackage.of(JobType.CRAFTER, 3));
    recipeMaps.put(new ItemStack(Material.STONE_PICKAXE), JobExpPackage.of(JobType.CRAFTER, 4));
    recipeMaps.put(new ItemStack(Material.STONE_AXE), JobExpPackage.of(JobType.CRAFTER, 4));
    recipeMaps.put(new ItemStack(Material.STONE_SHOVEL), JobExpPackage.of(JobType.CRAFTER, 2));
    recipeMaps.put(new ItemStack(Material.STONE_HOE), JobExpPackage.of(JobType.CRAFTER, 3));
    recipeMaps.put(new ItemStack(Material.DIAMOND_AXE), JobExpPackage.of(JobType.CRAFTER, 61));
    recipeMaps.put(new ItemStack(Material.DIAMOND_PICKAXE), JobExpPackage.of(JobType.CRAFTER, 61));
    recipeMaps.put(new ItemStack(Material.DIAMOND_SWORD), JobExpPackage.of(JobType.CRAFTER, 41));
    recipeMaps.put(new ItemStack(Material.DIAMOND_HOE), JobExpPackage.of(JobType.CRAFTER, 41));
    recipeMaps.put(new ItemStack(Material.DIAMOND_SHOVEL), JobExpPackage.of(JobType.CRAFTER, 21));
    recipeMaps.put(new ItemStack(Material.GOLDEN_SWORD), JobExpPackage.of(JobType.CRAFTER, 31));
    recipeMaps.put(new ItemStack(Material.GOLDEN_SHOVEL), JobExpPackage.of(JobType.CRAFTER, 16));
    recipeMaps.put(new ItemStack(Material.GOLDEN_AXE), JobExpPackage.of(JobType.CRAFTER, 36));
    recipeMaps.put(new ItemStack(Material.GOLDEN_PICKAXE), JobExpPackage.of(JobType.CRAFTER, 36));
    recipeMaps.put(new ItemStack(Material.GOLDEN_HOE), JobExpPackage.of(JobType.CRAFTER, 31));
    recipeMaps.put(new ItemStack(Material.LEATHER_HELMET), JobExpPackage.of(JobType.CRAFTER, 30));
    recipeMaps.put(new ItemStack(Material.LEATHER_CHESTPLATE), JobExpPackage.of(JobType.CRAFTER, 48));
    recipeMaps.put(new ItemStack(Material.LEATHER_LEGGINGS), JobExpPackage.of(JobType.CRAFTER, 42));
    recipeMaps.put(new ItemStack(Material.LEATHER_BOOTS), JobExpPackage.of(JobType.CRAFTER, 24));
    recipeMaps.put(new ItemStack(Material.CHAINMAIL_HELMET), JobExpPackage.of(JobType.CRAFTER, 30));
    recipeMaps.put(new ItemStack(Material.CHAINMAIL_CHESTPLATE), JobExpPackage.of(JobType.CRAFTER, 48));
    recipeMaps.put(new ItemStack(Material.CHAINMAIL_LEGGINGS), JobExpPackage.of(JobType.CRAFTER, 42));
    recipeMaps.put(new ItemStack(Material.CHAINMAIL_BOOTS), JobExpPackage.of(JobType.CRAFTER, 24));
    recipeMaps.put(new ItemStack(Material.DIAMOND_HELMET), JobExpPackage.of(JobType.CRAFTER, 100));
    recipeMaps.put(new ItemStack(Material.DIAMOND_CHESTPLATE), JobExpPackage.of(JobType.CRAFTER, 160));
    recipeMaps.put(new ItemStack(Material.DIAMOND_LEGGINGS), JobExpPackage.of(JobType.CRAFTER, 140));
    recipeMaps.put(new ItemStack(Material.DIAMOND_BOOTS), JobExpPackage.of(JobType.CRAFTER, 80));
    recipeMaps.put(new ItemStack(Material.GOLDEN_HELMET), JobExpPackage.of(JobType.CRAFTER, 75));
    recipeMaps.put(new ItemStack(Material.GOLDEN_CHESTPLATE), JobExpPackage.of(JobType.CRAFTER, 120));
    recipeMaps.put(new ItemStack(Material.GOLDEN_LEGGINGS), JobExpPackage.of(JobType.CRAFTER, 105));
    recipeMaps.put(new ItemStack(Material.GOLDEN_BOOTS), JobExpPackage.of(JobType.CRAFTER, 60));
    recipeMaps.put(new ItemStack(Material.FISHING_ROD), JobExpPackage.of(JobType.CRAFTER, 9));
    recipeMaps.put(new ItemStack(Material.ARROW), JobExpPackage.of(JobType.CRAFTER, 6));

    smithItems.add(new ItemStack(Material.ANVIL));
    smithItems.add(new ItemStack(Material.IRON_AXE));
    smithItems.add(new ItemStack(Material.IRON_PICKAXE));
    smithItems.add(new ItemStack(Material.IRON_SHOVEL));
    smithItems.add(new ItemStack(Material.IRON_HOE));
    smithItems.add(new ItemStack(Material.IRON_SWORD));
    smithItems.add(new ItemStack(Material.GOLDEN_AXE));
    smithItems.add(new ItemStack(Material.GOLDEN_PICKAXE));
    smithItems.add(new ItemStack(Material.GOLDEN_SHOVEL));
    smithItems.add(new ItemStack(Material.GOLDEN_HOE));
    smithItems.add(new ItemStack(Material.GOLDEN_SWORD));
    smithItems.add(new ItemStack(Material.DIAMOND_AXE));
    smithItems.add(new ItemStack(Material.DIAMOND_PICKAXE));
    smithItems.add(new ItemStack(Material.DIAMOND_SHOVEL));
    smithItems.add(new ItemStack(Material.DIAMOND_HOE));
    smithItems.add(new ItemStack(Material.DIAMOND_SWORD));
    smithItems.add(new ItemStack(Material.IRON_HELMET));
    smithItems.add(new ItemStack(Material.IRON_CHESTPLATE));
    smithItems.add(new ItemStack(Material.IRON_LEGGINGS));
    smithItems.add(new ItemStack(Material.IRON_BOOTS));
    smithItems.add(new ItemStack(Material.CHAINMAIL_HELMET));
    smithItems.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
    smithItems.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
    smithItems.add(new ItemStack(Material.CHAINMAIL_BOOTS));
    smithItems.add(new ItemStack(Material.DIAMOND_HELMET));
    smithItems.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
    smithItems.add(new ItemStack(Material.DIAMOND_LEGGINGS));
    smithItems.add(new ItemStack(Material.DIAMOND_BOOTS));

    for (ItemStack item : recipeMaps.keySet()) {
      if (!smithItems.contains(item)) {
        craftItems.add(item);
      }
    }

    saltWaterFishMap.put(0.181, ItemLibrary.ANEMON_FISH.getItem());
    saltWaterFishMap.put(0.061, ItemLibrary.HAWK_FISH.getItem());
    saltWaterFishMap.put(0.221, ItemLibrary.SNAPPER.getItem());

    freshWaterFishMap.put(0.181, ItemLibrary.SHRIMP.getItem());
    freshWaterFishMap.put(0.061, ItemLibrary.SHAD.getItem());
    freshWaterFishMap.put(0.221, ItemLibrary.TROUT.getItem());

    oreVeinDrops.put(0.0012, new ItemStack(Material.EMERALD_ORE));
    oreVeinDrops.put(0.0016, new ItemStack(Material.DIAMOND_ORE));
    oreVeinDrops.put(0.015, new ItemStack(Material.IRON_ORE));
    oreVeinDrops.put(0.028, new ItemStack(Material.COAL_ORE));
    oreVeinDrops.put(0.0024, new ItemStack(Material.GOLD_ORE));
    oreVeinDrops.put(0.0018, new ItemStack(Material.LAPIS_ORE));
    oreVeinDrops.put(0.0075, new ItemStack(Material.REDSTONE_ORE));

    domesticatedAnimalMap.put(EntityType.CHICKEN, 18L);
    domesticatedAnimalMap.put(EntityType.COW, 26L);
    domesticatedAnimalMap.put(EntityType.MUSHROOM_COW, 55L);
    domesticatedAnimalMap.put(EntityType.PIG, 26L);
    domesticatedAnimalMap.put(EntityType.RABBIT, 18L);
    domesticatedAnimalMap.put(EntityType.SHEEP, 22L);

    wildAnimalMap.put(EntityType.WOLF, 30L);
    wildAnimalMap.put(EntityType.SLIME, 25L);
    wildAnimalMap.put(EntityType.BAT, 10L);
    wildAnimalMap.put(EntityType.POLAR_BEAR, 120L);
    wildAnimalMap.put(EntityType.TURTLE, 30L);
    wildAnimalMap.put(EntityType.PANDA, 25L);
    wildAnimalMap.put(EntityType.FOX, 24L);
    wildAnimalMap.put(EntityType.BEE, 20L);
    wildAnimalMap.put(EntityType.OCELOT, 22L);
    wildAnimalMap.put(EntityType.LLAMA, 25L);

    learnedButcherDrops.put(EntityType.COW, Pair.of(0.08D, ItemLibrary.FILLET::getItem));
    learnedButcherDrops.put(EntityType.PIG, Pair.of(0.08D, ItemLibrary.FILLET::getItem));
    learnedButcherDrops.put(EntityType.SHEEP, Pair.of(0.08D, ItemLibrary.FILLET::getItem));
    learnedButcherDrops.put(EntityType.CHICKEN, Pair.of(0.25D, () -> {
      ItemStack item = new ItemStack(Material.FEATHER);
      item.add(random.nextInt(1, 3));
      return item;
    }));
    learnedButcherDrops.put(EntityType.MUSHROOM_COW, Pair.of(0.1D, ItemLibrary.FILLET::getItem));
    learnedButcherDrops.put(EntityType.RABBIT, Pair.of(0.06D, ItemLibrary.FILLET::getItem));

    extraDigDrops.put(0.0025, () -> new ItemStack(Material.DIAMOND));
    extraDigDrops.put(0.002, () -> new ItemStack(Material.EMERALD));
    extraDigDrops.put(0.08, () -> new ItemStack(Material.BONE));
    extraDigDrops.put(0.081, () -> new ItemStack(Material.BONE_MEAL));
    extraDigDrops.put(0.0451, () -> new ItemStack(Material.GRAVEL));
    extraDigDrops.put(0.0452, () -> new ItemStack(Material.SAND));
    extraDigDrops.put(0.0453, () -> new ItemStack(Material.CLAY_BALL));
    extraDigDrops.put(0.01, () -> new ItemStack(Material.LAPIS_LAZULI));
    extraDigDrops.put(0.028, () -> new ItemStack(Material.COAL));
    extraDigDrops.put(0.04, () -> {
      ItemStack item = new ItemStack(Material.IRON_NUGGET);
      if (random.nextBoolean()) {
        item.add();
        if (random.nextBoolean()) {
          item.add();
        }
      }
      return item;
    });
    extraDigDrops.put(0.02, () -> {
      ItemStack item = new ItemStack(Material.GOLD_NUGGET);
      if (random.nextBoolean()) {
        item.add();
        if (random.nextBoolean()) {
          item.add();
        }
      }
      return item;
    });

    netherSearcherBlockDrops.put(0.08, new ItemStack(Material.BLAZE_POWDER));
    netherSearcherBlockDrops.put(0.18, new ItemStack(Material.BONE));
    netherSearcherBlockDrops.put(0.01, new ItemStack(Material.BLAZE_ROD));
    netherSearcherBlockDrops.put(0.05, new ItemStack(Material.QUARTZ));
    netherSearcherBlockDrops.put(0.1, new ItemStack(Material.GUNPOWDER));

    fragmentChestMap.put(0.0401, ItemLibrary.LESSER_ARTIFACT_ADEM.getItem());
    fragmentChestMap.put(0.0402, ItemLibrary.LESSER_ARTIFACT_KEHR.getItem());
    fragmentChestMap.put(0.0403, ItemLibrary.LESSER_ARTIFACT_VULD.getItem());
    fragmentChestMap.put(0.025, ItemLibrary.LESSER_ARTIFACT_BONES.getItem());

    fragmentDigMap.put(0.003301, ItemLibrary.LESSER_ARTIFACT_ADEM.getItem());
    fragmentDigMap.put(0.003302, ItemLibrary.LESSER_ARTIFACT_KEHR.getItem());
    fragmentDigMap.put(0.003303, ItemLibrary.LESSER_ARTIFACT_VULD.getItem());
    fragmentDigMap.put(0.015, ItemLibrary.LESSER_ARTIFACT_BONES.getItem());

    for (Biome biome : Biome.values()) {
      if (biome.toString().contains("OCEAN")) {
        saltWaterBiomes.add(biome);
      }
    }

    saltWaterBiomes.add(Biome.BEACH);
    saltWaterBiomes.add(Biome.SNOWY_BEACH);

    for (Material mat : Material.values()) {
      if (mat.toString().contains("LOG") && !mat.toString().contains("LEGACY")) {
        logMap.put(mat, 6L);
      }
    }
    double elapsed = sw.stop().elapsed(TimeUnit.MICROSECONDS) / 1000D;
    elapsed = ((int) (elapsed * 10)) / 10D;
    Bukkit.getPluginManager().getPlugin("FruchtJobs").getLogger().info("Initialized JobExpGainManager [" + elapsed + "ms]");
  }

}