package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class LevelPreviewView extends Table {
  public final Image previewSprite;
  
  public final Label levelNameLabel;
  
  public final Button levelSelectButton;
  
  public LevelPreviewView(Skin skin, int level, String imageStyle) {
    // We just make it such that we have left image followed by right level info
    super();
    
    // Add preview sprite.
    previewSprite = new Image(skin, imageStyle);
    add(previewSprite).pad(20);
    
    levelNameLabel = new Label("Level " + level, skin);
    add(levelNameLabel).pad(20);
    
    levelSelectButton = new Button(skin);
    addActor(levelSelectButton);
    
    levelSelectButton.addListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent event, Actor actor) {
        // TODO Auto-generated method stub
        System.out.println(level);
      }
      
    });
    
  }
}
