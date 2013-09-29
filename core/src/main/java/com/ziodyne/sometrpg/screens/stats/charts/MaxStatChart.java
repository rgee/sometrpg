package com.ziodyne.sometrpg.screens.stats.charts;

import com.badlogic.gdx.math.Polygon;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.screens.stats.StatChartUtils;

public class MaxStatChart extends RadarChart {
  public MaxStatChart(Unit unit) {
    super(unit);
  }

  @Override
  protected Polygon generateChart(Unit unit) {
    return StatChartUtils.getMaxStatRadarChart(unit, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
  }
}