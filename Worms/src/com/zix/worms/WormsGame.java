package com.zix.worms;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import com.engine.Game;
import com.engine.Input;
import com.engine.Renderer;

public class WormsGame extends Game {

	private int mapWidth = 1024;
	private int mapHeight = 512;
	private byte[] map;
	
	private float cameraPosX = 0.0f;
	private float cameraPosY = 0.0f;
	
	private ArrayList<PhysicsObject> objectQueue = new ArrayList<>();
	private ArrayList<PhysicsObject> objects = new ArrayList<>();
	
	public void onCreate() {
		map = new byte[mapWidth * mapHeight];
		createMap();
	}

	public void onUpdate() {
		if(Input.keyDown(KeyEvent.VK_M)) {
			createMap();
		}
		
		if(Input.mouseButtonDownReset(MouseEvent.BUTTON2)) {
			objects.add(new Worm((float)(Input.mouseX() / scale()) + cameraPosX, (float)(Input.mouseY() / scale()) + cameraPosY));
		}

		if(Input.mouseButtonDownReset(MouseEvent.BUTTON1)) {
			explosion((float)(Input.mouseX() / scale()) + cameraPosX, (float)(Input.mouseY() / scale()) + cameraPosY, 9.0f);
		}
		
		if(Input.mouseButtonDownReset(MouseEvent.BUTTON3)) {
			objects.add(new Missile((float)(Input.mouseX() / scale()) + cameraPosX, (float)(Input.mouseY() / scale()) + cameraPosY));
		}
		
		float mapScrollSpeed = 2.0f;

		if(Input.keyDown(KeyEvent.VK_W)) cameraPosY -= mapScrollSpeed;
		if(Input.keyDown(KeyEvent.VK_S)) cameraPosY += mapScrollSpeed;
		if(Input.keyDown(KeyEvent.VK_A)) cameraPosX -= mapScrollSpeed;
		if(Input.keyDown(KeyEvent.VK_D)) cameraPosX += mapScrollSpeed;
		
		if(cameraPosX < 0) cameraPosX = 0;
		if(cameraPosX >= mapWidth - screenWidth()) cameraPosX = mapWidth - screenWidth();
		if(cameraPosY < 0) cameraPosY = 0;
		if(cameraPosY >= mapHeight - screenHeight()) cameraPosY = mapHeight - screenHeight();
		
		objects.addAll(objectQueue);
		objectQueue.clear();
		
		float elapsed = (1.0f / 60.0f);
		for(PhysicsObject p : objects) {
			p.ay += 94.0f;
			p.vx += p.ax * elapsed;
			p.vy += p.ay * elapsed;
			float potentialX = p.px + p.vx * elapsed;
			float potentialY = p.py + p.vy * elapsed;
			
			p.ax = 0.0f;
			p.ay = 0.0f;
			p.stable = false;
			
			float angle = (float)Math.atan2(p.vy, p.vx);
			float responseX = 0;
			float responseY = 0;
			boolean collision = false;
			
			float collisionX = 0.0f, collisionY = 0.0f;
			
			for(float r = angle - 3.14159f / 2.0f; r < angle + 3.14159f / 2.0f; r += 3.14159f / 8.0f) {
				float testPosX = (p.radius) * (float)Math.cos(r) + potentialX;
				float testPosY = (p.radius) * (float)Math.sin(r) + potentialY;
				
				if(testPosX >= mapWidth) testPosX = mapWidth - 1;
				if(testPosY >= mapHeight) testPosY = mapHeight - 1;
				if(testPosX < 0) testPosX = 0;
				if(testPosY < 0) testPosY = 0;
				
				if(map[(int)testPosY * mapWidth + (int)testPosX] != 0) {
					responseX += potentialX - testPosX;
					responseY += potentialY - testPosY;
					
					collisionX = testPosX;
					collisionY = testPosY;
					collision = true;
				}
 			}

			float magVelocity = (float) Math.sqrt(p.vx * p.vx + p.vy * p.vy);
			float magResponse = (float) Math.sqrt(responseX * responseX + responseY * responseY);
			if(collision) {
				p.stable = true;
				
				float dot = p.vx * (responseX / magResponse) + p.vy * (responseY / magResponse);
				p.vx = p.friction * (-2.0f * dot * (responseX / magResponse) + p.vx);
				p.vy = p.friction * (-2.0f * dot * (responseY / magResponse) + p.vy);
				
				if(p.maxBounce > 0) {
					p.maxBounce--;
					p.dead = p.maxBounce == 0;
					
					if(p.dead) {
						int response = p.deathAction();
						if(response > 0) {
							explosion(p.px, p.py, response);
						}
						/*
						if(p instanceof Debris) {
							for(int x = 0; x < 3; x++) {
								for(int y = 0; y < 3; y++) {
									if((x == 0 && y == 0) || (x == 2 && y == 0) || (x == 2 && y == 2) || (x == 0 && y == 2)) continue;
									int genX = (int)collisionX;
									int genY = (int)collisionY;
									map[(((genX - 1) + x) + ((genY - 1) + y) * mapWidth)] = 1;
								}
							}
						}
						*/
					}
				}
			} else {
				p.px = potentialX;
				p.py = potentialY;
			}
			if(magVelocity < 0.1f) p.stable = true;
		}
		
		// Remove dead objects.
		Iterator<PhysicsObject> iter = objects.iterator();
		while(iter.hasNext()) {
			PhysicsObject p = iter.next();
			if(p.dead) iter.remove();
		}
	}
	
