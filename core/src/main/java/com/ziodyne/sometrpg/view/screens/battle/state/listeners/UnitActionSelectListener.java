package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.InputStealingFlowListener;
import com.ziodyne.sometrpg.view.widgets.ActionMenu;
import com.ziodyne.sometrpg.view.widgets.ActionSelectedHandler;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Logic for entering and exiting the state where the player is selecting an action for a unit.
 */
public class UnitActionSelectListener extends InputStealingFlowListener<BattleContext> {
  private static final Logger LOG = new GdxLogger(UnitActionSelectListener.class);
  private Skin skin;
  private Stage stage;
  private Viewport viewport;
  private float gridSize;

  @Nullable
  private ActionMenu actionMenu;

  public UnitActionSelectListener(Skin skin, Viewport viewport, Stage stage, float gridSize) {
    super(BattleState.SELECTING_UNIT_ACTION);

    this.skin = skin;
    this.viewport = viewport;
    this.stage = stage;
    this.gridSize = gridSize;
  }

  @Override
  public void onLeave(BattleContext context) {
    super.onLeave(context);

    if (actionMenu != null) {
      actionMenu.clear();
    }

    context.mapController.enable();
  }

  @Override
  public void onEnter(final BattleContext context) {
    super.onEnter(context);

    Combatant selectedCombatant = context.selectedCombatant;
    Set<CombatantAction> allowedActions = context.battle.getAvailableActions(selectedCombatant);
    if (allowedActions.size() == 1 && allowedActions.contains(CombatantAction.INFO)) {
      LOG.log("Unit actions exhausted.");
      context.safeTrigger(BattleEvent.ACTIONS_EXHAUSTED);
    } else {

      actionMenu = new ActionMenu(allowedActions, skin);
      actionMenu.setX(context.selectedSquare.x * gridSize);
      actionMenu.setY(context.selectedSquare.y * gridSize);

      actionMenu.addSelectedListener(selectedAction -> {
        switch (selectedAction) {
          case ATTACK:
            context.safeTrigger(BattleEvent.ATTACK_ACTION_SELECTED);
            break;
          case MOVE:
            context.safeTrigger(BattleEvent.MOVE_ACTION_SELECTED);
            break;
          case INFO:
            context.safeTrigger(BattleEvent.INFO_ACTION_SELECTED);
            break;
          default:
            throw new IllegalArgumentException("Combatant action " + selectedAction + " not mapped to event.");
        }
      });

      stage.addActor(actionMenu);
      Gdx.input.setInputProcessor(stage);
      context.mapController.disable();
    }
  }

}
