package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The main menu view.
 * 
 * @author Jiacong Xu
 */
public class TitleView extends Table {
  public Label gameTitleLabel;
  public TextButton gameStartButton;
  
  
  public TitleView(Skin skin) {
    super();
    
    setFillParent(true);
    
    // Create center text
    gameTitleLabel = new Label("Sandglass", skin);
    gameTitleLabel.setFontScale(2);
    add(gameTitleLabel).padTop(20);
    
    // Create a button to play demo level
    row();
    gameStartButton = new TextButton("Start", skin);
    add(gameStartButton).padTop(120);
    
    gameStartButton.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        gameStartButton.setText("-    START    -");
      }
      
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        gameStartButton.setText("START");
      }
    });
  }
}
