package com.ziodyne.sometrpg.view.stats;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils;
import com.ziodyne.sometrpg.logic.models.Character;

public class StatChartUtilsTest {
  
  private static float epsilon = 0.00001f;

  static void assertEpsilonEquals(Vector2 expected, Vector2 actual, float epsilon) {
    if (!expected.epsilonEquals(actual, epsilon)) {
      Assert.fail();
    }
  }
  
  @Test
  public void testGrowthPolygonGeneration() {
    Character testCharacter = ModelTestUtils.createMaxedUnit();
    
    int radius = 5;
    Polygon radarChart = StatChartUtils.getStatRadarChart(testCharacter, StatChartUtils.DEFAULT_CHARTED_STATS, radius);
    
    // We expect 2 vertices per stat because the 2d vertices are stored in a flattened array
    Assert.assertEquals(radarChart.getTransformedVertices().length, StatChartUtils.DEFAULT_CHARTED_STATS.size()*2);
    
    float[] tVerts = radarChart.getTransformedVertices();   
    
    // Compare the distance between two opposing points on the chart.
    Vector2 firstPoint = new Vector2(tVerts[0], tVerts[1]);
    Vector2 opposingPoint = new Vector2(tVerts[4], tVerts[5]);
    
    Assert.assertEquals(radius*2, firstPoint.dst(opposingPoint), epsilon);
    
    Character crappyCharacter = ModelTestUtils.createCrappyUnit();
    Polygon crappyRadarChart = StatChartUtils.getGrowthRadarChart(crappyCharacter, StatChartUtils.DEFAULT_CHARTED_STATS, 0);
    
    float[] vertices = crappyRadarChart.getTransformedVertices();
    for (float vertex : vertices) {
      if (Math.abs(0 - vertex) > epsilon) {
        Assert.fail("Zero radius radar chart has non-zero (fuzzy match) vertex...");
      }
    }
  }
  
  @Test
  public void testVertexScaling() {
    int radius = 5;
    List<Float> scalingFactors = Lists.newArrayList(0.5f, 0.5f);
    List<Vector2> vectors = StatChartUtils.getScaledChartVertices(scalingFactors, radius);
    Assert.assertEquals(scalingFactors.size(), vectors.size());

    
    Vector2 expectedFirst = new Vector2(0, 1).scl(radius*0.5f);
    Vector2 expectedSecond = new Vector2(0, -1).scl(radius*0.5f);
    
    Vector2 actualFirst = vectors.get(0);
    Vector2 actualSecond = vectors.get(1);
    
    assertEpsilonEquals(expectedFirst, actualFirst, epsilon);
    assertEpsilonEquals(expectedSecond, actualSecond, epsilon);
  }
  

  @Test
  public void testVertexArrayConversion() {
    float[] vertices = new float[]{0f, 1f, 2f, 3f};
    List<Vector2> vectors = Lists.newArrayList(new Vector2(vertices[0], vertices[1]), new Vector2(vertices[2], vertices[3]));
    
    Assert.assertArrayEquals(vertices, StatChartUtils.toVertexArray(vectors), epsilon);
  }
}
