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
  
  public final Label messageLabel;
  
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
    
    currentTimeLabel = new Label("00:00", skin, "whiteNormal");
    add(currentTimeLabel).top().padTop(10).row();
    
    flipCountLabel = new Label("-", skin);
    add(flipCountLabel).top().left().padLeft(70).padTop(38).row();

    shipPieceCountLabel = new Label("-", skin);
    add(shipPieceCountLabel).top().right().padRight(20).padTop(112).width(50).row();
    
    messageLabel = new Label("", skin, "levelTitle");
    add(messageLabel).top().expand().row();
  }
  
  public void setFlipCount(int flipsLeft) {
    flipCountLabel.setText("" + flipsLeft);
  }
  
  public void setShipPieceCount(int shipPiecesLeft, int maxShipPieces) {
    shipPieceCountLabel.setText("" + (maxShipPieces - shipPiecesLeft));
  }
  
  public void setCurrentTime(int currentTime) {
	  currentTimeLabel.setText("" + addZero(currentTime/60) + ":" + addZero(currentTime%60));
  }
  
  public void setMessage(String s){
	  messageLabel.setText(s);
  }
  
  public float fadeMessage(){
	  Color mColor = messageLabel.getColor();
	  float red = mColor.r;
	  float green = mColor.g;
	  float blue = mColor.b;
	  
	  messageLabel.setColor(red, green, blue, mColor.a - .05f);
	  
	  return messageLabel.getColor().a;
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

