package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
/**
 * Displays a list of levels, each can be displayed as locked or unlocked.
 * 
 * @author Jiacong Xu
 */
public class LevelSelectView extends Table {
  public final Label titleLabel;
  
  public final TextButton backButton;
  
  public final ScrollPane levelScrollPane;
  
  public final LevelScrollView levelScrollView;

  public final TextButton scrollUpButton;
  
  public final TextButton scrollDownButton;
  
  private int scroll = 0;
  
  private float scrollSpeed = 0.01f;
  
  /**
   * Default constructor. Initializes all the UI components with the given skin.
   */
  public LevelSelectView(Skin skin) {
    super();
 
    setFillParent(true);
    
    Texture bgTexture = new Texture(Gdx.files.internal("Textures/LevelSelect_Page2.png"));
    bgTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
    Sprite bgSprite = new Sprite(bgTexture);
    setBackground(new SpriteDrawable(bgSprite));
    
    Table tbTable = new Table();
    
    titleLabel = new Label("SELECT LEVEL", skin);
    tbTable.add(titleLabel).top().left().pad(20);
    
    tbTable.row();
    
    backButton = new TextButton("BACK", skin);
    
    tbTable.add(backButton).bottom().left().pad(20).expand();
    
    add(tbTable).fill();
    
    levelScrollView = new LevelScrollView(skin);
    
    levelScrollPane = new ScrollPane(levelScrollView, skin);
    
    // Finally, we add two top and bottom views that act as scrollers.
    scrollUpButton = new TextButton("                                  ", skin);
    scrollDownButton = new TextButton("                                  ", skin);
    
    Table rhTable = new Table();
    rhTable.add(scrollUpButton).minHeight(30);
    rhTable.row();
    rhTable.add(levelScrollPane).expand();
    rhTable.row();
    rhTable.add(scrollDownButton).minHeight(30);
    add(rhTable);
    
    // Add actions
    scrollUpButton.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        scroll = -1;
      }
        
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        scroll = 0;
      }
    });

    scrollDownButton.addListener(new ClickListener() {
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        scroll = 1;
      }
        
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        scroll = 0;
      }
    });
  }
  
  @Override
  public void act(float dt) {
    super.act(dt);
    
    if (scroll != 0) {
      levelScrollPane.setScrollPercentY(levelScrollPane.getScrollPercentY() + scroll * scrollSpeed);
    }
  }
}
