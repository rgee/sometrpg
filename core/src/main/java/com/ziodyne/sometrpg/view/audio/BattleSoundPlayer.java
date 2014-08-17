package com.ziodyne.sometrpg.view.audio;

import com.google.common.eventbus.EventBus;
import com.ziodyne.sometrpg.events.EventListener;
import com.ziodyne.sometrpg.events.UnitHit;
import com.ziodyne.sometrpg.view.assets.AssetRepository;

/**
 * Knows how to respond to game events and play the right sounds
 */
public class BattleSoundPlayer {

  private final SoundPlayer soundPlayer;

  public BattleSoundPlayer(EventBus eventBus, AssetRepository repo) {

    this.soundPlayer = new SoundPlayer(repo);
    attachListeners(eventBus);
  }

  public void play(SoundEffect effect) {

    soundPlayer.play(effect.getPath());
  }

  private void attachListeners(EventBus bus) {
    // Bigole translation table between game events and sounds!
    bus.register((EventListener<UnitHit>) input -> play(SoundEffect.HIT));
  }
}