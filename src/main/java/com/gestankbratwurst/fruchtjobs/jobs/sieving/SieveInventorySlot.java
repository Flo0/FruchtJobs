package com.gestankbratwurst.fruchtjobs.jobs.sieving;

import com.gestankbratwurst.fruchtcore.items.ItemLibrary;
import com.gestankbratwurst.fruchtjobs.jobs.JobHolder;
import com.google.common.collect.ImmutableMap;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 10.04.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class SieveInventorySlot {

  private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
  private static final ImmutableMap<Double, Supplier<ItemStack>> DROP_MAP = ImmutableMap.<Double, Supplier<ItemStack>>builder()
      .put(0.25, () -> {
        ItemStack item = new ItemStack(Material.BONE);
        if (RANDOM.nextBoolean()) {
          item.add();
        }
        return item;
      })
      .put(0.121, () -> {
        ItemStack item = new ItemStack(Material.BLAZE_POWDER);
        if (RANDOM.nextBoolean()) {
          item.add();
        }
        return item;
      })
      .put(0.142, () -> {
        ItemStack item = new ItemStack(Material.BONE_MEAL);
        item.add();
        if (RANDOM.nextBoolean()) {
          item.add(RANDOM.nextInt(1, 3));
        }
        return item;
      })
      .put(0.129, () -> {
        ItemStack item = new ItemStack(Material.LAPIS_LAZULI);
        return item;
      })
      .put(0.051, () -> {
        ItemStack item = new ItemStack(Material.DIAMOND);
        if (RANDOM.nextBoolean()) {
          item.add();
        }
        return item;
      })
      .put(0.0502, () -> {
        ItemStack item = new ItemStack(Material.EMERALD);
        return item;
      })
      .put(0.0101, () -> {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET);
        item.add(RANDOM.nextInt(5, 8));
        return item;
      })
      .put(1.00, () -> {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET);
        item.add(RANDOM.nextInt(1, 2));
        return item;
      })
      .build();

  private static final ImmutableMap<Double, Supplier<ItemStack>> GEODE_MAP = ImmutableMap.<Double, Supplier<ItemStack>>builder()
      .put(0.201, () -> {
        ItemStack item = new ItemStack(Material.BLAZE_POWDER);
        item.add(10);
        return item;
      })
      .put(0.1034, () -> {
        ItemStack item = new ItemStack(Material.LAPIS_LAZULI);
        item.add(RANDOM.nextInt(6, 12));
        return item;
      })
      .put(0.0805, () -> {
        ItemStack item = new ItemStack(Material.DIAMOND);
        if (RANDOM.nextBoolean()) {
          item.add(RANDOM.nextInt(2, 4));
        }
        return item;
      })
      .put(0.0806, () -> {
        ItemStack item = new ItemStack(Material.EMERALD);
        if (RANDOM.nextBoolean()) {
          item.add(RANDOM.nextInt(2, 4));
        }
        return item;
      })
      .put(0.01001, ItemLibrary.LESSER_ARTIFACT_BONES::getItem)
      .put(0.005001, ItemLibrary.LESSER_ARTIFACT_ADEM::getItem)
      .put(0.005002, ItemLibrary.LESSER_ARTIFACT_KEHR::getItem)
      .put(0.005003, ItemLibrary.LESSER_ARTIFACT_VULD::getItem)
      .put(1.00, () -> {
        ItemStack item = new ItemStack(Material.GOLD_NUGGET);
        item.add(RANDOM.nextInt(20, 40));
        return item;
      })
      .build();

  protected SieveInventorySlot(SieveInventoryType type) {
    this.layers = new Stack<>();
    this.type = type;
    fill();
  }

  private final SieveInventoryType type;
  private final Stack<SieveLayerItem> layers;

  protected ItemStack getDisplay() {
    if (layers.isEmpty()) {
      return null;
    }
    return layers.peek().getDisplayItem();
  }

  protected boolean onClick(InventoryClickEvent event, JobHolder holder, boolean withSieve) {
    if (layers.isEmpty()) {
      return false;
    }
    SieveLayerItem layerItem = layers.peek();
    if (layerItem.onClick(event, holder, withSieve)) {
      layers.pop();
      return true;
    }
    return false;
  }

  private void fill() {
    if (RANDOM.nextBoolean()) {
      ItemStack item = null;
      for (Entry<Double, Supplier<ItemStack>> entry : DROP_MAP.entrySet()) {
        if (RANDOM.nextDouble() <= entry.getKey()) {
          item = entry.getValue().get();
          break;
        }
      }
      layers.push(new ItemLayer(item));
    } else if (RANDOM.nextDouble() < 0.1D) {
      ItemStack item = null;
      for (Entry<Double, Supplier<ItemStack>> entry : GEODE_MAP.entrySet()) {
        if (RANDOM.nextDouble() <= entry.getKey()) {
          item = entry.getValue().get();
          break;
        }
      }
      layers.push(new GeodeLayer(item));
    }
    if (RANDOM.nextBoolean()) {
      layers.push(new StoneSieveLayer());
    }
    layers.push(new TopSieveLayer(type));
  }

}
