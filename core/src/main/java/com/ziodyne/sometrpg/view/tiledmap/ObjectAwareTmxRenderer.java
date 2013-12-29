package com.ziodyne.sometrpg.view.tiledmap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;

/**
 * An extension to {@link OrthogonalTiledMapRenderer} that supports tile map objects to render
 * textured objects at arbitrary locations on the map.
 */
public class ObjectAwareTmxRenderer extends OrthogonalTiledMapRenderer {

  public ObjectAwareTmxRenderer(TiledMap map, float unitScale, SpriteBatch spriteBatch) {
    super(map, unitScale, spriteBatch);
  }

  @Override
  public void renderObject(MapObject object) {
    if (object instanceof RectangleMapObject) {
      RectangleMapObject rectObject = (RectangleMapObject)object;
      MapProperties props = rectObject.getProperties();

      if (props.containsKey("gid")) {
        Integer gid = props.get("gid", Integer.class);
        TiledMapTile tile = getMap().getTileSets().getTile(gid);

        // Get the texture region of this object from the tile set
        TextureRegion textureRegion = tile.getTextureRegion();
        Rectangle rect = rectObject.getRectangle();

        // Scale the object's size by the tile scale
        float width = textureRegion.getRegionWidth() * unitScale;
        float height = textureRegion.getRegionHeight() * unitScale;

        // Scale the object's position by the tile scale
        float posX = rect.x * unitScale;
        float posY = rect.y * unitScale;

        spriteBatch.draw(textureRegion, posX, posY, width, height);
      }
    }
  }
}