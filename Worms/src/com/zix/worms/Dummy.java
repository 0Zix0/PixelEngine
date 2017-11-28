package com.zix.worms;

import java.util.ArrayList;

import com.engine.Renderer;
import com.engine.Vector2f;

public class Dummy extends PhysicsObject {

	private static ArrayList<Vector2f> model = generateModel();
	
	public Dummy(float x, float y) {
		super(x, y);
	}

	public void render(Renderer renderer, float xOffset, float yOffset) {
		renderer.drawWireFrame(model, px - xOffset, py - yOffset, (float)Math.atan2(vy, vx), radius, 0xFFFFFF);
	}
	
	private static ArrayList<Vector2f> generateModel() {
		ArrayList<Vector2f> res = new ArrayList<>();
		res.add(new Vector2f(0, 0));
		for(int i = 0; i < 10; i++) {
			res.add(new Vector2f((float)Math.cos(i / 9.0f * 2.0f * Math.PI), (float)Math.sin(i / 9.0f * 2.0f * Math.PI)));
		}
		return res;
	}
}
