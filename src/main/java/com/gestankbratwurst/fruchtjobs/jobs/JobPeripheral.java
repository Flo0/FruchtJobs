package com.gestankbratwurst.fruchtjobs.jobs;

import com.gestankbratwurst.fruchtcore.FruchtCore;
import com.gestankbratwurst.fruchtcore.items.ItemActionManager;
import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtcore.recipes.RecipeModule;
import com.gestankbratwurst.fruchtcore.util.Msg;
import com.gestankbratwurst.fruchtcore.util.actionbar.ActionBarBoard.Section;
import com.gestankbratwurst.fruchtcore.util.actionbar.ActionBarManager;
import com.gestankbratwurst.fruchtcore.util.common.NameSpaceFactory;
import com.gestankbratwurst.fruchtcore.util.common.UtilMobs;
import com.gestankbratwurst.fruchtcore.util.common.UtilPlayer;
import com.gestankbratwurst.fruchtjobs.FruchtJobs;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.FurnaceBreadBun;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.FurnaceFilletRecipe;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.FurnaceAnemonFish;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.FurnaceHawkFish;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.FurnacePretzel;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.FurnaceShadFish;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.FurnaceShrimpFish;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.FurnaceSnapperFish;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.FurnaceTroutFish;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeBiomass;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeBreadDought;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeButcherKnife;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeChainBoots;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeChainChestplate;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeChainHelmet;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeChainLeggins;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeChains;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeClearDiamond;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeCobblePouch;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeDiaBoots;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeDiaChestplate;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeDiaHelmet;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeDiaLeggins;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeDough;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeFlour;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeGoldenLocket;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeGoldenNecklace;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeGoldenRing;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeHardLeather;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeHardLeatherBoots;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeHardLeatherChestplate;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeHardLeatherHelmet;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeHardLeatherLeggins;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeHomestone;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeIronArrow;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeIronArrowHead;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeJerky;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeLogPouch;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipePretzelDough;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeSieve;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeTreatedBow;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeTreatedIronArrow;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.RecipeTreatedWood;
import com.gestankbratwurst.fruchtjobs.jobs.recipes.impl.ReplaceRecipeArrow;
import com.gestankbratwurst.fruchtjobs.jobs.sieving.SieveManager;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 08.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public class JobPeripheral implements Listener {

  private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

  public static void init(FruchtCore core, FruchtJobs plugin) {
    Stopwatch sw = Stopwatch.createStarted();
    JobManager jobManager = plugin.getJobManager();
    ItemActionManager itemActionManager = core.getItemActionManager();
    RecipeModule recipeModule = core.getRecipeModule();
    ActionBarManager actionBarManager = core.getUtilModule().getActionBarManager();
    SieveManager sieveManager = plugin.getSieveManager();

    itemActionManager.registerInteractAction(ItemLibrary.COBBLE_POUCH.toString(), event ->
        jobManager.getOnlineHolder(event.getPlayer()).openSmallCobblePouch());

    itemActionManager.registerInteractAction(ItemLibrary.LOG_POUCH.toString(), event ->
        jobManager.getOnlineHolder(event.getPlayer()).openSmallLogPouch());

    itemActionManager.registerAttackAction(ItemLibrary.BUTCHER_KNIFER.toString(), ev -> {
      if (ev.getEntity() instanceof LivingEntity) {
        if (!UtilMobs.isDomesticated((LivingEntity) ev.getEntity())) {
          ev.setDamage(ev.getDamage() * 0.1D);
        }
      }
    });

    itemActionManager.registerKillAction(ItemLibrary.BUTCHER_KNIFER.toString(), (le, ev) -> {
      if (!(le instanceof Player)) {
        return;
      }
      Player player = (Player) le;
      JobHolder holder = jobManager.getOnlineHolder(player);
      if (!holder.isActive(JobType.FARMER)) {
        player.getInventory().setItemInMainHand(null);
        UtilPlayer.playSound(player, Sound.ENTITY_ITEM_BREAK);
        Msg.error(player, "Job", "Du bist kein " + Msg.elem(JobType.FARMER.getDisplayName()) +
            " und brichst deshalb aus Versehen das Messer ab...");
        return;
      }
      if (RANDOM.nextDouble() < 0.3333D) {
        Location dropLoc = ev.getEntity().getLocation();
        for (ItemStack drop : ev.getDrops()) {
          dropLoc.getWorld().dropItemNaturally(dropLoc, drop);
        }
        dropLoc.getWorld().spawnParticle(Particle.REDSTONE, dropLoc, 4, 0.5, 0.5, 0.5, new DustOptions(Color.RED, 1F));
      }
    });

    itemActionManager.registerInteractAction(ItemLibrary.HOME_STONE.toString(), event -> {
      final Player player = event.getPlayer();
      final PersistentDataContainer pdc = player.getPersistentDataContainer();
      final long now = System.currentTimeMillis();
      if (pdc.has(NameSpaceFactory.provide("HOME_STONE_CD"), PersistentDataType.LONG)) {
        long value = pdc.get(NameSpaceFactory.provide("HOME_STONE_CD"), PersistentDataType.LONG);
        long delta = now - value;
        if (delta < 1200000L) {
          String secs = Msg.elem((1200000L - delta) / 1000 + "s");
          Msg.error(player, "Heimstein", "Du muss noch " + secs + " warten, bis zur nächsten Teleportation.");
          return;
        }
      }
      final Location spawnLoc = player.getBedSpawnLocation();
      if (spawnLoc == null) {
        Msg.error(player, "Heimstein", "Du hast keinen Bett-spawn.");
        return;
      }
      Msg.send(player, "Heimstein", "Nicht bewegen.");
      actionBarManager.getBoard(player).getSection(Section.MIDDLE)
          .addTokenLayer(100L, "HOMESTONE", 100, () -> {
            int secs = 5 - (int) ((System.currentTimeMillis() - now) / 1000D);
            return "§6[ §eWarte noch §6" + secs + "§e Sekunden§6 ]";
          });
      UtilPlayer.forceWait(player, 100, true,
          p -> {
            p.teleport(spawnLoc);
            UtilPlayer.playSound(player, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.8F, 1.0F);
            pdc.set(NameSpaceFactory.provide("HOME_STONE_CD"), PersistentDataType.LONG, now);
          },
          p -> {
            Msg.error(p, "Heimstein", "Abgebrochen: Du hast dich bewegt oder Schaden erhalten.");
            UtilPlayer.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.8F, 0.5F);
          });
    });

    itemActionManager.registerInteractAction(ItemLibrary.SIEVE.toString(), event -> {
      JobHolder holder = jobManager.getOnlineHolder(event.getPlayer());
      Block block = event.getClickedBlock();
      if (block == null) {
        return;
      }
      Material mat = block.getType();
      if (holder.hasPerk(JobPerkType.SIEVEING) && (mat == Material.SAND || mat == Material.GRAVEL)) {
        if (block.getRelative(BlockFace.UP).getType() == Material.WATER) {
          sieveManager.openFor(holder, block);
        }
      }
    });

    itemActionManager.registerAttackAction("ONE_RANGED", event -> {
      if (event.getDamager() instanceof Projectile) {
        event.setDamage(event.getDamage() + 1);
      }
    });

    recipeModule.registerRecipe(new RecipeCobblePouch(jobManager));
    recipeModule.registerRecipe(new RecipeLogPouch(jobManager));
    recipeModule.registerRecipe(new RecipeButcherKnife(jobManager));
    recipeModule.registerRecipe(new FurnaceFilletRecipe());
    recipeModule.registerRecipe(new RecipeHomestone(jobManager));
    recipeModule.registerRecipe(new RecipeSieve(jobManager));
    recipeModule.registerRecipe(new RecipeHardLeather(jobManager));
    recipeModule.registerRecipe(new RecipeChains(jobManager));
    recipeModule.registerRecipe(new RecipeHardLeatherHelmet(jobManager));
    recipeModule.registerRecipe(new RecipeHardLeatherChestplate(jobManager));
    recipeModule.registerRecipe(new RecipeHardLeatherLeggins(jobManager));
    recipeModule.registerRecipe(new RecipeHardLeatherBoots(jobManager));
    recipeModule.registerRecipe(new RecipeChainHelmet(jobManager));
    recipeModule.registerRecipe(new RecipeChainChestplate(jobManager));
    recipeModule.registerRecipe(new RecipeChainLeggins(jobManager));
    recipeModule.registerRecipe(new RecipeChainBoots(jobManager));
    recipeModule.registerRecipe(new RecipeGoldenLocket(jobManager));
    recipeModule.registerRecipe(new RecipeGoldenNecklace(jobManager));
    recipeModule.registerRecipe(new RecipeGoldenRing(jobManager));
    recipeModule.registerRecipe(new RecipeIronArrow(jobManager));
    recipeModule.registerRecipe(new RecipeIronArrowHead(jobManager));
    recipeModule.registerRecipe(new RecipeTreatedBow(jobManager));
    recipeModule.registerRecipe(new RecipeTreatedIronArrow(jobManager));
    recipeModule.registerRecipe(new RecipeTreatedWood(jobManager));
    recipeModule.registerRecipe(new ReplaceRecipeArrow());
    recipeModule.registerRecipe(new RecipeClearDiamond(jobManager));
    recipeModule.registerRecipe(new RecipeDiaHelmet(jobManager));
    recipeModule.registerRecipe(new RecipeDiaChestplate(jobManager));
    recipeModule.registerRecipe(new RecipeDiaLeggins(jobManager));
    recipeModule.registerRecipe(new RecipeDiaBoots(jobManager));
    recipeModule.registerRecipe(new RecipeBiomass(jobManager));
    recipeModule.registerRecipe(new RecipeFlour(jobManager));
    recipeModule.registerRecipe(new RecipePretzelDough(jobManager));
    recipeModule.registerRecipe(new RecipeBreadDought(jobManager));
    recipeModule.registerRecipe(new RecipeDough(jobManager));

    recipeModule.registerRecipe(new FurnaceAnemonFish());
    recipeModule.registerRecipe(new FurnaceTroutFish());
    recipeModule.registerRecipe(new FurnaceHawkFish());
    recipeModule.registerRecipe(new FurnaceShadFish());
    recipeModule.registerRecipe(new FurnaceShrimpFish());
    recipeModule.registerRecipe(new FurnaceSnapperFish());
    recipeModule.registerRecipe(new RecipeJerky());
    recipeModule.registerRecipe(new FurnacePretzel());
    recipeModule.registerRecipe(new FurnaceBreadBun());

    Bukkit.getPluginManager().registerEvents(new JobPeripheral(core, plugin), plugin);
    double elapsed = sw.elapsed(TimeUnit.MICROSECONDS) / 1000D;
    elapsed = ((int) (elapsed * 10D)) / 10D;
    plugin.getLogger().info("Initialized JobPeripheral [" + elapsed + "ms]");
  }

  private final Set<Material> STONES = ImmutableSet.of(Material.COBBLESTONE, Material.STONE);
  private final Set<Material> LOGS = ImmutableSet.copyOf(Arrays.stream(Material
      .values())
      .filter(m -> m.toString().contains("LOG") && !m.toString().contains("LEGACY"))
      .collect(Collectors.toList()));

  private final FruchtCore core;
  private final FruchtJobs plugin;

  @EventHandler
  public void onClick(InventoryClickEvent event) {
    if (event.getView().getTitle().equals("Kleiner Steinbeutel")) {
      ItemStack item = event.getCurrentItem();
      if (item != null) {
        if (!STONES.contains(item.getType())) {
          event.setCancelled(true);
        }
      }
    } else if (event.getView().getTitle().equals("Kleiner Holzbeutel")) {
      ItemStack item = event.getCurrentItem();
      if (item != null) {
        if (!LOGS.contains(item.getType())) {
          event.setCancelled(true);
        }
      }
    }
  }

  @EventHandler
  public void onPickup(EntityPickupItemEvent event) {
    Entity entity = event.getEntity();

    if (entity instanceof Player) {
      Item item = event.getItem();
      ItemStack stack = item.getItemStack();
      Material pickup = stack.getType();
      Player player = (Player) entity;
      JobHolder holder = plugin.getJobManager().getOnlineHolder(player);
      ItemStack pouchItem = player.getInventory().getItem(9);
      if (pouchItem == null || pouchItem.getType() != Material.STICK) {
        return;
      }
      String pouchName = pouchItem.getItemMeta().getDisplayName();
      if (STONES.contains(pickup) && pouchName.equals("§aKleiner Steinbeutel")) {
        holder.getSmallCobblePouch().addItem(stack).values().forEach(remaining -> player.getInventory().addItem(remaining).values()
            .forEach(remainingLeft -> player.getWorld().dropItemNaturally(player.getLocation(), remainingLeft)));
        item.remove();
        UtilPlayer.playSound(player, Sound.ENTITY_ITEM_PICKUP, 0.3F, 1F);
        event.setCancelled(true);
      } else if (LOGS.contains(pickup) && pouchName.equals("§aKleiner Holzbeutel")) {
        holder.getSmallLogPouch().addItem(stack).values().forEach(remaining -> player.getInventory().addItem(remaining).values()
            .forEach(remainingLeft -> player.getWorld().dropItemNaturally(player.getLocation(), remainingLeft)));
        item.remove();
        UtilPlayer.playSound(player, Sound.ENTITY_ITEM_PICKUP, 0.3F, 1F);
        event.setCancelled(true);
      }
    }
  }

}
