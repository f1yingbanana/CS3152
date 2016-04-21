package com.ramenstudio.sandglass.title.controller;

/**
 * The main controller for title screen. This manages the level selection for
 * simplicity.
 */
public class TitleController {
  public UIController uiController = new UIController();
  
  public TitleController() {
    for (int i = 1; i < 10; i++) {
      uiController.levelSelectView.levelScrollView.addLevelView(i, "settings-100");
    }
  }
  
  public void update(float dt) {
    uiController.update(dt);
  }
  
  public void draw() {
    uiController.draw();
  }
}
