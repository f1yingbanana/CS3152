package com.ramenstudio.sandglass.title;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.AbstractMode;
import com.ramenstudio.sandglass.util.ScreenListener;

public class LoadingMode extends AbstractMode implements Screen{
	
	/** Background texture for start-up */
	private Texture background = new Texture(Gdx.app.getFiles().internal("Textures/bg1.png"));
	/** gamecanvas*/
	private GameCanvas canvas = new GameCanvas();
	/** bg rendering*/
	private PolygonSpriteBatch bgBatch = new PolygonSpriteBatch();
	/** LoadingMode shares assetManager with the whole game to track progress*/
	public AssetManager manager;
	/** Current progress (0 to 1) of the asset manager */
	private float progress;
	
	private boolean active = true;

	
	/** Default budget for asset loader (do nothing but load 60 fps) */
	private static int DEFAULT_BUDGET = 15;
	/** Standard window size (for scaling) */
	private static int STANDARD_WIDTH  = 1280;
	/** Standard window height (for scaling) */
	private static int STANDARD_HEIGHT = 720;
	

	@Override
	public void show() {
		active = true;
	}

	@Override
	public void render(float delta) {
		if (active){
			draw();
			update(delta);
			if (isReady()){
				screenListener.transitionToMode(this, 0);
			}
		}
	}

	private void update(float dt) {
		manager.update(DEFAULT_BUDGET);
		this.progress = manager.getProgress();

	}

	private void draw() {
		canvas.clear();
		bgBatch.begin();
		bgBatch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		bgBatch.end();	
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
		active = false;
		
	}

	@Override
	public void dispose() {
		background.dispose();
	}

	@Override
	public String[] getResourcePaths() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isReady(){
		return progress>=1.0f;
	}

}
