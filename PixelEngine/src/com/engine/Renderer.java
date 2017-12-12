package com.engine;

import java.util.ArrayList;

public class Renderer {

	private int width, height;
	private int[] pixels;
	
	public Renderer(int width, int height, int[] pixels) {
		this.width = width;
		this.height = height;
		this.pixels = pixels;
	}
	
	public void clear() {
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0x000000;
		}
	}
	
	/**
	 * Renders a wire frame model to the screen.
	 * @param coords Array list of coordinates to draw.
	 * @param x The x position to draw to.
	 * @param y The y position to draw to.
	 * @param r Rotation of the model, in radians.
	 * @param s Scale factor of the model, to change the size.
	 * @param color The color that the model should be drawn as.
	 */
	public void drawWireFrame(ArrayList<Vector2f> coords, float x, float y, float r, float s, int color) {
		ArrayList<Vector2f> transformed = new ArrayList<>();
		int verts = coords.size();
		
		// Rotate
		for (int i = 0; i < verts; i++) {
			transformed.add(new Vector2f(0, 0));
			transformed.get(i).x = (float) (coords.get(i).x * Math.cos(r) - coords.get(i).y * Math.sin(r));
			transformed.get(i).y = (float) (coords.get(i).x * Math.sin(r) + coords.get(i).y * Math.cos(r));
		}

		// Scale
		for (int i = 0; i < verts; i++) {
			transformed.get(i).x = transformed.get(i).x * s;
			transformed.get(i).y = transformed.get(i).y * s;
		}

		// Translate
		for (int i = 0; i < verts; i++) {
			transformed.get(i).x = transformed.get(i).x + x;
			transformed.get(i).y = transformed.get(i).y + y;
		}
		
		for (int i = 0; i < verts + 1; i++) {
			int j = (i + 1);
			drawLine((int)transformed.get(i % verts).x, (int)transformed.get(i % verts).y,
					 (int)transformed.get(j % verts).x, (int)transformed.get(j % verts).y, color);
		}
	}
	
	/**
	 * Draws a single sprite to the screen.
	 * @param x The x position to draw to.
	 * @param y The y position to draw to.
	 * @param sprite The sprite which to draw.
	 */
	public void drawSprite(int x, int y, Sprite sprite) {
		for (int i = 0; i < sprite.getWidth(); i++) {
			for (int j = 0; j < sprite.getHeight(); j++) {
				if (sprite.getGlyph(i, j) != ' ') {
					drawPixel(x + i, y + j, sprite.getColor(i, j));
				}
			}
		}
	}
	
	/**
	 * Draws a region from a sprite.
	 * @param x The x position to draw to.
	 * @param y The y position to draw to.
	 * @param sprite The sprite which to draw.
	 * @param ox The x position within the sprite to start drawing from.
	 * @param oy The y position within the sprite to start drawing from.
	 * @param w The height of the desired region within the sprite.
	 * @param h The width of the desired region within the sprite.
	 */
	public void drawPartialSprite(int x, int y, Sprite sprite, int ox, int oy, int w, int h) {
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				if (sprite.getGlyph(i+ox, j+oy) != 0x20)
					drawPixel(x + i, y + j, sprite.getColor(i+ox, j+oy));
			}
		}
	}
	
	/**
	 * Uses the Bresenham algorithm to draw a line between any 2 points on the screen.
	 * @param x The start x position.
	 * @param y The start y position.
	 * @param x2 The end x position.
	 * @param y2 The end y position.
	 * @param color The color of the line.
	 */
	public void drawLine(int x, int y, int x2, int y2, int color) {
	    int w = x2 - x ;
	    int h = y2 - y ;
	    int dx1 = 0, dy1 = 0, dx2 = 0, dy2 = 0 ;
	    if (w<0) dx1 = -1 ; else if (w>0) dx1 = 1 ;
	    if (h<0) dy1 = -1 ; else if (h>0) dy1 = 1 ;
	    if (w<0) dx2 = -1 ; else if (w>0) dx2 = 1 ;
	    int longest = Math.abs(w) ;
	    int shortest = Math.abs(h) ;
	    if (!(longest>shortest)) {
	        longest = Math.abs(h) ;
	        shortest = Math.abs(w) ;
	        if (h<0) dy2 = -1 ; else if (h>0) dy2 = 1 ;
	        dx2 = 0 ;            
	    }
	    int numerator = longest >> 1 ;
	    for (int i=0;i<=longest;i++) {
	        drawPixel(x,y,color) ;
	        numerator += shortest ;
	        if (!(numerator<longest)) {
	            numerator -= longest ;
	            x += dx1 ;
	            y += dy1 ;
	        } else {
	            x += dx2 ;
	            y += dy2 ;
	        }
	    }
	}
	
	/**
	 * Puts a singular pixel on the screen.
	 * @param xp The x position to draw to.
	 * @param yp The y position to daw to.
	 * @param color The color of the pixel.
	 */
	public void drawPixel(int xp, int yp, int color) {
		if(xp < 0 || xp >= width || yp < 0 || yp >= height) return;
		pixels[xp + yp * width] = color;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
