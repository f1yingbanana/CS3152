package com.ramenstudio.sandglass.title.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ramenstudio.sandglass.title.view.ui.*;

/**
 * This is the root controller for the title screen, which consists of mainly
 * UI stuff.
 * 
 * @author Jiacong Xu
 */
public class UIController {
  Stage stage = new Stage();
  Skin skin = new Skin(Gdx.files.internal("UI/Skin/uiskin.json"));
  
  TitleView titleView = new TitleView(skin);
  
  public UIController() {
    Gdx.input.setInputProcessor(stage);
    stage.addActor(titleView);
  }
  
  public void update() {
    stage.act();
  }
  
  public void draw() {
    stage.draw();
  }
}
