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
