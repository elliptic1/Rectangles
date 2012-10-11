package com.tbse.rectangles;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import android.util.Log;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ImmoveableObject extends AnimatedSprite implements ContactListener {

	private PhysicsHandler mPhysicsHandler;
	private Body body;
//	private Engine engine;
//	private Scene scene;
//	private PhysicsWorld world;
//	private float totalForce;
//	private Fixture forceF;

	public PhysicsHandler getmPhysicsHandler() {
		return mPhysicsHandler;
	}

	public void setmPhysicsHandler(PhysicsHandler h) {
		this.mPhysicsHandler = h;
	}

	public ImmoveableObject(float pX, float pY,
			TiledTextureRegion pTiledTextureRegion, Engine engine,
			Scene scene, PhysicsWorld world) {
		super(pX, pY, pTiledTextureRegion);
		// TODO Auto-generated constructor stub
		this.mPhysicsHandler = new PhysicsHandler(this);
		this.registerUpdateHandler(this.mPhysicsHandler);
//		this.engine = engine;
//		this.scene = scene;
//		this.world = world;
		
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

	}

//	private void remove(ImmoveableObject block) {
//		final PhysicsConnector pc = this.world.getPhysicsConnectorManager().findPhysicsConnectorByShape(block);
//
//		this.world.unregisterPhysicsConnector(pc);
//		this.world.destroyBody(pc.getBody());
//
//		this.scene.detachChild(block);
//		
//		System.gc();
//	}
	
	public boolean boxContainsPt(ArrayList<Line> lines, Float[] pt) {
		
		//       line 1
		//line 4       line 2
		//       line 3

		float leftBound = lines.get(0).getX1();
		float rightBound = lines.get(0).getX2();
		float upBound = lines.get(1).getY2();
		float downBound = lines.get(1).getY1();

		if (leftBound <= pt[0] && pt[0] <= rightBound) {
			if (downBound <= pt[1] && pt[1] <= upBound) {
				return true;
			}
		}

		return false;

	}
	
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public void toJail() {
		this.body.setTransform(0, 1000, 0);
		this.body.setActive(false);
	}

	public void fromJail(float x, float y) {
		this.body.setTransform(x, y, 0);
		this.body.setActive(true);
	}

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		
//		final Fixture fixA = contact.getFixtureA();
//		final Body bodyA = fixA.getBody();
		
//		final Fixture fixB = contact.getFixtureA();
//		final Body bodyB = fixB.getBody();
		
		Log.d("i","contact "+Math.random());
		
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
