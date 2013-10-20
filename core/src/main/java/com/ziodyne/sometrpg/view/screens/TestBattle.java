package com.ziodyne.sometrpg.view.screens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.components.TiledMapComponent;
import com.ziodyne.sometrpg.view.input.CameraMoveController;
import com.ziodyne.sometrpg.view.systems.MapSelectorUpdateSystem;
import com.ziodyne.sometrpg.view.systems.SpriteRenderSystem;
import com.ziodyne.sometrpg.view.systems.TiledMapRenderSystem;
import com.ziodyne.sometrpg.view.tween.CameraAccessor;

public class TestBattle extends ScreenAdapter {
  private final Game game;
  private final OrthographicCamera camera;
  private final TweenManager tweenManager;
  private final Position mapSelectorPos = new Position();
  private World world;
  private Entity tileSelectorOverlay;
  private SpriteRenderSystem spriteRenderSystem;
  private TiledMapRenderSystem mapRenderSystem;
  private MapSelectorUpdateSystem mapSelectorUpdateSystem;
  private final SpriteBatch spriteBatch;
  private Rectangle mapBoundingRect;

  public TestBattle(Game game) {
    this.game = game;
    this.tweenManager = new TweenManager();
    this.spriteBatch = new SpriteBatch();
    this.camera = new OrthographicCamera();
    camera.setToOrtho(false, 30, 20);

    Tween.registerAccessor(Camera.class, new CameraAccessor());

    TiledMap tiledMap = new TmxMapLoader().load("maps/test/test.tmx");
    TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
    mapBoundingRect = new Rectangle(0, 0, tileLayer.getWidth()-1, tileLayer.getHeight()-1);

    world = new World();

    spriteRenderSystem = new SpriteRenderSystem(camera, spriteBatch);
    mapRenderSystem = new TiledMapRenderSystem(camera);
    mapSelectorUpdateSystem = new MapSelectorUpdateSystem(world, camera, mapBoundingRect);

    world.setSystem(spriteRenderSystem, true);
    world.setSystem(mapRenderSystem, true);
    world.setSystem(mapRenderSystem, true);

    world.setManager(new TagManager());

    world.initialize();

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(new CameraMoveController(camera, world, tweenManager));

    Gdx.input.setInputProcessor(multiplexer);

    TiledMapComponent tiledMapComponent = new TiledMapComponent(tiledMap, 1 / 32f, spriteBatch);

    tileSelectorOverlay = world.createEntity();
    tileSelectorOverlay.addComponent(mapSelectorPos);

    Sprite sprite = new Sprite("grid_overlay.png", 1, 1);
    sprite.setMagFiler(Texture.TextureFilter.Linear);
    sprite.setMinFilter(Texture.TextureFilter.Linear);

    tileSelectorOverlay.addComponent(sprite);
    world.addEntity(tileSelectorOverlay);
    world.getManager(TagManager.class).register("map_selector", tileSelectorOverlay);

    Entity map = world.createEntity();
    map.addComponent(tiledMapComponent);
    world.addEntity(map);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

    camera.update();

    tweenManager.update(delta);
    world.setDelta(delta);
    world.process();

    mapSelectorUpdateSystem.process();
    mapRenderSystem.process();
    spriteRenderSystem.process();
  }
}