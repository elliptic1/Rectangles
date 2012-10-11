package com.tbse.rectangles;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Barrier {
	
	private Scene scene;
	private PhysicsWorld world;
	private Engine engine;
	private static final FixtureDef BRICK_DEF = PhysicsFactory
			.createFixtureDef(1f, 0.01f, 10f);

	private ArrayList<Vector2> locs = new ArrayList<Vector2>(10);
	
	public Barrier (Scene scene, PhysicsWorld world, Engine engine) {
		setScene(scene);
		setWorld(world);
		setEngine(engine);
	}
	
	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public PhysicsWorld getWorld() {
		return world;
	}

	public void setWorld(PhysicsWorld world) {
		this.world = world;
	}

	public ArrayList<Vector2> getLocs() {
		return locs;
	}

	public void setLocs(ArrayList<Vector2> locs) {
		this.locs = locs;
	}

	public void addBlock(float x, float y,
			TiledTextureRegion tr) {
		locs.add(new Vector2(x, y));
		final ImmoveableObject block = new ImmoveableObject(x, y, tr, getEngine(),
				getScene(), getWorld());

		block.setBody(PhysicsFactory.createBoxBody(getWorld(), block,
				BodyType.DynamicBody, BRICK_DEF));

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.isSensor = true;
		PolygonShape boxShape = new PolygonShape();
		boxShape.setAsBox(block.getWidth() / 2, block.getHeight() / 2);
		fixtureDef.shape = boxShape;
		block.getBody().createFixture(fixtureDef);

		getScene().attachChild(block);
		getWorld().registerPhysicsConnector(new PhysicsConnector(block, block
				.getBody(), true, true));

	}
	
	
	
	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

}
