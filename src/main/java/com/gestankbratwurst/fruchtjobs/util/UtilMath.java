package com.gestankbratwurst.fruchtjobs.util;

/*******************************************************
 * Copyright (C) Gestankbratwurst suotokka@gmail.com
 *
 * This file is part of FruchtJobs and was created at the 25.03.2020
 *
 * FruchtJobs can not be copied and/or distributed without the express
 * permission of the owner.
 *
 */
public class UtilMath {

  public static String getProgressBar(double progress, int length, String tile) {
    int fullTiles = (int) (length * progress);
    int emptyTiles = length - fullTiles;
    StringBuilder barBuilder = new StringBuilder();

    barBuilder.append("§a");
    for (int i = 0; i <= fullTiles; i++) {
      barBuilder.append(tile);
    }
    barBuilder.append("§c");
    for (int i = 0; i <= emptyTiles; i++) {
      barBuilder.append(tile);
    }

    return barBuilder.toString();
  }

}
