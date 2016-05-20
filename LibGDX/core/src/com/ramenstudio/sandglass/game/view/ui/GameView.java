package com.ramenstudio.sandglass.game.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

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
  
  private final Table flipCountTable;
  
  private final Image sandglassImage;
  
  private final Table timeTable;

  private final Image shipImage;
  private final Image shipPieceImage;
  
  private final Table shipPieceTable;
  
  /**
   * Default constructor. Uses the given skin to set up the pause screen UI.
   * @param skin is the style-sheet for the widgets.
   */
  public GameView(Skin skin, int level) {
    super();
    
    this.setFillParent(true);
    
    String bgStyle = "default_button";
    
    if (level < 9){
      bgStyle = "default_button";
    } else if (level < 18) {
      bgStyle = "default_button2";
    } else {
      bgStyle = "default_button3";
    }
    
    
    currentTimeLabel = new Label("00:00", skin, "whiteNormal");
    
    timeTable = new Table(skin);
    timeTable.setBackground(bgStyle);
    timeTable.add(currentTimeLabel).padTop(35);
    add(timeTable).top().center().size(200, 120).padTop(-50).colspan(2);
    
    row();
    
    flipCountLabel = new Label("-", skin);
    sandglassImage = new Image(new Texture("UI/Sandglass.png"));
    flipCountTable = new Table(skin);
    flipCountTable.setBackground(bgStyle);
    flipCountTable.add(sandglassImage).size(80).padLeft(60);
    flipCountTable.add(flipCountLabel);

    add(flipCountTable).size(250, 120).left().top().padTop(20).padLeft(-100);
    
    flipCountTable.add(flipCountLabel);
    shipPieceCountLabel = new Label("-", skin);
    shipPieceCountLabel.setAlignment(Align.center);

    shipImage = new Image(new Texture("UI/Ship.png"));
    shipPieceImage = new Image(new Texture("UI/ShipPiece.png"));
    
    shipPieceTable = new Table(skin);
    shipPieceTable.setBackground(bgStyle);
    shipPieceTable.add(shipImage).size(120).padBottom(20).colspan(2).padRight(100);
    shipPieceTable.row();
    shipPieceTable.add(shipPieceImage).size(64).padRight(10);
    shipPieceTable.add(shipPieceCountLabel).width(70).left().padRight(100);
    
    add(shipPieceTable).size(280, 250).right().top().padTop(20).padRight(-100);
    
    row();
    
    messageLabel = new Label("", skin, "levelTitle");
    add(messageLabel).top().expand().colspan(2);
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

