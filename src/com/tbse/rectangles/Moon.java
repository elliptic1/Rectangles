package com.tbse.rectangles;

import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

class Moon extends AnimatedSprite {
	private PhysicsHandler mPhysicsHandler;
	private Body body;
	private Disc disc;

	public PhysicsHandler getmPhysicsHandler() {
		return mPhysicsHandler;
	}

	public void setmPhysicsHandler(PhysicsHandler h) {
		this.mPhysicsHandler = h;
	}

	private float sinceLastUpdate;
	private int color;

	public Moon(float pX, float pY, TiledTextureRegion pTiledTextureRegion,
			Disc disc) {
		super(pX, pY, pTiledTextureRegion);
		// TODO Auto-generated constructor stub
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
		this.disc = disc;
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {

		super.onManagedUpdate(pSecondsElapsed);

		Vector2 moon2disc = new Vector2(getDisc().getBody().getWorldCenter().x
				- getBody().getWorldCenter().x, getDisc().getBody()
				.getWorldCenter().y - getBody().getWorldCenter().y);
		Vector2 perpFromDisc = new Vector2(-moon2disc.y / moon2disc.len(),
				moon2disc.x / moon2disc.len());
		Vector2 moonPoint = new Vector2(getBody().getWorldCenter().x, getBody()
				.getWorldCenter().y);
		getBody()
				.applyForce(
						moon2disc.mul(
								(float) (Math.max(
										1 * 100 / (dist(getBody()
												.getWorldCenter(), disc
												.getBody().getWorldCenter())),
										10f))).add(perpFromDisc.mul(

						 12.5f/((float) (dist(getBody()
						 .getWorldCenter(), disc
						 .getBody().getWorldCenter())))-2f
						 
						 

//								8f
								))

						, moonPoint);
//		Log.v("r",
//				""
//						+ (float) (dist(getBody().getWorldCenter(), disc
//								.getBody().getWorldCenter())));

	}

	private double dist(Vector2 a, Vector2 b) {
		return Math.pow(
				Math.pow((a.x - b.x), 2.0) + Math.pow((a.y - b.y), 2.0), 0.5);
	}

	public float getX() {
		return getBody().getWorldCenter().x;
	}

	public float getY() {
		return getBody().getWorldCenter().y;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public Disc getDisc() {
		return this.disc;
	}

	public void setDisc(Disc disc) {
		this.disc = disc;
	}

	public float getSinceLastUpdate() {
		return sinceLastUpdate;
	}

	public void setSinceLastUpdate(float sinceLastUpdate) {
		this.sinceLastUpdate = sinceLastUpdate;
	}

}