	public void explosion(float worldX, float worldY, float radius) {
		circle((int)worldX, (int)worldY, (int)radius);
		
		for(PhysicsObject p : objects) {
			float dx = p.px - worldX;
			float dy = p.py - worldY;
			float dist = (float) Math.sqrt(dx * dx + dy * dy);
			if(dist < 0.00001f) dist = 0.00001f;
			if(dist < radius) {
				p.vx = ((dx / dist) * radius) * 2.5f;
				p.vy = ((dy / dist) * radius) * 2.5f;
				p.stable = false;
			}
		}
		
		for(int i = 0; i < radius; i++) {
			objectQueue.add(new Debris(worldX, worldY));
		}
	}
	
	public void circle(int xc, int yc, int r) {
		int x = 0;
		int y = r;
		int p = 3 - 2 * r;
		if(r == 0) return;
		
		while(y > x) {
			line(xc - x, xc + x, yc - y);
			line(xc - y, xc + y, yc - x);
			line(xc - x, xc + x, yc + y);
			line(xc - y, xc + y, yc + x);
			if (p < 0) {
				p += 4 * x++ + 6;
			} else {
				p += 4 * (x++ - y--) + 10;
			}
		}
	}
	
	public void line(int sx, int ex, int ny) {
		for (int i = sx; i < ex; i++)
			if (ny >= 0 && ny < mapHeight && i >= 0 && i < mapWidth)
				map[ny * mapWidth + i] = 0;
	}

	public void onRender(Renderer renderer) {
		for(int x = 0; x < renderer.getWidth(); x++) {
			for(int y = 0; y < renderer.getHeight(); y++) {
				byte val = map[(y + (int)cameraPosY) * mapWidth + (x + (int)cameraPosX)];
				switch (val) {
				case 0:
					renderer.drawPixel(x, y, 0x51B0FF);
					break;
				case 1:
					renderer.drawPixel(x, y, 0x619B55);
					break;
				case 2:
					renderer.drawPixel(x, y, 0xFF0000);
					break;
				}
			}
		}
		
		for(PhysicsObject physicsObject : objects) {
			physicsObject.render(renderer, cameraPosX, cameraPosY);
		}
	}
	
	public void createMap() {
		float[] seed = new float[mapWidth];
		for(int i = 0; i < seed.length; i++) seed[i] = (float) Math.random();
		seed[0] = 0.5f;
		float[] surface = perlinNoise1D(mapWidth, seed, 8, 2.0f);
		
		for(int x = 0; x < mapWidth; x++) {
			for(int y = 0; y < mapHeight; y++) {
				if(y >= surface[x] * mapHeight) {
					map[y * mapWidth + x] = 1;
				} else {
					map[y * mapWidth + x] = 0;
				}
			}
		}
	}
	
	public float[] perlinNoise1D(int count, float[] seed, int octaves, float bias) {
		float[] output = new float[count];
		for(int x = 0; x < count; x++) {
			float noise = 0.0f;
			float scaleAcc = 0.0f;
			float scale = 1.0f;
			
			for(int o = 0; o < octaves; o++) {
				int pitch = count >> o;
				int sample1 = (x / pitch) * pitch;
				int sample2 = (sample1 + pitch) % count;
				float blend = (float)(x - sample1) / (float)pitch;
				float sample = (1.0f - blend) * seed[sample1] + blend * seed[sample2];
				scaleAcc += scale;
				noise += sample * scale;
				scale = scale / bias;
			}
			
			output[x] = noise / scaleAcc;
		}
		return output;
	}
	
	public static void main(String[] args) {
		WormsGame game = new WormsGame();
		game.start("Worms", 256, 170, 4, true);
	}
}
