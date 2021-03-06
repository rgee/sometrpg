package com.ziodyne.sometrpg.view.navigation;

import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PathUtilsTest {
  @Test
  public void testSingleMove() {
    Path<GridPoint2> path = new Path.Builder<>(new GridPoint2(1, 0))
            .addPoint(new GridPoint2(1, 1))
            .build();

    List<PathSegment> segs = PathUtils.segmentPath(path);
    Assert.assertEquals("There should only be one path segment", 1, segs.size());
  }

  @Test
  public void testSegmentPath() throws Exception {
    Path<GridPoint2> path = new Path.Builder<GridPoint2>(new GridPoint2(1, 0))
            .addPoint(new GridPoint2(0,0))
            .addPoint(new GridPoint2(0,1))
            .addPoint(new GridPoint2(0,2))
            .addPoint(new GridPoint2(0,3))
            .addPoint(new GridPoint2(1,3))
            .addPoint(new GridPoint2(2,3))
            .build();

    List<PathSegment> segmentedPath = PathUtils.segmentPath(path);
    PathSegment last = segmentedPath.get(segmentedPath.size() - 1);
    Assert.assertEquals("The type of the last segment should be south", PathSegment.Type.S, last.getType());

    PathSegment firstCurve = segmentedPath.get(2);
    Assert.assertEquals("The fourth path segment should be a west to south turn.", PathSegment.Type.W2S, firstCurve.getType());

    PathSegment postCurveStraight = segmentedPath.get(4);
    Assert.assertEquals("The fifth path segment after the turn should continue south.", PathSegment.Type.S, postCurveStraight.getType());
  }
}
