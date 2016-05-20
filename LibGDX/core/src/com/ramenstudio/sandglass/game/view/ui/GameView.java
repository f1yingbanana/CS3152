package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Defines the UI used for playing state of the game.
 * 
 * @author Jiacong Xu
 */
public class GameView extends Table {
  public final Label flipCountLabel;
  
  public final Label shipPieceCountLabel;
  
  public final Label currentTimeLabel;
  
  public final Label bestTimeLabel;
  
  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public GameView(Skin skin, int level) {
    super();
    
    this.setFillParent(true);
    
    if (level < 9){
    	this.setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("UI/UI_LEVEL_Type1.png")))));
    } else if (level < 18) {
    	this.setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("UI/UI_LEVEL_Type2.png")))));
    } else {
    	this.setBackground(new SpriteDrawable(new Sprite(new Texture(Gdx.files.internal("UI/UI_LEVEL_Type3.png")))));
    }
    
    flipCountLabel = new Label("10", skin);
    add(flipCountLabel).top().left().padLeft(65).padTop(83).expand().row();

    shipPieceCountLabel = new Label("10", skin);
    add(shipPieceCountLabel).top().right().padRight(15).padBottom(30).width(50).row();
    
    currentTimeLabel = new Label("YOUR TIME - ", skin, "font_gotham-_light_24pt", Color.WHITE);
    add(currentTimeLabel).bottom().left().padTop(275).padLeft(15).row();
    
    bestTimeLabel = new Label("BEST TIME - ", skin, "font_gotham-_light_24pt", Color.WHITE);
    add(bestTimeLabel).bottom().left().padBottom(20).padLeft(15).padTop(10);
  }
  
  public void setFlipCount(int flipsLeft) {
    flipCountLabel.setText("" + flipsLeft);
  }
  
  public void setShipPieceCount(int shipPiecesLeft, int maxShipPieces) {
    shipPieceCountLabel.setText("" + (maxShipPieces - shipPiecesLeft));
  }
  
  public void setCurrentTime(int currentTime) {
	  currentTimeLabel.setText("YOUR TIME - " + addZero(currentTime/60) + ":" + addZero(currentTime%60));
  }
  
  public void setBestTime(int bestTime) {
	  bestTimeLabel.setText("BEST TIME - " + addZero(bestTime/60) + ":" + addZero(bestTime%60));
  }
  
  public String addZero(int time) {
	  if (time < 10){
		  return "0" + time;
	  }
	  else {
		  return ""+time;
	  }
  }
}

