package com.tbse.rectangles;

import java.util.ArrayList;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.ColorBackground;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.shape.IShape;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.extension.physics.box2d.PhysicsConnector;
import org.anddev.andengine.extension.physics.box2d.PhysicsFactory;
import org.anddev.andengine.extension.physics.box2d.PhysicsWorld;
import org.anddev.andengine.extension.physics.box2d.util.Vector2Pool;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.sensor.accelerometer.AccelerometerData;
import org.anddev.andengine.sensor.accelerometer.IAccelerometerListener;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.KeyEvent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MainGameActivity extends BaseGameActivity implements IAccelerometerListener,
		IOnMenuItemClickListener {
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}


	protected PhysicsWorld world;
	private Scene scene;
	private Engine engine;

	// Menu
	protected MenuScene mMenuScene;
	protected TextureRegion mMenuHomeTextureRegion;
	private BitmapTextureAtlas mMenuTexture;
	//

	static int CAMERA_WIDTH;
	static int CAMERA_HEIGHT;
	protected static final int MENU_HOME = 1;

	// private static final FixtureDef BALL_DEF =
	// PhysicsFactory.createFixtureDef(
	// 1, 0.1f, 0.5f);
	private static final FixtureDef DISC_DEF = PhysicsFactory.createFixtureDef(
			15, // Density
			0.4f, // Elasticity
			0.7f); // Friction
	private ArrayList<Disc> discs = new ArrayList<Disc>(10);
	private ArrayList<Moon> moons = new ArrayList<Moon>(10);

	private ZoomCamera mCamera;

	private BitmapTextureAtlas redMoonTexture;
	private BitmapTextureAtlas blueMoonTexture;
	private BitmapTextureAtlas greenMoonTexture;
	private BitmapTextureAtlas yellowMoonTexture;
	private BitmapTextureAtlas discBlueTexture;
	private BitmapTextureAtlas discGreenTexture;
	private BitmapTextureAtlas discRedTexture;
	private BitmapTextureAtlas discYellowTexture;
	private BitmapTextureAtlas mTexture1;
	private BitmapTextureAtlas mTexture2;
	private BitmapTextureAtlas mTexture3;
	private BitmapTextureAtlas mTexture4;
	private BitmapTextureAtlas brickTexture1;

	private TiledTextureRegion mBlueDiscTextureRegion;
	private TiledTextureRegion mGreenDiscTextureRegion;
	private TiledTextureRegion mYellowDiscTextureRegion;
	private TiledTextureRegion mRedDiscTextureRegion;
	private TiledTextureRegion mYellowMoonTextureRegion;
	private TiledTextureRegion mBlueMoonTextureRegion;
	private TiledTextureRegion mGreenMoonTextureRegion;
	private TiledTextureRegion mRedMoonTextureRegion;
	private TiledTextureRegion brick1ttr;

	private TextureRegion mFaceTextureRegionBlue;
	private TextureRegion mFaceTextureRegionRed;
	private TextureRegion mFaceTextureRegionGreen;
	private TextureRegion mFaceTextureRegionYellow;

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public org.anddev.andengine.engine.Engine onLoadEngine() {
		// TODO Auto-generated method stub
		final Display display = getWindowManager().getDefaultDisplay();
		Point outSize = new Point();
		display.getSize(outSize);
		CAMERA_WIDTH = outSize.x;
		CAMERA_HEIGHT = outSize.y;
		this.mCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		System.loadLibrary("gdx");

		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.PORTRAIT, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);

		options.getTouchOptions().enableRunOnUpdateThread();

		this.engine = new org.anddev.andengine.engine.Engine(options);
		return engine;

	}

	@Override
	public void onLoadResources() {
		// Menu
		this.mMenuTexture = new BitmapTextureAtlas(512, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mMenuHomeTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mMenuTexture, this, "gfx/homebutton.png",
						0, 100);
		this.mEngine.getTextureManager().loadTexture(this.mMenuTexture);

		this.brickTexture1 = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.setBrick1ttr(BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.brickTexture1, this,
						"gfx/AlternatingBrick.png", 0, 0, 1, 1));
		this.yellowMoonTexture = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.greenMoonTexture = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.redMoonTexture = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.blueMoonTexture = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.discRedTexture = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.discBlueTexture = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.discYellowTexture = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.discGreenTexture = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.mTexture1 = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTexture2 = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTexture3 = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mTexture4 = new BitmapTextureAtlas(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.setmYellowMoonTextureRegion(BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.yellowMoonTexture, this,
						"gfx/ballyellow.png", 0, 0, 1, 1));
		this.setmGreenMoonTextureRegion(BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.greenMoonTexture, this,
						"gfx/ballgreen.png", 0, 0, 1, 1));
		this.setmRedMoonTextureRegion(BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.redMoonTexture, this,
						"gfx/ballred.png", 0, 0, 1, 1));
		this.setmBlueMoonTextureRegion(BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.blueMoonTexture, this,
						"gfx/ballblue.png", 0, 0, 1, 1));
		this.setmBlueDiscTextureRegion(BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.discBlueTexture, this,
						"gfx/discblue.png", 0, 0, 1, 1));
		this.setmGreenDiscTextureRegion(BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.discGreenTexture, this,
						"gfx/discgreen.png", 0, 0, 1, 1));
		this.setmYellowDiscTextureRegion(BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.discYellowTexture, this,
						"gfx/discyellow.png", 0, 0, 1, 1));
		this.setmRedDiscTextureRegion(BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.discRedTexture, this,
						"gfx/discred.png", 0, 0, 1, 1));
		this.setmFaceTextureRegionBlue(BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mTexture1, this, "gfx/ballblue.png", 0, 0));
		this.setmFaceTextureRegionYellow(BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mTexture2, this, "gfx/ballyellow.png", 0,
						0));
		this.setmFaceTextureRegionRed(BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mTexture3, this, "gfx/ballred.png", 0, 0));
		this.setmFaceTextureRegionGreen(BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mTexture4, this, "gfx/ballgreen.png", 0,
						0));
		this.mEngine.getTextureManager().loadTexture(this.discBlueTexture);
		this.mEngine.getTextureManager().loadTexture(this.discYellowTexture);
		this.mEngine.getTextureManager().loadTexture(this.discRedTexture);
		this.mEngine.getTextureManager().loadTexture(this.discGreenTexture);
		this.mEngine.getTextureManager().loadTexture(this.yellowMoonTexture);
		this.mEngine.getTextureManager().loadTexture(this.redMoonTexture);
		this.mEngine.getTextureManager().loadTexture(this.blueMoonTexture);
		this.mEngine.getTextureManager().loadTexture(this.greenMoonTexture);
		this.mEngine.getTextureManager().loadTexture(this.mTexture1);
		this.mEngine.getTextureManager().loadTexture(this.mTexture2);
		this.mEngine.getTextureManager().loadTexture(this.mTexture3);
		this.mEngine.getTextureManager().loadTexture(this.mTexture4);
		this.mEngine.getTextureManager().loadTexture(this.mMenuTexture);
		this.mEngine.getTextureManager().loadTexture(this.brickTexture1);

		this.enableAccelerometerSensor(this);
	}

	public Sprite makeBall(float x, float y, TextureRegion tr) {
		return new Sprite(x, y, tr);
	}

	public Disc addDisc(Scene scene, PhysicsWorld world, float x, float y,
			String color) {

		Disc disc;

		if (color.equals("blue")) {

			disc = new Disc(x, y, getmBlueDiscTextureRegion(), scene, this);
		}

		else if (color.equals("red")) {

			disc = new Disc(x, y, getmRedDiscTextureRegion(), scene, this);
		}

		else if (color.equals("yellow")) {

			disc = new Disc(x, y, getmYellowDiscTextureRegion(), scene, this);
		}

		else if (color.equals("green")) {

			disc = new Disc(x, y, getmGreenDiscTextureRegion(), scene, this);

		} else {

			disc = new Disc(x, y, getmRandomDiscTextureRegion(), scene, this);

		}

		scene.attachChild(disc);
		scene.registerTouchArea(disc);

		disc.setBody(PhysicsFactory.createCircleBody(this.world, disc,
				BodyType.DynamicBody, DISC_DEF), disc);

		this.world.registerPhysicsConnector(new PhysicsConnector(disc, disc
				.getBody(), true, true));

		discs.add(disc);

		return disc;

	}

	public void addMoon(Scene scene, PhysicsWorld world, float x, float y,
			Disc disc, String color) {

		Moon moon;
		if (color.equals("red")) {
			moon = new Moon(x, y, getmRedMoonTextureRegion(), disc);
		} else if (color.equals("blue")) {
			moon = new Moon(x, y, getmBlueMoonTextureRegion(), disc);
		} else if (color.equals("green")) {
			moon = new Moon(x, y, getmGreenMoonTextureRegion(), disc);
		} else if (color.equals("yellow")) {
			moon = new Moon(x, y, getmYellowMoonTextureRegion(), disc);
		} else {
			moon = new Moon(x, y, getmRandomMoonTextureRegion(), disc);
		}
		

		scene.attachChild(moon);
		scene.registerTouchArea(moon);

		moon.setBody(PhysicsFactory.createCircleBody(this.world, moon,
				BodyType.DynamicBody, DISC_DEF));

		this.world.registerPhysicsConnector(new PhysicsConnector(moon, moon
				.getBody(), true, true));

		moons.add(moon);

	}

	@Override
	public Scene onLoadScene() {
		// TODO Auto-generated method stub
		this.mEngine.registerUpdateHandler(new FPSLogger());

//		this.createMenuScene();

		final Scene scene = new Scene();
		setScene(scene);
		// getScene().setBackground(new ColorBackground(135/255f, 206/255f,
		// 235/255f)); // skyblue
		// getScene().setBackground(new ColorBackground(191/255f, 239/255f,
		// 255/255f)); // lightblue
		getScene().setBackground(
				new ColorBackground(176 / 255f, 226 / 255f, 255 / 255f)); // lightskyblue
		// getScene().setBackground(new ColorBackground(176/255f, 224/255f,
		// 230/255f)); // powderblue

		// getScene().setBackground(new ColorBackground(0, 0, 0.35f));

		// make world
		Vector2 gravity = new Vector2(1.00f * SensorManager.GRAVITY_EARTH, 0);
		world = new PhysicsWorld(gravity, false);

		int numDiscsLeft = 0;
		int numDiscsRight = 0;
		Intent intent = getIntent();
		int numDiscsCenter = intent.getIntExtra("com.tbse.rectangles.numdiscs", 1);
		int numBricks = 0;
		int numMoons = intent.getIntExtra("com.tbse.rectangles.nummoons", 1);
		int numRectangles = intent.getIntExtra("com.tbse.rectangles.numrectangles", 1);

		// disc, moon and balls center
		for (int i = 0; i < numDiscsCenter; i++) {
			Disc disc = addDisc(scene, this.world, (float) (CAMERA_WIDTH * .9
					* Math.random() + 20),
					(float) (CAMERA_HEIGHT * .9 * Math.random() + 20), "random");

			for (int j = 0; j < numMoons; j++) {
				addMoon(scene, this.world, CAMERA_WIDTH/2, CAMERA_HEIGHT/2, disc, "random");
			}

		}

		// disc, moon and balls left
		for (int i = 0; i < numDiscsLeft; i++) {
			Disc disc = addDisc(scene, this.world, (float) (CAMERA_WIDTH * .4
					* Math.random() + 20),
					(float) (CAMERA_HEIGHT * .9 * Math.random() + 20), "blue");

			for (int j = 0; j < numMoons; j++) {
				addMoon(scene, this.world, CAMERA_WIDTH/2, CAMERA_HEIGHT/2, disc, "random");
			}

		}

		// discs right
		for (int i = 0; i < numDiscsRight; i++) {
			Disc disc = addDisc(scene, this.world, (float) (CAMERA_WIDTH * .4
					* Math.random() + CAMERA_WIDTH/2),
					(float) (CAMERA_HEIGHT * .9 * Math.random() + 20), "red");

			for (int j = 0; j < numMoons; j++) {
				addMoon(scene, this.world, 400, 400, disc, "random");
			}

		}
		//

		ArrayList<Vector2> points = new ArrayList<Vector2>(10);
		points.add(new Vector2(30f, 30f));
		Barrier barrier = new Barrier(getScene(), getWorld(), getEngine());

		for (int i = 0; i < numBricks; i++) {
			barrier.addBlock(50f, 50f, this.brick1ttr);
		}

		getScene().setTouchAreaBindingEnabled(true);

		// rectangles
		for (int i = 0; i < numRectangles; i++) {

			addRectangle((float) (CAMERA_WIDTH * Math.random()), // horiz loc
					(float) (CAMERA_HEIGHT * Math.random()), // vert loc
					(float) (1 + 150 * Math.random()), // Width
					(float) (1 + 30 * Math.random()), // Height
					(float) (0.8f * Math.random()), // Density
					(float) (Math.random()), // Elasticity
					(float) (Math.random()), // Friction
					(float) Math.random(), // red
					(float) Math.random(), // green
					(float) Math.random() // blue

			);

		}

		// walls
		Boolean showWalls = true;
		if (showWalls) {
			final IShape ground = new Rectangle(0, CAMERA_HEIGHT - 2,
					CAMERA_WIDTH, 2);
			final IShape roof = new Rectangle(0, 0, CAMERA_WIDTH, 2);
			final IShape left = new Rectangle(0, 0, 2, CAMERA_HEIGHT);
			final IShape right = new Rectangle(CAMERA_WIDTH - 2, 0, 2,
					CAMERA_HEIGHT);

			final FixtureDef wallFixtureDef = PhysicsFactory.createFixtureDef(
					0, 0.5f, 0.5f);
			PhysicsFactory.createBoxBody(this.world, ground,
					BodyType.StaticBody, wallFixtureDef);
			PhysicsFactory.createBoxBody(this.world, roof, BodyType.StaticBody,
					wallFixtureDef);
			PhysicsFactory.createBoxBody(this.world, left, BodyType.StaticBody,
					wallFixtureDef);
			PhysicsFactory.createBoxBody(this.world, right,
					BodyType.StaticBody, wallFixtureDef);
			ground.setUserData(new Wall());
			getScene().attachChild(ground);
			roof.setUserData(new Wall());
			getScene().attachChild(roof);
			left.setUserData(new Wall());
			getScene().attachChild(left);
			right.setUserData(new Wall());
			getScene().attachChild(right);
		}

		getScene().registerUpdateHandler(this.world);

		this.world.setContactListener(new ContactListener() {

			@Override
			public void beginContact(Contact contact) {

				if (contact.getFixtureA().getBody().getUserData() == null)
					return;

				if (contact.getFixtureB().getBody().getUserData() == null)
					return;

				// double contactSession = Math.floor(Math.random()*100);

				if (contact.getFixtureA().getBody().getUserData().getClass() == Disc.class) {
					// Log.d("g", contactSession + " contact by disc A" );
					((Disc) contact.getFixtureA().getBody().getUserData())
							.addContact(contact);
				}

				if (contact.getFixtureA().getBody().getUserData().getClass() == Wall.class) {
					// Log.d("g", contactSession + " contact by wall");
					((Wall) contact.getFixtureA().getBody().getUserData())
							.addContact(contact);
				}

				if (contact.getFixtureB().getBody().getUserData().getClass() == Disc.class) {
					// Log.d("g", contactSession + " contact by disc B");
					((Disc) contact.getFixtureB().getBody().getUserData())
							.addContact(contact);
				}

				if (contact.getFixtureB().getBody().getUserData().getClass() == Wall.class) {
					// Log.d("g", contactSession + " contact by wall");
					((Wall) contact.getFixtureB().getBody().getUserData())
							.addContact(contact);
				}

			}

			@Override
			public void endContact(Contact contact) {

				if (contact.getFixtureA().getBody().getUserData() == null)
					return;

				if (contact.getFixtureB().getBody().getUserData() == null)
					return;

				// Log.d("g", "contact ended");
				((Disc) contact.getFixtureA().getBody().getUserData())
						.removeContact(contact);

				((Disc) contact.getFixtureB().getBody().getUserData())
						.removeContact(contact);

			}

			@Override
			public void postSolve(Contact arg0, ContactImpulse arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void preSolve(Contact arg0, Manifold arg1) {
				// TODO Auto-generated method stub

			}
		});

		return getScene();
	}

	public void addRectangle(float x, float y, float w, float h, float d,
			float e, float f, float r, float g, float b) {
		final Rectangle box = new Rectangle(x, y, w, h);
		box.setColor(r, g, b);
		final Body boxbody = PhysicsFactory.createBoxBody(this.world, box,
				BodyType.DynamicBody, PhysicsFactory.createFixtureDef(d, e, f));
		getScene().attachChild(box);
		// getScene().registerTouchArea(box);
		getWorld().registerPhysicsConnector(
				new PhysicsConnector(box, boxbody, true, true));

	}

	public Scene getScene() {
		return this.scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public PhysicsWorld getWorld() {
		return this.world;
	}

	public TextureRegion getmFaceTextureRegionGreen() {
		return mFaceTextureRegionGreen;
	}

	public void setmFaceTextureRegionGreen(TextureRegion mFaceTextureRegion) {
		this.mFaceTextureRegionGreen = mFaceTextureRegion;
	}

	public TextureRegion getmFaceTextureRegionRed() {
		return mFaceTextureRegionRed;
	}

	public void setmFaceTextureRegionRed(TextureRegion mFaceTextureRegion) {
		this.mFaceTextureRegionRed = mFaceTextureRegion;
	}

	public TextureRegion getmFaceTextureRegionYellow() {
		return mFaceTextureRegionYellow;
	}

	public void setmFaceTextureRegionYellow(TextureRegion mFaceTextureRegion) {
		this.mFaceTextureRegionYellow = mFaceTextureRegion;
	}

	public TextureRegion getmFaceTextureRegionBlue() {
		return mFaceTextureRegionBlue;
	}

	public void setmFaceTextureRegionBlue(TextureRegion mFaceTextureRegion) {
		this.mFaceTextureRegionBlue = mFaceTextureRegion;
	}

	public TiledTextureRegion getmBlueDiscTextureRegion() {
		return mBlueDiscTextureRegion;
	}

	public TiledTextureRegion getmYellowDiscTextureRegion() {
		return mYellowDiscTextureRegion;
	}

	public TiledTextureRegion getmGreenDiscTextureRegion() {
		return mGreenDiscTextureRegion;
	}

	public TiledTextureRegion getmRedDiscTextureRegion() {
		return mRedDiscTextureRegion;
	}

	public TiledTextureRegion getmRandomDiscTextureRegion() {
		double r = 4 * Math.random();

		if (r >= 0 && r < 1) {
			return mYellowDiscTextureRegion.deepCopy();
		} else if (r >= 1 && r < 2) {
			return mRedDiscTextureRegion.deepCopy();
		} else if (r >= 2 && r < 3) {
			return mGreenDiscTextureRegion.deepCopy();
		} else {
			return mBlueDiscTextureRegion.deepCopy();
		}
	}
	
	public TiledTextureRegion getmRandomMoonTextureRegion() {
		double r = 4 * Math.random();

		if (r >= 0 && r < 1) {
			return mYellowMoonTextureRegion.deepCopy();
		} else if (r >= 1 && r < 2) {
			return mRedMoonTextureRegion.deepCopy();
		} else if (r >= 2 && r < 3) {
			return mGreenMoonTextureRegion.deepCopy();
		} else {
			return mBlueMoonTextureRegion.deepCopy();
		}
	}


	public void setmBlueDiscTextureRegion(TiledTextureRegion mFaceTextureRegion) {
		this.mBlueDiscTextureRegion = mFaceTextureRegion;
	}

	public void setmRedDiscTextureRegion(TiledTextureRegion mFaceTextureRegion) {
		this.mRedDiscTextureRegion = mFaceTextureRegion;
	}

	public void setmYellowDiscTextureRegion(
			TiledTextureRegion mFaceTextureRegion) {
		this.mYellowDiscTextureRegion = mFaceTextureRegion;
	}

	public void setmGreenDiscTextureRegion(TiledTextureRegion mFaceTextureRegion) {
		this.mGreenDiscTextureRegion = mFaceTextureRegion;
	}

	public TiledTextureRegion getmRedMoonTextureRegion() {
		return mRedMoonTextureRegion;
	}
	public TiledTextureRegion getmYellowMoonTextureRegion() {
		return mYellowMoonTextureRegion;
	}
	public TiledTextureRegion getmGreenMoonTextureRegion() {
		return mGreenMoonTextureRegion;
	}
	public TiledTextureRegion getmBlueMoonTextureRegion() {
		return mBlueMoonTextureRegion;
	}

	public void setmYellowMoonTextureRegion(TiledTextureRegion mFaceTextureRegion) {
		this.mYellowMoonTextureRegion = mFaceTextureRegion;
	}
	public void setmRedMoonTextureRegion(TiledTextureRegion mFaceTextureRegion) {
		this.mRedMoonTextureRegion = mFaceTextureRegion;
	}
	public void setmBlueMoonTextureRegion(TiledTextureRegion mFaceTextureRegion) {
		this.mBlueMoonTextureRegion = mFaceTextureRegion;
	}
	public void setmGreenMoonTextureRegion(TiledTextureRegion mFaceTextureRegion) {
		this.mGreenMoonTextureRegion = mFaceTextureRegion;
	}

	@Override
	public void onAccelerometerChanged(
			final AccelerometerData pAccelerometerData) {
		final Vector2 gravity = Vector2Pool.obtain(pAccelerometerData.getX(),
				pAccelerometerData.getY());
		this.world.setGravity(gravity);
		Vector2Pool.recycle(gravity);
	}

	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {
		if (pKeyCode == KeyEvent.KEYCODE_MENU
				&& pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if (getScene().hasChildScene()) {
				/* Remove the menu and reset it. */
				this.mMenuScene.back();
			} else {
				/* Attach the menu. */
				getScene().setChildScene(this.mMenuScene, false, true, true);
			}
			return true;
		} else {
			return super.onKeyDown(pKeyCode, pEvent);
		}

	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		switch (pMenuItem.getID()) {
		case MENU_HOME:
			/* End Activity. */
			this.finish();
			return true;
		default:
			return false;
		}

	}

//	protected void createMenuScene() {
//		this.mMenuScene = new MenuScene(this.mCamera);
//
//		final SpriteMenuItem homeMenuItem = new SpriteMenuItem(MENU_HOME,
//				this.mMenuHomeTextureRegion);
////		this.mMenuScene.addMenuItem(homeMenuItem);
//
//		this.mMenuScene.buildAnimations();
//
//		this.mMenuScene.setBackgroundEnabled(false);
//
//		this.mMenuScene.setOnMenuItemClickListener(this);
//	}

	public Engine getEngine() {
		return engine;
	}

	public void setEngine(Engine engine) {
		this.engine = engine;
	}

	public TiledTextureRegion getBrick1ttr() {
		return brick1ttr;
	}

	public void setBrick1ttr(TiledTextureRegion brick1ttr) {
		this.brick1ttr = brick1ttr;
	}

}
