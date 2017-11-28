package com.zix.worms;

import java.util.ArrayList;

import com.engine.Renderer;
import com.engine.Vector2f;

public class Debris extends PhysicsObject {

	private static ArrayList<Vector2f> model = generateModel();
	
	public static final float INITIAL_VELOCITY = 70.0f;
	
	public Debris(float x, float y) {
		super(x, y);
		vx = (float)(INITIAL_VELOCITY * Math.cos(Math.random() * 2.0f * Math.PI));
		vy = (float)(INITIAL_VELOCITY * Math.sin(Math.random() * 2.0f * Math.PI));
		radius = 1.0f;
		friction = 0.8f;
		maxBounce = 5;
	}

	public void render(Renderer renderer, float xOffset, float yOffset) {
		renderer.drawWireFrame(model, px - xOffset, py - yOffset, (float)Math.atan2(vy, vx), radius, 0x588C4D);
	}
	
	private static ArrayList<Vector2f> generateModel() {
		ArrayList<Vector2f> res = new ArrayList<>();
		res.add(new Vector2f(0, 0));
		res.add(new Vector2f(1.0f, 0.0f));
		res.add(new Vector2f(1.0f, 1.0f));
		res.add(new Vector2f(0.0f, 0.0f));
		return res;
	}
}
