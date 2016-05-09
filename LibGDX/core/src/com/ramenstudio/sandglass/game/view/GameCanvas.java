package com.ramenstudio.sandglass.game.view;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.*;

/**
 * The root view object used for drawing. Canvas needs to track the camera
 * reference to be able to calculate the transforms. This class provides various
 * graphic APIs to draw objects on screen.
 * 
 * @author Jiacong Xu
 */
public class GameCanvas {
	// The background color
	private Color bgColor = Color.DARK_GRAY;

	// Drawing context to handle textures AND POLYGONS as sprites
	private PolygonSpriteBatch spriteBatch = new PolygonSpriteBatch();

	/**
	 * Instantiates a game canvas.
	 */
	public GameCanvas() {
	}

	/**
	 * @return the current background color for the canvas.
	 */
	public Color getBackgroundColor() {
		return bgColor;
	}

	/**
	 * Sets the background color for the canvas. Without it we get random noise 
	 * from the buffer. The alpha channel is ignored.
	 * 
	 * @param color is the background color to draw. Default is coral.
	 */
	public void setBackgroundColor(Color color) {
		bgColor = color;
	}

	/**
	 * Call first thing in render to avoid left-over buffer garbage.
	 */
	public void clear() {
		Gdx.gl.glClearColor(bgColor.r, bgColor.b, bgColor.g, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	/**
	 * Begins a new drawing pass with the given matrix transformation.
	 * @param worldToScreenMatrix is the matrix that transforms world coordinates
	 *        into screen space. This should be given by the main camera.
	 */
	public void begin(Matrix4 worldToScreenMatrix) {
		spriteBatch.setProjectionMatrix(worldToScreenMatrix);
		spriteBatch.begin();
	}

	/**
	 * Flushes all the drawing to the GPU.
	 */
	public void end() {
		spriteBatch.end();
	}

	/**
	 * Draws the given texture at origin with given size.
	 * @param image is the texture we will render.
	 * @param origin is the bottom left corner of the image.
	 * @param size is how big the image should be in world units.
	 */
	public void draw(Texture image, Vector2 origin, Vector2 size) {
		spriteBatch.draw(image, origin.x, origin.y, size.x, size.y);
	}

	public void draw(TextureRegion image, Vector2 origin, Vector2 size, 
			Vector2 worldOrigin, float angle) {
		spriteBatch.draw(image, origin.x, origin.y, worldOrigin.x, worldOrigin.y, 
				size.x, size.y, 1, 1, angle);
	}

	public void draw(TextureRegion image, Color color, Vector2 origin, Vector2 size, 
			Vector2 worldOrigin, float angle) {
		spriteBatch.setColor(color);
		spriteBatch.draw(image, origin.x, origin.y, worldOrigin.x, worldOrigin.y, 
				size.x, size.y, 1, 1, angle);
	}
}
