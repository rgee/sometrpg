package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;
import org.apache.commons.lang3.Validate;

public class Sprite extends Component {
  private TextureRegion region;
  private Texture texture;
  private Texture.TextureFilter minFilter;
  private Texture.TextureFilter magFiler;
  private SpriteLayer layer;
  private float alpha = 1;
  private final float width;
  private final float height;

  public Sprite(Texture texture, float width, float height, SpriteLayer layer) {
    Validate.notNull(texture);
    Validate.notNull(layer);

    this.layer = layer;
    this.texture = texture;
    this.width = width;
    this.height = height;
  }

  public Sprite(TextureRegion region, float width, float height, SpriteLayer layer) {
    Validate.notNull(region);
    Validate.notNull(layer);

    this.layer = layer;
    this.width = width;
    this.height = height;
    this.region = region;
  }

  public void setRegion(TextureRegion region) {

    this.region = region;
  }

  public SpriteLayer getLayer() {
    return layer;
  }

  public float getAlpha() {
    return alpha;
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }

  public TextureRegion getRegion() {
    return region;
  }

  public Texture getTexture() {
    return texture;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public Texture.TextureFilter getMinFilter() {
    return minFilter;
  }

  public void setMinFilter(Texture.TextureFilter minFilter) {
    this.minFilter = minFilter;
  }

  public Texture.TextureFilter getMagFiler() {
    return magFiler;
  }

  public void setMagFiler(Texture.TextureFilter magFiler) {
    this.magFiler = magFiler;
  }
}
