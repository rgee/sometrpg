package com.ziodyne.sometrpg.logic.loader.models;

public class CharacterSpec {
  private String id;
  private String name;
  private CharacterStats stats;
  private CharacterGrowths growths;

  public String getId() {

    return id;
  }

  public String getName() {

    return name;
  }

  public CharacterStats getStats() {

    return stats;
  }

  public CharacterGrowths getGrowths() {

    return growths;
  }
}
