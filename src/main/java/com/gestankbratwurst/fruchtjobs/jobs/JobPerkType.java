package com.gestankbratwurst.fruchtjobs.jobs;

import com.gestankbratwurst.fruchtcore.util.items.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 28.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
@AllArgsConstructor
public enum JobPerkType {

  STONE_HUNTER("Stein verliebt", Material.DIORITE,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 5),
  ORE_HUNTER("Erz verliebt", Material.COAL_ORE,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 5),
  TREE_HUNTER("Baum verliebt", Material.OAK_LOG,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 5),
  PLANT_HUNTER("Pflanzen verliebt", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 5),
  CAVE_DIVER("Höhlentaucher", Material.COBBLESTONE,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 8),
  FOREST_WANDERER("Waldkundig", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 8),
  PLAIN_RUNNER("Wiesenläufer", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 8),
  SECRET_ORE_VEINS("Versteckte Adern", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 10),
  TAR_KNOWLEDGE("Harzkunde", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 10),
  HERBAL_KNOWLEDGE("Kräuterkunde", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 10),
  RECIPE_SMALL_STONE_POUCH("Rezept: §eKleiner Steinbeutel", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 13),
  RECIPE_SMALL_WOOD_POUCH("Rezept: §eKleiner Holzbeutel", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 13),
  LUCKY_HERB("Glücksgriff", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 13),
  STONE_SALT("Steinsalz", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 15),
  SILKY_MINING("Behutsames Minen", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 17),
  SILKY_WOODCUTTING("Behutsames Holz Hacken", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 17),
  HERBAL_TOOLS("Kräuterwerkzeug", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.GATHERER, 17),

  BETTER_HARVEST("Bessere Ernte", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 3),
  BETTER_BUTCHER("Besseres Schlachten", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 3),
  BETTER_HUNTING("Besseres Jagen", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 3),
  GREEN_THUMB("Grüner Daumen", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 6),
  RECIPE_BUTCHER_KNIFE("Rezept: §eSchlachtermesser", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 6),
  WILD_HUNT("Wildern", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 6),
  FARM_SOIL_RUNNER("Feldläufer", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 9),
  LEARNED_BUTCHER("Gelerntes schlachten", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 9),
  THE_HUNT_MUST_GO_ON("Jagdinstinkt", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 9),
  RECIPE_FLOUR_BIOMASS("Rezepte: §eMehl und Biomasse", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 13),
  RECIPES_BUTTER_AND_CLOTH("Rezepte: §eButter und Stoff", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 13),
  HEADHUNTER("Headhunter", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 13),

  DIGGER("Gräber", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 3),
  BONE_FINDER("Schatzfinder", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 3),
  ADVENTURE_RUNNER("Abenteuerläufer", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 6),
  NETHER_RUNNER("Netherläufer", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 6),
  SPADE_FINDINGS("Schaufelfunde", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 9),
  NETHER_SEARCHER("Nethersucher", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 9),
  FRAGMENT_SEARCH("Fragmentsuche", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 12),
  FRAGMENT_DIG("Fragmentgräber", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 12),
  RECIPE_HOME_STONE("Rezept: §eHeimstein", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 14),
  SIEVEING("Schürfen", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 16),
  LOOT_DIGGER_I("Schatzgräber I", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 18),
  ARCHEOLOGY_I("Archeologie I", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 18),

  SMITH_APPRENTICE("Schmiedelehrling", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 3),
  CRAFT_APPRENTICE("Handwerkslehrling", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 3),
  RECIPES_ROBUST_ITEMS("Robuste Items", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 6),
  GOLD_SMITH("Goldschmuck", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 10),
  PREPARATIONS("Handwerks Präparate", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 10),
  LOSSLESS_REPAIRING("Verlustfreies Reparieren", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 14),
  RECIPES_GRINDSTONES("Rezepte: §eSchleifsteine", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 14),
  RECIPE_METALL_PLATES("Rezept: §eMetallplatten", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 17),
  RECIPE_SMALL_POUCH("Rezept: §eKleiner Beutel", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 17),

  RECIPES_CAULDRON_POTIONS_I("Rezepte: §eKesseltränke I", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 3),
  RECIPES_SIMPLE_SCROLLS("Rezepte: §eEinfache Schriftrollen", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 3),
  RECIPES_CAULDRON_POTIONS_II("Rezepte: §eKesseltränke II", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 11),
  RECIPES_ENCHANTMENTS_I("Rezepte: §eVerzauberungen I", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 11),
  RECIPES_TALISMANS_I("Rezepte: §eTalismane I", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 18),
  RECIPES_SCROLLS("Rezepte: §eSchriftrollen", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 18),

  SWEET_WATER_FISHING("Süßwasserfischen I", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 5),
  SALT_WATER_FISHING("Salzwasserfischen I", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 5),
  RECIPE_SALT("Rezept: Salz", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 7),
  RECIPE_FISHING_NET("Rezept: Fischernetz", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 10),
  RECIPES_FISHING_TRAPS("Rezepte: Muschelsieb & Hummerfalle", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 13),
  RECIPE_DIVING_FINS("Rezepte: §eTaucherflossen & Korallen-Netz", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 13),
  RECIPE_SEA_FOOD("Rezepte: §eAnglereintopf & Tiefseeeintopf", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 15),
  RECIPE_BAITED_FISHING_ROD("Rezept: §eKöder Angel", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 17),
  RECIPE_HARPUN("Rezept: §eHarpune", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 17),
  RECIPE_DIVING_HELMET("Rezepte: §eTaucherhelm & Tiefsee Sieb", Material.GRASS,
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 17);

  @Getter
  private final String displayName;
  private final Material iconMaterial;
  private final String[] description;
  @Getter
  private final JobType jobType;
  @Getter
  private final int level;

  public ItemStack getPerkChooseIcon(JobHolder jobHolder) {
    ItemBuilder builder = new ItemBuilder(this.iconMaterial);

    boolean isLevel = jobHolder.getLvl(this.jobType) >= this.level;

    builder.name("§6" + this.displayName);
    builder.lore("");
    builder.lore("§eLevel: " + (isLevel ? "§a" : "§c") + level);
    builder.lore("");
    builder.lore(description);
    builder.lore("");
    builder.lore("§7Nur ein Perk per Level.");
    builder.lore("");
    if (jobHolder.hasPerk(this)) {
      builder.lore("§6[§aAusgewählt§6]");
      builder.enchant(Enchantment.ARROW_FIRE, 1);
    } else if (isLevel) {
      builder.lore("§6[§eKlicke zum Auswählen§6]");
    } else {
      builder.lore("§c[§7Vorraussetzungen nicht erfüllt§c]");
    }

    return builder.build();
  }

}
