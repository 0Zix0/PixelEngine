package com.zix.worms;

import com.engine.Renderer;

public abstract class PhysicsObject {

	public float px = 0.0f;
	public float py = 0.0f;
	public float vx = 0.0f;
	public float vy = 0.0f;
	public float ax = 0.0f;
	public float ay = 0.0f;
	public float friction = 0.8f;
	
	public float radius = 4.0f;
	public boolean stable = true;
	
	public boolean dead = false;
	public int maxBounce = -1;
	
	public PhysicsObject(float x, float y) {
		px = x;
		py = y;
	}
	
	public abstract void render(Renderer renderer, float xOffset, float yOffset);
	public int deathAction() { return 0; }
}
