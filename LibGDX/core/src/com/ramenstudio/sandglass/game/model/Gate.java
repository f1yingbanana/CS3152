package com.ramenstudio.sandglass.game.model;

import com.ramenstudio.sandglass.game.view.GameCanvas;
import com.ramenstudio.sandglass.util.Drawable;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.graphics.Texture;



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
	
	public Gate(){
		super();
		open = false;
		texture = closedTexture;
		size.x = 0.8f;
	    size.y = 1.5f;
	    getBodyDef().type = BodyDef.BodyType.StaticBody;
	    fixtureDefs = new FixtureDef[1];
	    PolygonShape shape = new PolygonShape();
	    fixtureDefs[0] = new FixtureDef();
	    fixtureDefs[0].isSensor = true;
	    shape.setAsBox(size.x, size.y);
	    fixtureDefs[0].shape = shape;
	}
	
	/**sets the gate to open*/
	public void setOpen(){
		open = true;
		texture = openTexture;
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
	
	@Override
	public void draw(GameCanvas canvas){
		if (allPiecesCollected) {
			canvas.draw(texture, getPosition(), getSize());
		}
	}
	
}
