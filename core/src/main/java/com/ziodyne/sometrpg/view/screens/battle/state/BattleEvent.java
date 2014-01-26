package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.EventEnum;

/**
 * These are events that can occur during a battle.
 */
public enum BattleEvent implements EventEnum {
  FRIENDLY_UNIT_SELECTED,
  ENEMY_UNIT_SELECTED,
  MOVE_ACTION_SELECTED,
  MOVE_LOC_SELECTED,
  UNIT_MOVED,
  ATTACK_SELECTED,
  ATTACK_CONFIRMED,
  UNIT_ATTACKED,
  FRIENDLY_ACTIONS_EXHAUSTED,
  UNIT_DETAILS_CLOSED,
  FRIENDLY_UNIT_STATS_SELECTED
}