package com.ramenstudio.sandglass;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.*;

/**
 * The overall container for a loading mode. This displays pretty graphics while
 * the game loads.
 * 
 * @author flyingbanana
 */
public class LoadingMode implements Screen, InputProcessor {

	// Textures necessary to support the loading screen
	private static final String PROGRESS_FILE = "shared/progressbar.png";

	/** Background texture for start-up */
	private Texture background;
	/** Texture atlas to support a progress bar */
	private Texture statusBar;

	// statusbar
	/** Left cap to the status background (grey region) */
	private TextureRegion statusBkgLeft;
	/** Middle portion of the status background (grey region) */
	private TextureRegion statusBkgMiddle;
	/** Right cap to the status background (grey region) */
	private TextureRegion statusBkgRight;
	/** Left cap to the status forground (colored region) */
	private TextureRegion statusFrgLeft;
	/** Middle portion of the status forground (colored region) */
	private TextureRegion statusFrgMiddle;
	/** Right cap to the status forground (colored region) */
	private TextureRegion statusFrgRight;

	/** AssetManager to be loading in the background */
	private AssetManager manager;
	/** Reference to GameCanvas created by the root */
	private GameCanvas canvas;
	/** Listener that will update the player mode when we are done */
	private ScreenListener listener;
	/** Animation controller */
	private AnimationController animecontroller;

	/** The width of the progress bar */
	private int width;
	/** The y-coordinate of the center of the progress bar */
	private int centerY;
	/** The x-coordinate of the center of the progress bar */
	private int centerX;
	/**
	 * The height of the canvas window (necessary since sprite origin != screen
	 * origin)
	 */
	private int heightY;
	/** Scaling factor for when the student changes the resolution. */
	private float scale;

	/** Current progress (0 to 1) of the asset manager */
	private float progress;
	/**
	 * The amount of time to devote to loading assets (as opposed to on screen
	 * hints, etc.)
	 */
	private int budget;
	/** Whether or not this player mode is still active */
	private boolean active;

	public int getBudget() {
		return budget;
	}

	public void setBudget(int millis) {
		budget = millis;
	}

	public LoadingMode(GameCanvas canvas, AssetManager manager) {
		this(canvas, manager, DEFAULT_BUDGET);
	}

	public LoadingMode(GameCanvas canvas, AssetManager manager, int millis) {
		this.manager = manager;
		this.canvas = canvas;
		budget = millis;

		// Compute the dimensions from the canvas
		resize(canvas.getWidth(), canvas.getHeight());

		// Load the next two images immediately.
		statusBar = new Texture(PROGRESS_FILE);

		// No progress so far.
		progress = 0;
		active = false;

		// Break up the status bar texture into regions
		statusBkgLeft = new TextureRegion(statusBar, 0, 0, PROGRESS_CAP, PROGRESS_HEIGHT);
		statusBkgRight = new TextureRegion(statusBar, statusBar.getWidth() - PROGRESS_CAP, 0, PROGRESS_CAP,
				PROGRESS_HEIGHT);
		statusBkgMiddle = new TextureRegion(statusBar, PROGRESS_CAP, 0, PROGRESS_MIDDLE, PROGRESS_HEIGHT);

		int offset = statusBar.getHeight() - PROGRESS_HEIGHT;

		statusFrgLeft = new TextureRegion(statusBar, 0, offset, PROGRESS_CAP, PROGRESS_HEIGHT);
		statusFrgRight = new TextureRegion(statusBar, statusBar.getWidth() - PROGRESS_CAP, offset, PROGRESS_CAP,
				PROGRESS_HEIGHT);
		statusFrgMiddle = new TextureRegion(statusBar, PROGRESS_CAP, offset, PROGRESS_MIDDLE, PROGRESS_HEIGHT);

		Gdx.input.setInputProcessor(this);

		// Let ANY connected controller start the game.
		for (Controller controller : Controllers.getControllers()) {
			controller.addListener(this);
		}
		active = true;
	}

	public setBackground(String dir){
	  background = new Texture(dir);
	  }

	private void loadAssets(String[] assets) {
		for (int i = 0; i < assets.length; i++) {
			manager.load(assets[i], Texture.class);
		}
	}

	private void unloadAssets(String[] assets){
	  for (int i = 0; i<assets.length;i++){
		  manager.unload(assets[i])
		  }
	  }

	private void update(float delta) {
		if (progress < 1.0f) {
			manager.update(budget);
			this.progress = manager.getProgress();
			if (progress >= 1.0f) {
				this.progress = 1.0f;
			}
		}
	}

	private void draw() {
		canvas.begin();
		canvas.draw(background, 0, 0);
		canvas.end();
	}

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		if (active) {
			update(delta);
			draw();

			// We are are ready, notify our listener
			if (isReady() && listener != null) {
				listener.exitScreen(this, 0);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		float sx = ((float) width) / STANDARD_WIDTH;
		float sy = ((float) height) / STANDARD_HEIGHT;
		scale = (sx < sy ? sx : sy);

		this.width = (int) (BAR_WIDTH_RATIO * width);
		centerY = (int) (BAR_HEIGHT_RATIO * height);
		centerX = width / 2;
		heightY = height;
	}

	private void drawProgress(GameCanvas canvas) {
		canvas.draw(statusBkgLeft, Color.WHITE, centerX - width / 2, centerY, scale * PROGRESS_CAP,
				scale * PROGRESS_HEIGHT);
		canvas.draw(statusBkgRight, Color.WHITE, centerX + width / 2 - scale * PROGRESS_CAP, centerY,
				scale * PROGRESS_CAP, scale * PROGRESS_HEIGHT);
		canvas.draw(statusBkgMiddle, Color.WHITE, centerX - width / 2 + scale * PROGRESS_CAP, centerY,
				width - 2 * scale * PROGRESS_CAP, scale * PROGRESS_HEIGHT);

		canvas.draw(statusFrgLeft, Color.WHITE, centerX - width / 2, centerY, scale * PROGRESS_CAP,
				scale * PROGRESS_HEIGHT);
		
		if (progress > 0) {
			float span = progress * (width - 2 * scale * PROGRESS_CAP) / 2.0f;
			canvas.draw(statusFrgRight, Color.WHITE, centerX - width / 2 + scale * PROGRESS_CAP + span, centerY,
					scale * PROGRESS_CAP, scale * PROGRESS_HEIGHT);
			canvas.draw(statusFrgMiddle, Color.WHITE, centerX - width / 2 + scale * PROGRESS_CAP, centerY, span,
					scale * PROGRESS_HEIGHT);
		} else {
			canvas.draw(statusFrgRight, Color.WHITE, centerX - width / 2 + scale * PROGRESS_CAP, centerY,
					scale * PROGRESS_CAP, scale * PROGRESS_HEIGHT);
		}
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
        statusBkgLeft = null;
        statusBkgRight = null;
        statusBkgMiddle = null;

        statusFrgLeft = null;
        statusFrgRight = null;
        statusFrgMiddle = null;

        background.dispose();
        statusBar.dispose();
        background = null;
        statusBar  = null;
    }
	
}
