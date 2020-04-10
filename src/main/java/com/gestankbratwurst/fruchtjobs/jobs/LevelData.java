package com.gestankbratwurst.fruchtjobs.jobs;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.Getter;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 24.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class LevelData {

  private static long evaluateExp(int level) {
    if (level < 1) {
      return 0;
    }
    return level * level + 100 + (5 * level);
  }

  private static final RangeMap<Long, Integer> EXP_CURVE_MAPPING = TreeRangeMap.create();
  private static final Int2ObjectMap<Range<Long>> LVL_CURVE_MAPPING = new Int2ObjectOpenHashMap<>();

  static {
    long lastExp = 0;
    long nextExp;
    for (int lvl = 1; lvl < 100; lvl++) {
      nextExp = lastExp + evaluateExp(lvl);
      Range<Long> expRange = Range.closedOpen(lastExp, nextExp);
      lastExp = nextExp;
      EXP_CURVE_MAPPING.put(expRange, lvl);
      LVL_CURVE_MAPPING.put(lvl, expRange);
    }
    EXP_CURVE_MAPPING.put(Range.atLeast(evaluateExp(99) + evaluateExp(100)), 100);
    LVL_CURVE_MAPPING.put(100, Range.atLeast(evaluateExp(99) + evaluateExp(100)));
  }


  public LevelData(long exp) {
    this.exp = exp;
    this.checkLevel(true);
  }

  public LevelData(long exp, int level) {
    this.exp = exp;
    this.level = level;
    this.checkLevel(true);
  }

  public LevelData() {
    this(0, 1);
  }

  @Getter
  private int level;
  @Getter
  private long exp;

  public double getProgress() {
    long diff = getExpLevelDifference();
    long prog = getExpToNextLevel();
    return 1.0D / diff * (diff - prog);
  }

  public long getExpLevelDifference() {
    return getExpForLevel(this.level + 1) - getExpForLevel(this.level);
  }

  public long getExpToNextLevel() {
    return this.getExpForLevel(this.level + 1) - this.exp;
  }

  protected void addExp(long value) {
    this.exp += value;
    this.checkLevel(false);
  }

  public void removeExp(long value) {
    this.exp -= value;
    this.checkLevel(false);
  }

  private void checkLevel(boolean silent) {
    int desiredLevel = getLevelFromExp(this.exp);
    if (desiredLevel > this.level) {
      this.levelUp(silent);
      checkLevel(silent);
    } else if (desiredLevel < this.level) {
      this.levelDown(silent);
    }
  }

  private void levelUp(boolean silent) {
    this.level += 1;
  }

  private void levelDown(boolean silent) {
    this.level -= 1;
  }

  private int getLevelFromExp(long exp) {
    return EXP_CURVE_MAPPING.get(exp);
  }

  private long getExpForLevel(int level) {
    return LVL_CURVE_MAPPING.get(level).lowerEndpoint();
  }

}
