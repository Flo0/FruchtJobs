package com.gestankbratwurst.fruchtjobs.jobs;

import com.gestankbratwurst.fruchtcore.resourcepack.skins.Model;
import com.gestankbratwurst.fruchtcore.util.items.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
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

  STONE_HUNTER("Steinsammler", new ItemStack(Material.DIORITE),
      new String[]{"§fDu erhältst alle §e20", "§fSteine §e20 §fzusätzliche", "§fErfahrung."},
      JobType.GATHERER, 5),
  ORE_HUNTER("Erzsammler", new ItemStack(Material.COAL_ORE),
      new String[]{"§fErze geben §e10%§f mehr", "§fErfahrung."},
      JobType.GATHERER, 5),
  TREE_HUNTER("Holzsammler", new ItemStack(Material.OAK_WOOD),
      new String[]{"§fBäume geben §e10%§f mehr", "§fErfahrung."},
      JobType.GATHERER, 5),
  PLANT_HUNTER("Pflanzensammler", new ItemStack(Material.POPPY),
      new String[]{"§fPflanzen und Pilze geben", "§e10%§f mehr Erfahrung."},
      JobType.GATHERER, 5),
  CAVE_DIVER("Höhlentaucher", Model.CAVE_DIVER_ICON.getItem(),
      new String[]{"§fWenn du dich unter der", "§fHöhe §e56 §fbewegst, erhältst", "§fdu einen §eSpeed I§f Buff."},
      JobType.GATHERER, 8),
  FOREST_WANDERER("Waldkundig", Model.FOREST_WANDERER_ICON.getItem(),
      new String[]{"§fDu erhälst §eSpeed I", "§fin §eWaldbiomen§f."},
      JobType.GATHERER, 8),
  PLAIN_RUNNER("Wiesenläufer", Model.PLAIN_RUNNER_ICON.getItem(),
      new String[]{"§fDu erhälst §eSpeed I", "§fin §eWiesen/Blumen - Biomen§f."},
      JobType.GATHERER, 8),
  SECRET_ORE_VEINS("Versteckte Adern", Model.SECRET_ORE_VEINS_ICON.getItem(),
      new String[]{"§fDu findest selten §eErze", "§faus allen §eSteinarten§f."},
      JobType.GATHERER, 10),
  TAR_KNOWLEDGE("Harzkunde", Model.RESIN.getItem(),
      new String[]{"§fDu kannst §eHarz §fbeim", "§fFällen von §eBäumen§f finden."},
      JobType.GATHERER, 10),
  HERBAL_KNOWLEDGE("Kräuterkunde", Model.ROOTS.getItem(),
      new String[]{"§fDu findest seltene §ePflanzen-", "§eteile§f, wie §eWurzeln§f bei §eBlumen", "§fund §eNebenpilze§f bei §ePilzen§f."},
      JobType.GATHERER, 10),
  RECIPE_SMALL_STONE_POUCH("Rezept: §eKleiner Steinbeutel", Model.COBBLE_POUCH.getItem(),
      new String[]{"§fDu kannst einen kleinen", "§eSteinbeutel §fcraften, der", "§eStein §fund §eCobble §fautomatisch", "§faufnimmt."},
      JobType.GATHERER, 13),
  RECIPE_SMALL_WOOD_POUCH("Rezept: §eKleiner Holzbeutel", Model.LOG_POUCH.getItem(),
      new String[]{"§fDu kannst einen kleinen", "§eHolzbeutel §fcraften, der", "§eHolz §fautomatisch aufnimmt."},
      JobType.GATHERER, 13),
  LUCKY_HERB("Glücksgriff", Model.LUCKY_CLOVER.getItem(),
      new String[]{"§fDu kannst selten", "§eGlücksklee §fbei Pflanzen/Pilzen finden."},
      JobType.GATHERER, 13),
  STONE_SALT("Steinsalz", Model.SALT.getItem(),
      new String[]{"§fStein droppt selten Salz."},
      JobType.GATHERER, 15),
  SILKY_MINING("Behutsames Minen", new ItemStack(Material.IRON_PICKAXE),
      new String[]{"§fDu hast eine Chance von", "§e10%§f, den §edoppelten Ertrag§f aus", "§eErzen §fzu erhalten."},
      JobType.GATHERER, 17),
  SILKY_WOODCUTTING("Behutsames Holz Hacken", new ItemStack(Material.IRON_AXE),
      new String[]{"§fDu hast eine Chance von", "§e10%§f, den §edoppelten Ertrag§f aus", "§eHolz §fzu erhalten."},
      JobType.GATHERER, 17),
  HERBAL_TOOLS("Kräuterwerkzeug", new ItemStack(Material.IRON_HOE),
      new String[]{"§fDie Chance auf §ezusätzliche", "§fDrops bei §ePflanzen §fist ver-", "§fdoppelt, wenn sie mit einer",
          "§eSense §fabgebaut werden."},
      JobType.GATHERER, 17),

  BETTER_HARVEST("Bessere Ernte", new ItemStack(Material.WHEAT),
      new String[]{"§fDas Abbauen von Nutzpflanzen", "§fbringt §e10% §fmehr Erfahrung."},
      JobType.FARMER, 3),
  BETTER_BUTCHER("Bessere Tierhaltung", new ItemStack(Material.WHITE_WOOL),
      new String[]{"§fSchlachten: §e+10% Exp", "§fScheren: §e+20% Exp", "§fMelken: §e+37.5% Exp", "§fVermehren: §e+42.5%Exp"},
      JobType.FARMER, 3),
  BETTER_HUNTING("Besseres Jagen", new ItemStack(Material.BONE),
      new String[]{"§fDas Jagen von wilden Tieren", "§fbringt §e33% §fmehr Erfahrung."},
      JobType.FARMER, 3),
  GREEN_THUMB("Grüner Daumen", new ItemStack(Material.IRON_HOE),
      new String[]{"§fDu kannst eine §eSense §fver-", "§fwenden, um §eNutzpflanzen §fmit", "§eRechtsklick §fdirekt zu ernten und",
          "§fsie wieder nachzupflanzen."},
      JobType.FARMER, 6),
  RECIPE_BUTCHER_KNIFE("Rezept: §eSchlachtermesser", Model.BUTCHER_KNIFE.getItem(),
      new String[]{"§fDu kannst ein §eSchlachtermesser", "§fcraften, welches die §eDropmenge §fbei", "§eNutztieren §ferhöht."},
      JobType.FARMER, 6),
  WILD_HUNT("Wildern", new ItemStack(Material.BOW),
      new String[]{"§eWilde Tiere §fhaben eine Chance", "§fvon §e18.5% §fdoppelt zu droppen."},
      JobType.FARMER, 6),
  FARM_SOIL_RUNNER("Feldläufer", new ItemStack(Material.FARMLAND),
      new String[]{"§fDu erhälst einen §eSpeed I", "§fBuff auf §eGras§f, §eErde §fund §eFeldern§f."},
      JobType.FARMER, 9),
  LEARNED_BUTCHER("Gelerntes Schlachten", Model.FILLET.getItem(),
      new String[]{"§fZusätzliche Drops:", "§f- Filet bei Kühen, Schweinen, Schafen, Hasen", "§f- Federn bei Hühnern",
          "§f- Schnur beim Scheren"},
      JobType.FARMER, 9),
  THE_HUNT_MUST_GO_ON("Jagdinstinkt", Model.THE_HUNT_MUST_GO_ON_ICON.getItem(),
      new String[]{"§fNach einem Wildtier-Kill bekommst", "§fdu für §e14 Sek §feinen §eSpeed II §fBuff."},
      JobType.FARMER, 9),
  RECIPE_FLOUR_BIOMASS("Rezepte: §eMehl und Biomasse", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 13),
  RECIPES_BUTTER_AND_CLOTH("Rezepte: §eButter und Stoff", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 13),
  HEADHUNTER("Headhunter", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FARMER, 13),

  DIGGER("Gräber", new ItemStack(Material.IRON_SHOVEL),
      new String[]{"§fDu bekommst §e10% §fmehr Erfahrung von:", "§f - Sand", "§f - Kies", "§f - Clay",
          "§fDu bekommst §e15% §fmehr Erfahrung von:", "§f - Podzol", "§f - Mycelium", "§f - Mossy Cobble",
          "§f - Spawnern", "§f - Spinnen Netzen", "§f - Netherquarz", "§f - Magma Block"},
      JobType.ADVENTURER, 3),
  BONE_FINDER("Schatzfinder", new ItemStack(Material.CHEST),
      new String[]{"§fDU erhälst §e25% §fmehr Erfahrung", "§fund zusätzliche Knochendrops", "§fdurch das Ausgraben von Knochenblöcken.", "",
          "§fDu erhälst §e10% §fmehr Erfahrung", "§fdurch das öffnen von §eKisten§f und das", "§fAbbauen von §eSmokern§f, §eAmbossen etc."},
      JobType.ADVENTURER, 3),
  ADVENTURE_RUNNER("Abenteuerläufer", new ItemStack(Material.SAND),
      new String[]{"§fDu bekommst einen §eSpeed I§f Buff", "§fin §eWüsten §fund §eSümpfen."},
      JobType.ADVENTURER, 6),
  NETHER_RUNNER("Netherläufer", new ItemStack(Material.NETHERRACK),
      new String[]{"§fDu bekommst einen §eSpeed I §fBuff", "§fim §eNether§f."},
      JobType.ADVENTURER, 6),
  SPADE_FINDINGS("Schaufelfunde", Model.SPADE_FINDINGS_ICON.getItem(),
      new String[]{"§fViele zusätzliche Drops", "§faus allen Erden, Kies und Sand."},
      JobType.ADVENTURER, 9),
  NETHER_SEARCHER("Nethersucher", Model.NETHER_SEARCHER_ICON.getItem(),
      new String[]{"§fSoulsand droppt nun:", "§f - Knochen", "§f - Netherquarz", "§f - Blazerods", "§f - Blazepowder", "§f - Gunpowder", ""
          , "§fAußerdem erhälst du §e25%", "§fmehr Erfahrung aus §eKisten §fim §eNether§f."},
      JobType.ADVENTURER, 9),
  FRAGMENT_SEARCH("Fragmentsuche", Model.LESSER_ARTIFACT_ADEM.getItem(),
      new String[]{"§fDu kannst §eniedere Artefakte §f", "§fin Kisten finden."},
      JobType.ADVENTURER, 12),
  FRAGMENT_DIG("Fragmentgräber", Model.LESSER_ARTIFACT_BONES.getItem(),
      new String[]{"§fDu kannst §eniedere Artefakte §fbeim", "§fgraben finden. Ins besondere", "§fin Knochenblöcken."},
      JobType.ADVENTURER, 12),
  RECIPE_HOME_STONE("Rezept: §eHeimstein", Model.HOME_STONE.getItem(),
      new String[]{"§fTeleportiert dich nach Hause."},
      JobType.ADVENTURER, 14),
  SIEVEING("Schürfen", Model.SIEVE_SIEVE.getItem(),
      new String[]{"§fErlaubt es dir Schürfequipment zu", "§fcraften und es zum Schürfen zu", "§fbenutzen."},
      JobType.ADVENTURER, 16),
  LOOT_DIGGER_I("Schatzgräber I", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 18),
  ARCHEOLOGY_I("Archeologie I", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ADVENTURER, 18),

  SMITH_APPRENTICE("Schmiedelehrling", new ItemStack(Material.SMITHING_TABLE),
      new String[]{"§fWaffen und Rüstungen aus §eMetall", "§fund §eDiamant§f geben §e10% §fmehr Erfahrung."},
      JobType.CRAFTER, 3),
  CRAFT_APPRENTICE("Handwerkslehrling", new ItemStack(Material.FLETCHING_TABLE),
      new String[]{"§fPfeile, Bögen, Lederrezepte, Eimer etc", "§fgeben §e15% §fmehr Erfahrung."},
      JobType.CRAFTER, 3),
  RECIPES_CHAINS("Rezepte: §eKetten", Model.CHAINS.getItem(),
      new String[]{"§fDu kannst Ketten herstellen.", "§fDiese werden für Ketten-", "§früstungen verwendet."},
      JobType.CRAFTER, 6),
  RECIPES_HARD_LEATHER("Rezepte: §eGehärtetes Leder", Model.HARD_LEATHER.getItem(),
      new String[]{"§fDu kannst gehärtetes Leder herstellen.", "§fDies wird für harte Leder-", "§früstungen verwendet."},
      JobType.CRAFTER, 6),
  GOLD_SMITH("Goldschmuck", Model.GOLDEN_NECKLACE.getItem(),
      new String[]{"§fDu kannst Goldschmuck schmieden."},
      JobType.CRAFTER, 10),
  PREPARATIONS("Behandeltes Holz", Model.TREATED_BOW.getItem(),
      new String[]{"§fDu kannst Holz behandeln", "§fund daraus Bögen und Pfeile herstellen."},
      JobType.CRAFTER, 10),
  LOSSLESS_REPAIRING("Verlustfreies Reparieren", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 14),
  RECIPES_GRINDSTONES("Rezepte: §eSchleifsteine", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 14),
  RECIPE_METALL_PLATES("Rezept: §eMetallplatten", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 17),
  RECIPE_SMALL_POUCH("Rezept: §eKleiner Beutel", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.CRAFTER, 17),

  RECIPES_CAULDRON_POTIONS_I("Rezepte: §eKesseltränke I", new ItemStack(Material.CAULDRON),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 3),
  RECIPES_SIMPLE_SCROLLS("Rezepte: §eEinfache Schriftrollen", new ItemStack(Material.LECTERN),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 3),
  RECIPES_CAULDRON_POTIONS_II("Rezepte: §eKesseltränke II", new ItemStack(Material.GRASS),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 11),
  RECIPES_ENCHANTMENTS_I("Rezepte: §eVerzauberungen I", new ItemStack(Material.ENCHANTED_BOOK),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 11),
  RECIPES_TALISMANS_I("Rezepte: §eTalismane I", new ItemStack(Material.GRASS),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 18),
  RECIPES_SCROLLS("Rezepte: §eSchriftrollen", new ItemStack(Material.GRASS),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.ALCHEMIST, 18),

  SWEET_WATER_FISHING("Süßwasserfischen I", Model.TROUT.getItem(),
      new String[]{"§fDu kannst neue Fische fangen:", "§f- Forelle", "§f- Süßwasser Shrimp", "§f- Alse", "",
          "§fDiese geben §e30% §fmehr Erfahrung"},
      JobType.FISHER, 3),
  SALT_WATER_FISHING("Salzwasserfischen I", Model.HAWK_FISH.getItem(),
      new String[]{"§fDu kannst neue Fische fangen:", "§f- Falken Fisch", "§f- Anemonen Fisch", "§f- Schnapper Fisch", "",
          "§fDiese geben §e30% §fmehr Erfahrung"},
      JobType.FISHER, 3),
  RECIPE_SALT("Rezept: Salz", Model.SALT.getItem(),
      new String[]{"§fDu kannst Salz aus dem Meer", "§fabschöpfen, indem du mit einem", "§fPapier im Salzwasser rechtsklickst."},
      JobType.FISHER, 5),
  RECIPE_FISHING_NET("Rezept: Fischernetz", new ItemStack(Material.GRASS),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 8),
  RECIPES_FISHING_TRAPS("Rezepte: Muschelsieb & Hummerfalle", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 12),
  RECIPE_DIVING_FINS("Rezepte: §eTaucherflossen & Korallen-Netz", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 12),
  RECIPE_SEA_FOOD("Rezepte: §eAnglereintopf & Tiefseeeintopf", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 15),
  RECIPE_BAITED_FISHING_ROD("Rezept: §eKöder Angel", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 17),
  RECIPE_HARPUN("Rezept: §eHarpune", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 17),
  RECIPE_DIVING_HELMET("Rezepte: §eTaucherhelm & Tiefsee Sieb", Model.RED_X.getItem(),
      new String[]{"§fBeschreibung", "§fkommt noch."},
      JobType.FISHER, 17);

  @Getter
  private final String displayName;
  private final ItemStack iconItem;
  private final String[] description;
  @Getter
  private final JobType jobType;
  @Getter
  private final int level;

  public ItemStack getPerkChooseIcon(JobHolder jobHolder) {
    ItemBuilder builder = new ItemBuilder(iconItem);

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
      builder.flag(ItemFlag.HIDE_ENCHANTS);
    } else if (isLevel) {
      builder.lore("§6[§eKlicke zum Auswählen§6]");
    } else {
      builder.lore("§c[§7Vorraussetzungen nicht erfüllt§c]");
    }

    return builder.build();
  }

}
