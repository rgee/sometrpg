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
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.BattleMap;
import com.ziodyne.sometrpg.logic.models.TerrainType;
import com.ziodyne.sometrpg.logic.models.Tile;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Army;
import com.ziodyne.sometrpg.logic.models.battle.ArmyType;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.conditions.Rout;
import com.ziodyne.sometrpg.view.assets.BattleLoader;
import com.ziodyne.sometrpg.view.assets.MapLoader;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.components.TiledMapComponent;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.input.CameraMoveController;
import com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils;
import com.ziodyne.sometrpg.view.systems.MapSelectorUpdateSystem;
import com.ziodyne.sometrpg.view.systems.SpriteRenderSystem;
import com.ziodyne.sometrpg.view.systems.TiledMapRenderSystem;
import com.ziodyne.sometrpg.view.tween.CameraAccessor;

public class TestBattle extends BattleScreen {
  private final TweenManager tweenManager;
  private SpriteRenderSystem spriteRenderSystem;
  private TiledMapRenderSystem mapRenderSystem;
  private MapSelectorUpdateSystem mapSelectorUpdateSystem;
  private Rectangle mapBoundingRect;
  private final Battle battle;
  private Tile selectedTile;

  public TestBattle() {
    super(new OrthographicCamera(), "maps/test/test.tmx");
    this.tweenManager = new TweenManager();
    camera.setToOrtho(false, 30, 20);

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(new CameraMoveController(camera, tweenManager));
    Gdx.input.setInputProcessor(multiplexer);

    Tween.registerAccessor(Camera.class, new CameraAccessor());

    TiledMap tiledMap = new TmxMapLoader().load("maps/test/test.tmx");
    TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
    mapBoundingRect = new Rectangle(0, 0, tileLayer.getWidth()-1, tileLayer.getHeight()-1);

    spriteRenderSystem = new SpriteRenderSystem(camera, spriteBatch);
    mapRenderSystem = new TiledMapRenderSystem(camera);
    mapSelectorUpdateSystem = new MapSelectorUpdateSystem(world, camera, mapBoundingRect);

    world.setSystem(spriteRenderSystem, true);
    world.setSystem(mapRenderSystem, true);
    world.setSystem(mapRenderSystem, true);

    world.setManager(new TagManager());

    world.initialize();


    Entity tileSelectorOverlay = entityFactory.createMapSelector();
    world.addEntity(tileSelectorOverlay);
    world.getManager(TagManager.class).register("map_selector", tileSelectorOverlay);

    world.addEntity(entityFactory.createTiledMap(tiledMap, spriteBatch));

    assetManager.setLoader(BattleMap.class, new MapLoader(new InternalFileHandleResolver()));
    assetManager.setLoader(Battle.class, new BattleLoader(new InternalFileHandleResolver()));

    //assetManager.load("battles/test.json", Battle.class);

    battle = initBattle(tileLayer);
    initUnitEntities();
  }



  private void initUnitEntities() {
    BattleMap map = battle.getMap();
    for (int i = 0; i < map.getWidth(); i++) {
      for (int j = 0; j < map.getHeight(); j++) {
        Tile tile = map.getTile(i, j);
        Unit unit = tile.getOccupyingUnit();
        if (unit != null) {
          Entity unitEntity = entityFactory.createUnit(unit, "grid_overlay.png", i, j);
          world.addEntity(unitEntity);
        }
      }
    }
  }

  private Battle initBattle(TiledMapTileLayer map) {
    Battle battle = new Battle();

    Unit player = new Unit(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test");
    Unit enemy = new Unit(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test");

    Army playerArmy = new Army(Sets.newHashSet(player), "Greil Mercenaries", ArmyType.PLAYER);
    Army enemyArmy = new Army(Sets.newHashSet(enemy), "Dawn Brigade", ArmyType.ENEMY);
    battle.setArmies(ImmutableList.of(playerArmy, enemyArmy));

    battle.setCondition(new Rout());

    Tile[][] tiles = new Tile[map.getWidth()][map.getHeight()];
    for (int i = 0; i < map.getWidth(); i++) {
      for (int j = 0; j < map.getHeight(); j++) {
        // TODO: Read the tile from the tile layer and get the attribute
        tiles[i][j] = new Tile(TerrainType.GRASS);
      }
    }

    BattleMap battleMap = new BattleMap(tiles.length, tiles);
    battleMap.addUnit(player, 0, 0);
    battleMap.addUnit(enemy, tiles.length-1, tiles.length-1);
    battle.setMap(battleMap);

    return battle;
  }

  protected void update(float delta) {
    tweenManager.update(delta);
    mapSelectorUpdateSystem.process();
    mapRenderSystem.process();
    spriteRenderSystem.process();
  }
}
