package com.zix.worms;

import java.util.ArrayList;

import com.engine.Renderer;
import com.engine.Sprite;
import com.engine.Vector2f;

public class Missile extends PhysicsObject {

	private static ArrayList<Vector2f> model = generateModel();

	public Missile(float x, float y, float velX, float velY) {
		super(x, y);
		radius = 2.5f;
		friction = 0.5f;
		vx = velX;
		vy = velY;
		dead = false;
		maxBounce = 1;
	}
	
	public Missile(float x, float y) {
		super(x, y);
		radius = 2.5f;
		friction = 0.5f;
		dead = false;
		maxBounce = 1;
	}

	public void render(Renderer renderer, float xOffset, float yOffset) {
		renderer.drawWireFrame(model, px - xOffset, py - yOffset, (float)Math.atan2(vy, vx), radius, 0xF2CB41);
	}
	
	public int deathAction() {
		return 20;
	}
	
	private static ArrayList<Vector2f> generateModel() {
		ArrayList<Vector2f> res = new ArrayList<>();
		res.add(new Vector2f(0.0f, 0.0f));
		res.add(new Vector2f(1.0f, 1.0f));
		res.add(new Vector2f(2.0f, 1.0f));
		res.add(new Vector2f(2.5f, 0.0f));
		res.add(new Vector2f(2.0f, -1.0f));
		res.add(new Vector2f(1.0f, -1.0f));
		res.add(new Vector2f(0.0f, 0.0f));
		res.add(new Vector2f(-1.0f, -1.0f));
		res.add(new Vector2f(-2.5f, -1.0f));
		res.add(new Vector2f(-2.0f, 0.0f));
		res.add(new Vector2f(-2.5f, 1.0f));
		res.add(new Vector2f(-1.0f, 1.0f));
		
		for(Vector2f vec : res) {
			vec.x /= 2.5f;
			vec.y /= 2.5f;
		}
		
		return res;
	}
}
