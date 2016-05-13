package com.ramenstudio.sandglass.game.model;

import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;



/** Class for the Game's Gate 
 * @author Nathaniel Hunter*/
public class Gate extends GameObject implements Drawable {
	
	//is the gate open?
	private boolean open;
	//the texture of the gate while closed
	private Texture closedTexture;
	//the texture of the gate while open
	private Texture openTexture;
	//the current texture of the gate
	private Texture texture;
	
	private boolean allPiecesCollected;
	
	public Gate(Vector2 pos){
		super();
		open = false;
		size.x = 1.5f;
	    size.y = 1.5f;
	    getBodyDef().type = BodyDef.BodyType.StaticBody;
	    getBodyDef().position.set(pos);
	    fixtureDefs = new FixtureDef[1];
	    PolygonShape shape = new PolygonShape();
	    fixtureDefs[0] = new FixtureDef();
	    fixtureDefs[0].isSensor = true;
	    shape.setAsBox(size.x*0.5f - 0.05f, size.y*0.5f - 0.05f);
	    fixtureDefs[0].shape = shape;
	}
	
	/**sets the gate to open*/
	public void setOpen(){
		open = true;
		//texture = openTexture;
	}
	
	public void setTextureLevel(int gameLevel){
		if (gameLevel < 10){
	    	texture = new Texture(Gdx.files.internal("gate.v1.png"));
	    }
	    else if (gameLevel < 18){
	    	texture = new Texture(Gdx.files.internal("gate.v3.png"));
	    }
	    else {
	    	texture = new Texture(Gdx.files.internal("gate.v2.png"));
	    }
	}
	
	/**@return true if the gate is open*/
	public boolean isOpen(){
		return open;
	}
	
	/**sets the gate's open texture*/
	public void setOpenTexture(Texture t){
		openTexture = t;
	}
	
	/**sets the gate's closed texture*/
	public void setClosedTexture(Texture t){
		closedTexture = t;
	}
	
	public void setAllPiecesCollected(boolean argCollected) {
		allPiecesCollected = argCollected;
	}
	
	public boolean getAllPiecesCollected() {
		return allPiecesCollected;
	}
	
	@Override
	public void draw(GameCanvas canvas){
		if (allPiecesCollected) {
			canvas.draw(texture, getPosition().sub(0.5f*size.x,0.5f*size.y), getSize());
		}
	}
	
	@Override
	public void dispose() {
		texture.dispose();
	}
}
