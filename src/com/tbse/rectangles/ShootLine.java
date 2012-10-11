package com.tbse.rectangles;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;

public class ShootLine {

	private Scene scene;
	private Line line;
	
	public ShootLine(Scene scene, float startX, float startY, float endX, float endY) {
		this.scene = scene;
		this.line = new Line(startX, startY, endX, endY);
		scene.attachChild(this.line);
	}
	
	public void update(float dx, float dy, float x, float y) {
		line.setPosition(dx, dy, x, y);
	}
	
	public void detachSelf() {
		scene.detachChild(scene.getLastChild());
	}
	
}
