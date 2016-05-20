package com.ramenstudio.sandglass.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.ramenstudio.sandglass.game.controller.GameController;
import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.AbstractMode;

/**
 * This is the controller for any game level. It is the root for each level. It
 * implements an update-draw loop and handles application-based events.
 * 
 * @author Jiacong Xu
 */
public class GameMode extends AbstractMode implements Screen {

	// The game play controller that handles the basic logic of the game
	private GameController gameplayController;

	// The game canvas.
	private GameCanvas canvas = new GameCanvas();

	// BG renderer
	private PolygonSpriteBatch bgBatch = new PolygonSpriteBatch();


	private Texture backgroundImage;
	/** reference to game level*/
	private int gameLevel;

	// A debug renderer
	Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

	// Toggle debug
	private boolean debug = false;
	// Debug timer
	private int count = 0;

	TiledMapRenderer tiledMapRenderer;

	/**
	 * Initializes an instance of the game with all the controllers, model and
	 * view canvas.
	 */
	public GameMode(int gameLevel) {
		this.gameLevel = gameLevel;
		setBackgroundImage();
		gameplayController = new GameController(gameLevel);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(gameplayController.loader.tiledMap, 1/128f);
	}

	private void setBackgroundImage() {
		if (gameLevel <= 9){
			backgroundImage = new Texture(Gdx.files.internal("Textures/background.beta.V2.png"));
		}
		else if (gameLevel <= 18){
			backgroundImage = new Texture(Gdx.files.internal("Textures/background.beta.V1.png"));
		}
		else {
			backgroundImage = new Texture(Gdx.files.internal("Textures/background.beta.V3.png"));
		}
	}

	@Override
	public void show() {
		// UI needs to be shown.
		gameplayController.uiController.acquireInputProcesser();
	}

	@Override
	public void render(float delta) {
		// Implements an update-draw loop		
		gameplayController.update(delta);
		if (gameplayController.isChangingLevel){
			this.gameLevel= gameplayController.getGameModel().getGameLevel();
			setBackgroundImage();
			gameplayController.isChangingLevel=false;
		}
		
		if (!gameplayController.getGameModel().isInOverworld()){
			bgBatch.setColor(Color.GRAY);
		}
		else{
			bgBatch.setColor(Color.WHITE);
		}
		// Now we render all objects that we can render
		canvas.clear();

		// Draw bg
		bgBatch.begin();
		bgBatch.draw(backgroundImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		bgBatch.end();

		tiledMapRenderer.setView(gameplayController.getViewCamera());

		tiledMapRenderer.render();

		gameplayController.getCameraController().swapCameraDimensions();

		canvas.begin(gameplayController.world2ScreenMatrix());
		gameplayController.draw(canvas);
		canvas.end();

		// DEBUG RENDERS. We can have more render passes later implemented here.

		if (Gdx.input.isKeyJustPressed(Input.Keys.D)) {
			debug ^= true;
		}
		if (debug) {
			debugRenderer.render(gameplayController.world, gameplayController.world2ScreenMatrix());
		}

		// UI RENDER - special case. UI has to be rendered outside loop.
		gameplayController.uiController.draw(canvas);

		// If we want to reset, create a new game controller.
		if (gameplayController.needsReset) {
			gameplayController.dispose();
			gameplayController = new GameController(gameplayController.getGameModel().getGameLevel());
			tiledMapRenderer = new OrthogonalTiledMapRenderer(gameplayController.loader.tiledMap, 1/128f);
		}

		if (gameplayController.needsMenu) {
			screenListener.transitionToMode(this, 0);
		}
	}

	@Override
	public void resize(int width, int height) {
		// We should not need to resize, ever.
	}

	@Override
	public void pause() {
		// Called when the game is paused (loss of focus)
	}

	@Override
	public void resume() {
		// Called when the game regains focus.
	}

	@Override
	public void hide() {
		// When the game disposes this mode.
	}

	@Override
	public void dispose() {
		// When we should release all resources for this screen.
		gameplayController.dispose();
		bgBatch.dispose();
		backgroundImage.dispose();
		canvas.dispose();
	}

	@Override
	public String[] getResourcePaths() {
		return null;
	}

	public int getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(int gameLevel) {
		this.gameLevel = gameLevel;
	}
}
