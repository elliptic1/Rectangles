package com.tbse.rectangles;

import java.util.ArrayList;

import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

class Disc extends AnimatedSprite {
	private final PhysicsHandler mPhysicsHandler;
	// private float sinceLastUpdate = 0;
	private String color;
	private Body body;
	private boolean shouldBeMoved;
	private ArrayList<Float> xhist = new ArrayList<Float>(5);
	private ArrayList<Float> yhist = new ArrayList<Float>(5);
	private ContactsManager contactsManager = new ContactsManager();
	private Scene scene;
	// private Main main;
	private Boolean toBeSetActiveFalse;
	private ShootLine line;

	public Disc(float pX, float pY, TiledTextureRegion pTiledTextureRegion,
			Scene scene, MainGameActivity main) {
		super(pX, pY, pTiledTextureRegion);
		// TODO Auto-generated constructor stub
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.setScene(scene);
		// this.main = main;
	}

	public void addXYhist(float x, float y) {
		int maxlen = 3;
		if (getXhist().size() > maxlen) {
			getXhist().remove(0);
			getXhist().add(maxlen, x / 32f);
		} else {
			getXhist().add(x / 32f);
		}
		if (getYhist().size() > maxlen) {
			getYhist().remove(0);
			getYhist().add(maxlen, y / 32f);
		} else {
			getYhist().add(y / 32f);
		}

	}

	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
			final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
		try {
			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
				setLine(new ShootLine(scene, -1f, -1f, -1f, -1f));
			}

			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_MOVE) {

				addXYhist(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

				getLine().update(getX() * 32f, getY() * 32f,
						pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

			}

			if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {

				getBody()
						.setLinearVelocity(
								new Vector2(
										0.3f * (pSceneTouchEvent.getX() - getX() * 32f),
										0.3f * (pSceneTouchEvent.getY() - getY() * 32f)));
				getLine().detachSelf();

			}
		} catch (Exception e) {
//			Log.v("r","Excp: "+e.getMessage());
		}

		return true;
	}

	public float getX() {
		return getBody().getWorldCenter().x;
	}

	public float getY() {
		return getBody().getWorldCenter().y;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Body getBody() {
		return body;
	}

	public Disc getDisc() {
		return this;
	}

	public void setBody(Body body, Disc disc) {
		this.body = body;
		this.body.setUserData(disc);
	}

	public Vector2 getCenterPoint() {
		return new Vector2(getX(), getY());
	}

	public boolean getShouldBeMoved() {
		return shouldBeMoved;
	}

	public void setShouldBeMoved(boolean shouldBeMoved) {
		this.shouldBeMoved = shouldBeMoved;
	}

	public ArrayList<Float> getXhist() {
		return xhist;
	}

	public ArrayList<Float> getYhist() {
		return yhist;
	}

	public float getXtrend() {
		if (getXhist().size() >= 1) {
			return (getXhist().get(getXhist().size() - 1) - getXhist().get(0)) * 32;
		} else {
			return 1;
		}
	}

	public float getYtrend() {
		if (getYhist().size() >= 1) {
			return (getYhist().get(getYhist().size() - 1) - getYhist().get(0)) * 20;
		} else {
			return 1;
		}
	}

	public ContactsManager getContactsManager() {
		return this.contactsManager;
	}

	public void addContact(Contact c) {
		this.contactsManager.addContact(c);
	}

	public void removeContact(Contact c) {
		this.contactsManager.removeContact(c);
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Boolean getToBeSetActiveFalse() {
		return toBeSetActiveFalse;
	}

	public void setToBeSetActiveFalse(Boolean bool) {
		this.toBeSetActiveFalse = bool;
	}

	public ShootLine getLine() {
		return line;
	}

	public void setLine(ShootLine line) {
		this.line = line;
	}

}
