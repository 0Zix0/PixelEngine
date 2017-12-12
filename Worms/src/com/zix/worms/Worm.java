package com.zix.worms;

import com.engine.Renderer;
import com.engine.Sprite;

public class Worm extends PhysicsObject {
	
	private static final Sprite WORM_SPRITE = Sprite.load("worms.spr");;
	
	public Worm(float x, float y) {
		super(x, y);
		radius = 3.5f;
		friction = 0.2f;
		dead = false;
		maxBounce = -1;
	}

	public void render(Renderer renderer, float xOffset, float yOffset) {
		renderer.drawPartialSprite((int)px - (int)xOffset - (int)radius, (int)py - (int)yOffset - (int)radius, WORM_SPRITE, 0, 0, 8, 8);
	}
}
