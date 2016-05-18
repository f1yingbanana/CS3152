package com.ramenstudio.sandglass.title.view.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.ramenstudio.sandglass.util.view.ui.KeyboardControlButton;
/**
 * Displays a list of levels, each can be displayed as locked or unlocked.
 * 
 * @author Jiacong Xu
 */
public class LevelSelectView extends Table {
  public final Label titleLabel;
  
  public final KeyboardControlButton backButton;
  
  public final ScrollPane levelScrollPane;
  
  public final LevelScrollView levelScrollView;

  public int scrollTo = 1;
  
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
    
    backButton = new KeyboardControlButton("BACK", skin, "white");
    tbTable.add(backButton).bottom().left().pad(20).expand().prefSize(120, 60);
    
    add(tbTable).fill();
    
    levelScrollView = new LevelScrollView(skin);
    
    levelScrollPane = new ScrollPane(levelScrollView, skin);
    add(levelScrollPane);
  }
  
  @Override
  public void act(float dt) {
    super.act(dt);
    /*
    if (isScrolling) {
      levelScrollPane.setScrollPercentY(levelScrollPane.getScrollPercentY() + scroll * scrollSpeed);
    }*/
    
    levelScrollPane.setScrollY(scrollTo * (200 + 10) - Gdx.graphics.getHeight() / 2f);
  }
}
