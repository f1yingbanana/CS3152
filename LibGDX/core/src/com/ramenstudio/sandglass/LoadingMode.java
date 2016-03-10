package com.ramenstudio.sandglass;

import com.badlogic.gdx.Screen;

/**
 * The overall container for a loading mode. This displays pretty graphics while
 * the game loads.
 * 
 * @author flyingbanana
 */
public class LoadingMode extends AbstractMode implements Screen {

  @Override
  public void show() {
    // TODO Auto-generated method stub

  }

  @Override
  public void render(float delta) {
    // TODO Auto-generated method stub

  }

  @Override
  public void resize(int width, int height) {
    // TODO Auto-generated method stub

  }

  @Override
  public void pause() {
    // TODO Auto-generated method stub

  }

  @Override
  public void resume() {
    // TODO Auto-generated method stub

  }

  @Override
  public void hide() {
    // TODO Auto-generated method stub

  }

  @Override
  public void dispose() {
    // TODO Auto-generated method stub

  }

  @Override
  public String[] getResourcePaths() {
    // We don't need to do anything since the whole point of this method is such
    // that we can use the loading mode. Loading mode should not be loading
    // another loading mode!
    return null;
  }

}
