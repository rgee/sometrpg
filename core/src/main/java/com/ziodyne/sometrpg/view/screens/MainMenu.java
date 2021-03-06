package com.ziodyne.sometrpg.view.screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.ziodyne.sometrpg.view.Director;
import com.ziodyne.sometrpg.view.screens.battle.TestBattle;
import com.ziodyne.sometrpg.view.tween.ActorAccessor;

public class MainMenu extends ScreenAdapter {
  private final Stage stage;
  private final Skin skin;
  private final Director director;
  private final TweenManager tweenManager;
  private final Label title;
  private final Button startGameButton;
  private final Button quitButton;
  private final TestBattle.Factory battleFactory;

  private boolean menuInitialized = false;
  private boolean initializing = false;

  @Inject
  public MainMenu(Director dir, TweenManager tweenManager, TweenAccessor<Actor> actorTweenAccessor,
                  TestBattle.Factory battleFactory) {
    this.battleFactory = battleFactory;
    this.director = dir;
    this.stage = new Stage(new ScreenViewport());
    this.skin = new Skin(Gdx.files.internal("uiskin.json"));
    this.tweenManager = tweenManager;


    title = new Label("Welcome to Some Tactical RPG", skin);
    title.setX((480 - title.getWidth()) / 2);
    title.setY(310f);

    float buttonHeight = 50;
    float buttonWidth = 200;

    startGameButton = new TextButton("Start Game", skin);
    startGameButton.setX((480 - buttonWidth)/2);
    startGameButton.setY(title.getY() - title.getHeight() - 110);
    startGameButton.setHeight(buttonHeight);
    startGameButton.setWidth(buttonWidth);
    startGameButton.getColor().a = 0;

    startGameButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {

        director.addScreen(battleFactory.create("data/chapters/chapter1.json"));
      }
    });

    quitButton = new TextButton("Quit", skin);
    quitButton.setX((480 - buttonWidth)/2);
    quitButton.setY(startGameButton.getY() - startGameButton.getHeight() - 20);
    quitButton.setHeight(buttonHeight);
    quitButton.setWidth(buttonWidth);
    quitButton.getColor().a = 0;

    quitButton.addListener(new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        Gdx.app.exit();
      }
    });

    stage.addActor(title);
    stage.addActor(startGameButton);
    stage.addActor(quitButton);

    Tween.setCombinedAttributesLimit(4);
    Tween.registerAccessor(Actor.class, actorTweenAccessor);
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    stage.act(Gdx.graphics.getDeltaTime());

    tweenManager.update(delta);
    stage.draw();

    if (!menuInitialized && !initializing) {
      initializing = true;
      Timeline.createSequence()
        .push(Tween.to(title, ActorAccessor.POSITION_Y, 0.5f).ease(TweenEquations.easeOutCubic).target(250f))
        .beginParallel()
          .push(Tween.to(startGameButton, ActorAccessor.OPACITY, 0.3f).target(1.0f))
          .push(Tween.to(quitButton, ActorAccessor.OPACITY, 0.3f).target(1.0f))
        .end()
        .setCallback((type, source) -> {
          if (type == TweenCallback.COMPLETE) {
            menuInitialized = true;
          }
        })
        .start(tweenManager);
    }

  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height);
  }

  @Override
  public void dispose() {
    stage.dispose();
  }

}
